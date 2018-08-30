(ns swarmpit.component.network.info
  (:require [material.icon :as icon]
            [material.component :as comp]
            [material.component.form :as form]
            [material.component.panel :as panel]
            [material.component.list-table-auto :as list]
            [swarmpit.component.message :as message]
            [swarmpit.component.state :as state]
            [swarmpit.component.mixin :as mixin]
            [swarmpit.component.progress :as progress]
            [swarmpit.component.service.list :as services]
            [swarmpit.docker.utils :as utils]
            [swarmpit.url :refer [dispatch!]]
            [swarmpit.ajax :as ajax]
            [swarmpit.routes :as routes]
            [swarmpit.time :as time]
            [rum.core :as rum]))

(enable-console-print!)

(def driver-opts-headers ["Name" "Value"])

(def driver-opts-render-keys
  [[:name] [:value]])

(defn driver-opts-render-item
  [item]
  (val item))

(defn- network-services-handler
  [network-id]
  (ajax/get
    (routes/path-for-backend :network-services {:id network-id})
    {:on-success (fn [{:keys [response]}]
                   (state/update-value [:services] response state/form-value-cursor))}))

(defn- network-handler
  [network-id]
  (ajax/get
    (routes/path-for-backend :network {:id network-id})
    {:state      [:loading?]
     :on-success (fn [{:keys [response]}]
                   (state/update-value [:network] response state/form-value-cursor))}))

(defn- delete-network-handler
  [network-id]
  (ajax/delete
    (routes/path-for-backend :network-delete {:id network-id})
    {:on-success (fn [_]
                   (dispatch!
                     (routes/path-for-frontend :network-list))
                   (message/info
                     (str "Network " network-id " has been removed.")))
     :on-error   (fn [{:keys [response]}]
                   (message/error
                     (str "Network removing failed. " (:error response))))}))

(defn- init-form-state
  []
  (state/set-value {:loading? true} state/form-state-cursor))

(def mixin-init-form
  (mixin/init-form
    (fn [{{:keys [id]} :params}]
      (init-form-state)
      (network-handler id)
      (network-services-handler id))))

(rum/defc form-info < rum/static [{:keys [network services]}]
  (let [subnet (get-in network [:ipam :subnet])
        gateway (get-in network [:ipam :gateway])]
    [:div
     [:div.form-panel
      [:div.form-panel-left
       (panel/info icon/networks
                   (:networkName network))]
      [:div.form-panel-right
       (comp/mui
         (comp/raised-button
           {:onTouchTap #(delete-network-handler (:id network))
            :label      "Delete"}))]]
     [:div.form-layout
      [:div.form-layout-group
       (form/section "General settings")
       (form/item "ID" (:id network))
       (form/item-stack (:stack network))
       (form/item "NAME" (utils/trim-stack (:stack network)
                                           (:networkName network)))
       (when (time/valid? (:created network))
         (form/item-date "CREATED" (:created network)))
       (form/item "INTERNAL" (if (:internal network) "yes" "no"))
       (form/item "ATTACHABLE" (if (:attachable network) "yes" "no"))
       (form/item "INGRESS" (if (:ingress network) "yes" "no"))
       (form/item "ENABLED IPv6" (if (:enableIPv6 network) "yes" "no"))]
      [:div.form-layout-group.form-layout-group-border
       (form/section "Driver")
       (form/item "NAME" (:driver network))
       (when (not-empty (:options network))
         [:div
          (form/subsection "Network driver options")
          (list/table driver-opts-headers
                      (:options network)
                      driver-opts-render-item
                      driver-opts-render-keys
                      nil)])]
      (when (and (some? subnet)
                 (some? gateway))
        [:div.form-layout-group.form-layout-group-border
         (form/section "IP address management")
         (form/item "SUBNET" subnet)
         (form/item "GATEWAY" gateway)])
      (services/linked-services services)]]))

(rum/defc form < rum/reactive
                 mixin-init-form
                 mixin/subscribe-form [_]
  (let [state (state/react state/form-state-cursor)
        item (state/react state/form-value-cursor)]
    (progress/form
      (:loading? state)
      (form-info item))))