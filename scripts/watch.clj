(require '[cljs.build.api :as b]
         '[clojure.java.io :as io])
(refer 'cljs.closure :only '[js-transforms])
(import 'javax.script.ScriptEngineManager)

(def engine
  (doto (.getEngineByName (ScriptEngineManager.) "nashorn")
    (.eval (io/reader (io/file "jstransform-simple.bundle.js")))))

(defmethod js-transforms :jsx [ijs opts]
  (let [code (str (gensym))]
    (.put engine code (:source ijs))
    (assoc ijs :source (.eval engine (str "simple.transform(" code ", {react: true}).code")))))

(b/watch "src"
  {:main 'circle-color.core
   :asset-path "js/out"
   :output-to "resources/public/js/out/circle_color.js"
   :output-dir "resources/public/js/out"
   :verbose true
   :compiler-stats true
   :pretty-print true
   :foreign-libs [{:file "src/libs/Circle.js"
                   :provides ["libs.Circle"]
                   :module-type :commonjs
                   :preprocess :jsx}
                  {:file "src/libs/React.js"
                   :provides ["libs.React"]
                   :module-type :commonjs}]
   :closure-warnings {:non-standard-jsdoc :off}})
