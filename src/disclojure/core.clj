(ns disclojure.core
  (:require [leipzig.melody :as m]
            [leipzig.scale :as scale]
            [leipzig.chord :as chord]
            [disclojure.live :as l]
            [overtone.live :as ol]
            [disclojure.inst :as di]
            [leipzig.live :as live]))

(methods live/play-note)

;;14.5 beats loop

(ol/recording-start "~/sandbox/aho.wav")
(ol/recording-stop)
(ol/defsynth my-sin [freq 440]
  (ol/out 0 (ol/pan2 (ol/sin-osc freq))))

(my-sin)

(ol/kill my-sin)
((ol/sample "~/sandbox/aho.wav"))
(ol/load-sample "~/sandbox/aho.wav")

(def melody
  (->> (m/phrase [2 1/2 1/2 1/2 2  1/2 1/2 1/2 2  1/2 1/2 1/2 2  1  1]
               [0 -1  0   2   -3 -4  -3  -1  -5 -6  -5  -3  -7 -6 -5])
       (m/where :pitch (comp scale/G scale/minor))
       (m/all :part :doo)
       (m/all :amp 0.6)
       (m/times 1)
       (l/assoc-track :melody)))
(l/assoc-track :melody nil)

(live/jam (l/track))
(live/stop)

(def melody-2
  (->> (m/phrase  [1/4 1/4 1/4 1/4 1/2 1/2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1 1]
                [1 2 3 4 3 2 1 1 2 1 -1 4 5 4 2 6 7 6 4 8 7 6])
       (m/where :pitch (comp scale/G scale/minor))
       (m/all :part :doo)
       (m/all :amp 0.2)
       (m/times 1)
       (l/assoc-track :melody-2)))
(l/assoc-track :melody-2 nil)

(def beats
  (->> (m/phrase (take 58 (cycle [1/4 1/4 1/4 1/4]))
         (cycle [-1 nil -5 nil]))
       (m/where :pitch (comp scale/C scale/minor))
       (m/all :part :bass)
       (m/all :amp 0.5)
       (m/times 1)
       (l/assoc-track :beats)))
(l/assoc-track :beats nil)

(live/jam (l/track))
(live/stop)

(def bass-2
  (->> (m/phrase (take 58 (cycle [1/4 1/4 1/4 1/4]))
               (cycle [0 0 0 nil]))
       (m/where :pitch (comp scale/C scale/minor))
       (m/all :part :beep)
       (m/all :amp 0.4)
       (l/assoc-track :bass-2)))
(l/assoc-track :bass-2 nil)


(def initial-track
  {:melody (m/times 1 melody)})

(live/jam (l/track))
(live/stop)

;; ===================================

(def kick-beats
  (->> (m/phrase (take 25.6 (cycle [1/2 1/2 1 1/2]))
               (take 16 (cycle (-> chord/triad (chord/root -8)))))
       (m/where :pitch (comp scale/C scale/major))
       (m/all :part :kick)
       (m/all :amp 0.4)
       (m/times 1)
       (l/assoc-track :kick-beats)))
(l/assoc-track :kick-beats nil)

(live/jam (l/track))
(live/stop)

(->> (m/phrase [4
              1/4 1/4 1/4 1/4
              6
              1/2 1/2 1/2 1/2]
             [nil
              -6  -5  -3  -7
              nil
              -1  0   2   -3])
     (m/where :pitch (comp scale/C scale/major))
     (m/all :part :trem)
     (m/all :amp 0.5)
     (l/assoc-track :trem))
(l/assoc-track :trem nil)

(->> (m/phrase (take 29 (cycle [1/2 1/2]))
             (take 29 (cycle [1 1])))
     (m/where :pitch (comp scale/C scale/major))
     (m/all :part :c-hat)
     (m/all :amp 0.2)
     (l/assoc-track :c-hat))
(l/assoc-track :c-hat nil)

(->> (m/phrase [8 6]
             [1 12])
     (m/where :pitch (comp scale/C scale/major))
     (m/all :part :pew)
     (m/all :amp 0.4)
     (l/assoc-track :pew))
(l/assoc-track :pew nil)

(live/jam (l/track))
(live/stop)
