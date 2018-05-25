(ns disclojure.core
  (:require [leipzig.melody :refer :all]
            [leipzig.scale :as scale]
            [leipzig.chord :as chord]
            [disclojure.live :as l]
            [overtone.live :refer :all]
            [leipzig.live :as live]))

(methods live/play-note)
;; => {:bass #function[disclojure.play/eval27042/fn--27051], :stab #function[disclojure.play/eval27042/fn--27051], :plucky #function[disclojure.play/eval27042/fn--27051], :supersaw #function[disclojure.play/eval27042/fn--27051]}

;;14.5 beats loop

(defsynth my-sin [freq 440]
  (out 0 (pan2 (sin-osc freq))))

(my-sin)

(kill my-sin)

(definst doo [freq 440 dur 1.0 amp 1.0]
  (-> (pan2 (sin-osc freq))
      (* (env-gen (perc 0.05 dur amp) :action FREE))))

(defmethod live/play-note :doo [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (doo hertz seconds amp)))

(definst beep [freq 440 dur 1.0 amp 1.0]
  (-> freq
      saw
      (* (env-gen (perc 0.05 dur amp) :action FREE))))

(defmethod live/play-note :beep [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (beep hertz seconds amp)))

(def melody
  (->> (phrase [2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1 1 1]
               [0 -1 0 2 -3 -4 -3 -1 -5 -6 -5 -3 -7 -6 -5])
       (where :pitch (comp scale/G scale/minor))
       (all :part :supersaw)
       (all :amp 0.8)
       (times 1)
       (l/assoc-track :melody)))
(l/assoc-track :melody nil)

(def melody-2
  (->> (phrase  [1/4 1/4 1/4 1/4 1/2 1/2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1/2 1/2 1/2 2 1 1]
                [1 2 3 4 3 2 1 1 2 1 -1 4 5 4 2 6 7 6 4 8 7 6])
       (where :pitch (comp scale/G scale/minor))
       (all :part :beep)
       (all :amp 0.2)
       (times 1)
       (l/assoc-track :melody-2)))
(l/assoc-track :melody-2 nil)


(def beats
  (->> (phrase (take 58 (cycle [1/4 1/4 1/4 1/4]))
               (cycle [-16 nil nil nil]))
       (where :pitch (comp scale/C scale/minor))
       (all :part :bass)
       (all :amp 0.3)
       (times 1)
       (l/assoc-track :beats)))
(l/assoc-track :beats nil)

(def bass-2
  (->> (phrase (take 58 (cycle [1/4 1/4 1/4 1/4]))
               (cycle [0 nil 0 nil]))
       (where :pitch (comp scale/C scale/minor))
       (all :part :beep)
       (all :amp 0.4)
       (l/assoc-track :bass-2)))
(l/assoc-track :bass-2 nil)


(def initial-track
  {:melody (times 1 melody)})

(def state
  (l/reset-track initial-track))
(live/jam (l/track))
(live/stop)

;; ===================================
(definst kick [freq 120 dur 0.3 amp 1.0 width 0.5]
  (let [freq-env (* freq (env-gen (perc 0 (* 0.99 dur))))
        env (env-gen (perc 0.01 dur amp) 1 1 0 1 FREE)
        sqr (* (env-gen (perc 0 0.01)) (pulse (* 2 freq) width))
        src (sin-osc freq-env)
        drum (+ sqr (* env src))]
    (compander drum drum 0.2 1 0.1 0.01 0.01)))

(kick 330 0.8 0.5 1)

(defmethod live/play-note :kick-beats [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (kick hertz seconds amp)))

(def kick-beats
  (->> (phrase (take 16 (repeat 1))
               (take 16 (repeat (-> chord/triad (chord/root -5)))))
       (where :pitch (comp scale/C scale/major))
       (all :part :kick-beats)
       (all :amp 0.4)
       (times 1)
       (l/assoc-track :kick-beats)))
(l/assoc-track :kick-beats nil)

(def kick-track
  {:kick-beats (times 1 kick-beats)})

(def state
  (l/reset-track kick-track))

(live/jam (l/track))
(live/stop)

(definst trem [freq 440 length 4 amp 1 depth 10 rate 6]
  (* (env-gen (perc 0.01 0.8 amp) 1 1 0 1 FREE)
     (saw (+ freq (* depth (sin-osc:kr rate))))))

(defmethod live/play-note :trem [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (trem hertz amp seconds)))

(->> (phrase [4
              1/4 1/4 1/4 1/4
              6
              1 1 1 1]
             [nil
              -11 -11 -11 -11
              nil
              -3 -1 1 3])
     (where :pitch (comp scale/C scale/major))
     (all :part :trem)
     (all :amp 0.2)
     (l/assoc-track :trem))
(l/assoc-track :trem nil)

(definst c-hat [amp 0.8 t 0.04]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))

(defmethod live/play-note :c-hat [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (c-hat amp seconds)))

(->> (phrase (take 32 (cycle [1/2 1/2]))
             (take 32 (cycle [nil 1])))
     (where :pitch (comp scale/C scale/major))
     (all :part :c-hat)
     (all :amp 0.4)
     (l/assoc-track :c-hat))
(l/assoc-track :c-hat nil)

(c-hat 1 1)

(definst pew [freq 440 dur 3 amp 1]
  (* (env-gen (perc 0.001 dur amp) 1 1 0 1 FREE)
     (lpf (mix (saw [freq (line freq 1600 5) (line (/ freq 2) 800 5) (line (/ freq 4) freq 5)]))
          (lin-lin (lf-tri (line 2 20 5)) -1 1 200 4000))))

(pew 340 10 0.8)

(defmethod live/play-note :pew [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (pew hertz seconds amp)))

(->> (phrase [8 8]
             [1 14])
     (where :pitch (comp scale/C scale/major))
     (all :part :pew)
     (all :amp 0.8)
     (l/assoc-track :pew))
(l/assoc-track :pew nil)
