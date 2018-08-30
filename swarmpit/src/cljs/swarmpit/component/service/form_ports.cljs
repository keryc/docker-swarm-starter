(ns swarmpit.component.service.form-ports
  (:require [material.component :as comp]
            [material.component.form :as form]
            [material.component.list-table-form :as list]
            [swarmpit.component.state :as state]
            [swarmpit.component.parser :refer [parse-int]]
            [swarmpit.ajax :as ajax]
            [swarmpit.routes :as routes]
            [sablono.core :refer-macros [html]]
            [rum.core :as rum]))

(enable-console-print!)

(def form-value-cursor (conj state/form-value-cursor :ports))

(defn- not-suggested?
  [port]
  (not
    (some #(= (:containerPort port)
              (:containerPort %)) (state/get-value form-value-cursor))))

(defn load-suggestable-ports
  [repository]
  (ajax/get
    (routes/path-for-backend :repository-ports)
    {:params     {:repository    (:name repository)
                  :repositoryTag (:tag repository)}
     :on-success (fn [{:keys [response]}]
                   (doseq [port response]
                     (if (not-suggested? port)
                       (state/add-item (merge port
                                              {:mode "ingress"}) form-value-cursor))))
     :on-error   (fn [_])}))

(def headers [{:name  "Container port"
               :width "100px"}
              {:name  "Protocol"
               :width "100px"}
              {:name  "Mode"
               :width "150px"}
              {:name  "Host port"
               :width "100px"}])

(defn- form-container [value index]
  (list/textfield
    {:name     (str "form-container-text-" index)
     :key      (str "form-container-text-" index)
     :type     "number"
     :min      1
     :max      65535
     :value    value
     :onChange (fn [_ v]
                 (state/update-item index :containerPort (parse-int v) form-value-cursor))}))

(defn- form-protocol [value index]
  (list/selectfield
    {:name     (str "form-protocol-select-" index)
     :key      (str "form-protocol-select-" index)
     :value    value
     :onChange (fn [_ _ v]
                 (state/update-item index :protocol v form-value-cursor))}
    (comp/menu-item
      {:name        (str "form-protocol-tcp-" index)
       :key         (str "form-protocol-tcp-" index)
       :value       "tcp"
       :primaryText "TCP"})
    (comp/menu-item
      {:name        (str "form-protocol-udp-" index)
       :key         (str "form-protocol-udp-" index)
       :value       "udp"
       :primaryText "UDP"})))

(defn- form-mode [value index]
  (list/selectfield
    {:name     (str "form-mode-select-" index)
     :key      (str "form-mode-select-" index)
     :value    value
     :onChange (fn [_ _ v]
                 (state/update-item index :mode v form-value-cursor))}
    (comp/menu-item
      {:name        (str "form-mode-ingress-" index)
       :key         (str "form-mode-ingress-" index)
       :value       "ingress"
       :primaryText "ingress"})
    (comp/menu-item
      {:name        (str "form-mode-host-" index)
       :key         (str "form-mode-host-" index)
       :value       "host"
       :primaryText "host"})))

(defn- form-host [value index]
  (list/textfield
    {:name     (str "form-host-text-" index)
     :key      (str "form-host-text-" index)
     :type     "number"
     :min      1
     :max      65535
     :value    value
     :onChange (fn [_ v]
                 (state/update-item index :hostPort (parse-int v) form-value-cursor))}))

(defn- render-ports
  [item index _]
  (let [{:keys [containerPort
                protocol
                mode
                hostPort]} item]
    [(form-container containerPort index)
     (form-protocol protocol index)
     (form-mode mode index)
     (form-host hostPort index)]))

(defn- form-table
  [ports]
  (form/form
    {}
    (list/table-raw headers
                    ports
                    nil
                    render-ports
                    (fn [index] (state/remove-item index form-value-cursor)))))

(defn- add-item
  []
  (state/add-item {:containerPort 0
                   :protocol      "tcp"
                   :mode          "ingress"
                   :hostPort      0} form-value-cursor))

(rum/defc form < rum/reactive []
  (let [ports (state/react form-value-cursor)]
    (if (empty? ports)
      (form/value "Service has no published ports.")
      (form-table ports))))