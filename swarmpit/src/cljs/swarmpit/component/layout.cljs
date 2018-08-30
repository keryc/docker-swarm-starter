(ns swarmpit.component.layout
  (:require [rum.core :as rum]
            [clojure.string :as str]
            [swarmpit.view :as view]
            [swarmpit.component.state :as state]
            [swarmpit.component.menu :as menu]
            [swarmpit.component.header :as header]))

(enable-console-print!)

(def page-titles
  {:index                 "Home"
   :password              "Change password"
   :api-access            "API access"
   :stack-list            "Stacks"
   :stack-create          "Stacks / Create"
   :stack-compose         "Stacks / Edit"
   :stack-last            "Stacks / Edit"
   :stack-previous        "Stacks / Edit"
   :stack-info            "Stacks"
   :service-list          "Services"
   :service-create-config "Services / Wizard"
   :service-create-image  "Services / Wizard"
   :service-info          "Services"
   :service-log           "Services / Log"
   :service-edit          "Services / Edit"
   :network-list          "Networks"
   :network-create        "Networks / Create"
   :network-info          "Networks"
   :volume-list           "Volumes"
   :volume-create         "Volumes / Create"
   :volume-info           "Volumes"
   :secret-list           "Secrets"
   :secret-create         "Secrets / Create"
   :secret-info           "Secrets"
   :config-list           "Configs"
   :config-create         "Configs / Create"
   :config-info           "Configs"
   :node-list             "Nodes"
   :node-info             "Nodes"
   :node-edit             "Nodes / Edit"
   :task-list             "Tasks"
   :task-info             "Tasks"
   :user-list             "Users"
   :user-create           "Users / Create"
   :user-edit             "Users / Edit"
   :user-info             "Users"
   :registry-info         "Registries"
   :registry-list         "Registries"
   :registry-create       "Registries / Create"
   :registry-edit         "Registries / Edit"
   :dockerhub-user-info   "Dockerhub"
   :dockerhub-user-list   "Dockerhub"
   :dockerhub-user-create "Dockerhub / Create"
   :dockerhub-user-edit   "Dockerhub / Edit"})

(defn- page-title
  [handler]
  (get page-titles handler))

(defn- page-domain
  [handler]
  (keyword (first (str/split (name handler) #"-"))))

(def single-pages
  #{:login :error :unauthorized :not-found nil})

(defn- page-layout?
  [handler]
  (not (contains? single-pages handler)))

(defn- document-title
  [page-title]
  (set! (-> js/document .-title)
        (str page-title " :: swarmpit")))

(rum/defc page-single < rum/static [route]
  (view/dispatch route))

(rum/defc page-layout < rum/reactive [route]
  (let [{:keys [opened]} (state/react state/layout-cursor)
        {:keys [handler]} route
        layout-type (if opened
                      "layout-opened"
                      "layout-closed")
        page-title (page-title handler)
        page-domain (page-domain handler)]
    (document-title page-title)
    [:div {:class ["layout" layout-type]}
     [:header (header/appbar page-title)]
     [:nav (menu/drawer page-domain)]
     [:main (view/dispatch route)]]))

(rum/defc layout < rum/reactive []
  (let [{:keys [handler] :as route} (state/react state/route-cursor)]
    (if (page-layout? handler)
      (page-layout route)
      (page-single route))))

(defn mount!
  []
  (rum/mount (layout) (.getElementById js/document "layout")))