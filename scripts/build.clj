(require '[cljs.build.api :as b]
         '[clojure.java.io :as io])
(refer 'cljs.closure :only '[js-transforms])
(import 'javax.script.ScriptEngineManager)

(defmethod js-transforms :jsx [ijs opts]
  (let [engine (doto (.getEngineByName (ScriptEngineManager.) "nashorn")
                 (.eval (io/reader (io/file "jstransform-simple.bundle.js")))
                 (.put "originalCode" (:source ijs)))]
    (assoc ijs :source
      (.eval engine (str "simple.transform(originalCode, {react: true}).code")))))

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
    {:main 'circle-color.core
     :asset-path "js/out"
     :output-to "resources/public/js/out/circle_color.js"
     :output-dir "resources/public/js/out"
     :verbose true
     ;:pretty-print true
     :optimizations :none
     :foreign-libs [{:file "src"
                     :module-type :commonjs
                     :preprocess :jsx}]
     :externs ["resources/public/js/externs/externs.js"]
     :parallel-build true
     :closure-warnings {:non-standard-jsdoc :off}})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
