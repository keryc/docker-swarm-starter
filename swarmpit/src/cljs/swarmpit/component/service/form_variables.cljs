(ns swarmpit.component.service.form-variables
  (:require [material.component.form :as form]
            [material.component.list-table-form :as list]
            [swarmpit.component.state :as state]
            [sablono.core :refer-macros [html]]
            [rum.core :as rum]))

(enable-console-print!)

(def form-value-cursor (conj state/form-value-cursor :variables))

(def headers [{:name  "Name"
               :width "35%"}
              {:name  "Value"
               :width "35%"}])

(defn- form-name [value index]
  (list/textfield
    {:name     (str "form-name-text-" index)
     :key      (str "form-name-text-" index)
     :value    value
     :onChange (fn [_ v]
                 (state/update-item index :name v form-value-cursor))}))

(defn- form-value [value index]
  (list/textfield
    {:name     (str "form-value-text-" index)
     :key      (str "form-value-text-" index)
     :value    value
     :onChange (fn [_ v]
                 (state/update-item index :value v form-value-cursor))}))

(defn- render-variables
  [item index]
  (let [{:keys [name
                value]} item]
    [(form-name name index)
     (form-value value index)]))

(defn- form-table
  [variables]
  (form/form
    {}
    (list/table-raw headers
                    variables
                    nil
                    render-variables
                    (fn [index] (state/remove-item index form-value-cursor)))))

(defn- add-item
  []
  (state/add-item {:name  ""
                   :value ""} form-value-cursor))

(rum/defc form < rum/reactive []
  (let [variables (state/react form-value-cursor)]
    (if (empty? variables)
      (form/value "No environment variables defined for the service.")
      (form-table variables))))