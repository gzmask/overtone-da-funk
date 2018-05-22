(ns disclojure.core
  (:require [leipzig.melody :refer :all]
            [leipzig.scale :as scale]
            [disclojure.live :as l]
            [overtone.live :refer :all]
            [leipzig.live :as live]))

(methods live/play-note)
;; => {:bass #function[disclojure.play/eval27042/fn--27051], :stab #function[disclojure.play/eval27042/fn--27051], :plucky #function[disclojure.play/eval27042/fn--27051], :supersaw #function[disclojure.play/eval27042/fn--27051]}

;;14.5 beats loop

(definst beep [freq 440 dur 1.0 amp 1.0]
  (-> freq
      saw
      (* (env-gen (perc 0.05 dur amp) :action FREE))))

(defmethod live/play-note :beep [{hertz :pitch seconds :duration amp :amp}]
  (beep hertz seconds amp))

(def melody
  (->> (phrase [2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1 1]
               [0 -1 0 2 -3 -4 -3 -1 -5 -6 -5 -3 -7 -6 -5])
       (where :pitch (comp scale/G scale/minor))
       (all :part :beep)
       (all :amp 0.0)))
(l/assoc-track :melody (times 1 melody))

(def melody-3
  (->> (phrase  [1/4 1/4 1/4 1/4 1/2 1/2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1 1]
                [0 0 0 0 0 0 0 0 -1 0 2 -3 -4 -3 -1 -5 -6 -5 -3 -7 -6 -5])
       (where :pitch (comp scale/G scale/minor))
       (all :part :bass)
       (all :amp 0.0)))
(l/assoc-track :melody-3 (times 1 melody-3))

(def beats
  (->> (phrase (take 58 (cycle [1/4]))
               (cycle [-4 -6 -4 3 -7 -9 -7]))
       (where :pitch (comp scale/C scale/minor))
       (all :part :bass)
       (all :amp 0.0)))
(l/assoc-track :beats (times 1 beats))

(def melody-2
  (->> (phrase (take 58 (cycle [1/4 1/4 1/4 1/4]))
               (cycle [0 -1 -1 3 0 -1 -1]))
       (where :pitch (comp scale/C scale/minor))
       (all :part :bass)
       (all :amp 0.0)))
(l/assoc-track :melody-2 (times 1 melody-2))


(def initial-track
  {:melody (times 1 melody)})

(def state
  (l/reset-track initial-track))

(live/jam (l/track))
(live/stop)

