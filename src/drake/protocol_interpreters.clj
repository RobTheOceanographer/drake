(ns drake.protocol-interpreters
  (:use [drake-interface.core :only [Protocol]])
  (:use drake.protocol))

(def WINDOWS? (.startsWith (System/getProperty "os.name") "Win")) 

(defn- register-interpreter!
  [[protocol-name interpreter args]]
  (register-protocols! protocol-name
                       (reify Protocol
                         (cmds-required? [_] true)
                         (run [_ step]
                           (run-interpreter step interpreter args)))))

(dorun (map register-interpreter! [(if WINDOWS? 
                                     ["shell" "cmd" ["/C"]]
                                     ["shell" (get (System/getenv) "SHELL") nil]) 
                                   ["ruby" "ruby" nil]
                                   ["python" "python" nil]
                                   ["node" "node" nil]
                                   ["R" "R" ["--slave" "-f"]]]))
