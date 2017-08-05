(ns user
  (:require [mount.core :as mount]
            app.core))

(defn start []
  (mount/start-without #'app.core/repl-server))

(defn stop []
  (mount/stop-except #'app.core/repl-server))

(defn restart []
  (stop)
  (start))


