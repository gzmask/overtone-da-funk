(ns disclojure.core
  (:require [leipzig.melody :refer :all]
            [leipzig.scale :as scale]
            [leipzig.chord :as chord]
            [disclojure.live :as l]
            [overtone.live :refer :all]
            [disclojure.inst :as di]
            [leipzig.live :as live]))

(methods live/play-note)

;;14.5 beats loop

(recording-start "~/sandbox/aho.wav")
(defsynth my-sin [freq 440]
  (out 0 (pan2 (sin-osc freq))))

(my-sin)

(kill my-sin)
(recording-stop)
((sample "~/sandbox/aho.wav"))
((load-sample "~/sandbox/aho.wav"))

(def melody
  (->> (phrase [2 1/2 1/2 1/2 2  1/2 1/2 1/2 2  1/2 1/2 1/2 2  1  1]
               [0 -1  0   2   -3 -4  -3  -1  -5 -6  -5  -3  -7 -6 -5])
       (where :pitch (comp scale/G scale/minor))
       (all :part :beep)
       (all :amp 0.4)
       (times 1)
       (l/assoc-track :melody)))
(l/assoc-track :melody nil)

(live/jam (l/track))
(live/stop)

(def melody-2
  (->> (phrase  [1/4 1/4 1/4 1/4 1/2 1/2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1 1]
                [1 2 3 4 3 2 1 1 2 1 -1 4 5 4 2 6 7 6 4 8 7 6])
       (where :pitch (comp scale/G scale/minor))
       (all :part :doo)
       (all :amp 0.2)
       (times 1)
       (l/assoc-track :melody-2)))
(l/assoc-track :melody-2 nil)

(def beats
  (->> (phrase (take 58 (cycle [1/4 1/4 1/4 1/4]))
         (cycle [-1 nil -5 nil]))
       (where :pitch (comp scale/C scale/minor))
       (all :part :bass)
       (all :amp 0.5)
       (times 1)
       (l/assoc-track :beats)))
(l/assoc-track :beats nil)

(live/jam (l/track))
(live/stop)

(def bass-2
  (->> (phrase (take 58 (cycle [1/4 1/4 1/4 1/4]))
               (cycle [0 0 0 nil]))
       (where :pitch (comp scale/C scale/minor))
       (all :part :beep)
       (all :amp 0.4)
       (l/assoc-track :bass-2)))
(l/assoc-track :bass-2 nil)


(def initial-track
  {:melody (times 1 melody)})

(live/jam (l/track))
(live/stop)

;; ===================================

(defmethod live/play-note :kick-beats [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (kick hertz seconds amp)))

(def kick-beats
  (->> (phrase (take 25.6 (cycle [1/2 1/2 1 1/2]))
               (take 16 (cycle (-> chord/triad (chord/root -8)))))
       (where :pitch (comp scale/C scale/major))
       (all :part :kick)
       (all :amp 0.4)
       (times 1)
       (l/assoc-track :kick-beats)))
(l/assoc-track :kick-beats nil)

(live/jam (l/track))
(live/stop)

(->> (phrase [4
              1/4 1/4 1/4 1/4
              6
              1/2 1/2 1/2 1/2]
             [nil
              -6  -5  -3  -7
              nil
              -1  0   2   -3])
     (where :pitch (comp scale/C scale/major))
     (all :part :trem)
     (all :amp 0.5)
     (l/assoc-track :trem))
(l/assoc-track :trem nil)

(->> (phrase (take 29 (cycle [1/2 1/2]))
             (take 29 (cycle [1 1])))
     (where :pitch (comp scale/C scale/major))
     (all :part :c-hat)
     (all :amp 0.2)
     (l/assoc-track :c-hat))
(l/assoc-track :c-hat nil)

(->> (phrase [8 6]
             [1 12])
     (where :pitch (comp scale/C scale/major))
     (all :part :pew)
     (all :amp 0.4)
     (l/assoc-track :pew))
(l/assoc-track :pew nil)

(live/jam (l/track))
(live/stop)
