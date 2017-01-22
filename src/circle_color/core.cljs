(ns circle-color.core
  (:require [clojure.browser.repl :as repl]
            [libs.React :as React]
            [libs.Circle :as Circle]))

(defn bridge [m]
  (specify! m
    Object
    (get [_ k]
      (get m (keyword k)))))

(def ColorInput
  (React/createClass
   #js {:render
        (fn []
          (this-as this
            (React/createElement "div" nil
              (React/createElement "input" #js {:type "text"
                                          :className "center"
                                          :onChange (.. this -props -onChange)}))))}))

(def Container
  (React/createClass
   #js {:getInitialState (fn [] #js {:color ""})
        :handleColorChange (fn [event]
                             (this-as this
                               (.setState this #js {:color (.. event -target -value)})))
        :render (fn []
                  (this-as this
                    (React/createElement "div" nil
                      (React/createElement ColorInput #js {:onChange (. this -handleColorChange)})
                      (React/createElement Circle/Circle
                        (bridge {:color (.. this -state -color)})))))}))

(React/render
  (React/createElement Container)
  (.getElementById js/document "app"))
