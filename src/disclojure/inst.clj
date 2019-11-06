(ns disclojure.inst
  (:require [overtone.live :refer :all]
            [overtone.inst.drum :as drum]
            [overtone.inst.synth :as synth]
            [leipzig.live :as live]))

(definst doo [hertz 440 dur 1.0 amp 1.0]
  (-> (pan2 (sin-osc hertz) -0.5)
    (* (env-gen (perc 0.05 dur amp) :action FREE))))


(defmethod live/play-note :doo [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (doo hertz seconds amp)))

(definst beep [freq 440 dur 1.0 amp 0.2]
  (-> freq
    saw
    (* (env-gen (perc 0.02 dur amp) :action FREE))))


(defmethod live/play-note :beep [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (beep hertz seconds amp)))

(defmethod live/play-note :kick [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (drum/kick4 hertz amp (* seconds 0.01) (* seconds 0.99))))

(defmethod live/play-note :bass [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (synth/bass hertz seconds amp)))

(definst pew [freq 440 dur 3 amp 1]
  (* (env-gen (perc 0.001 dur amp) 1 1 0 1 FREE)
    (lpf (mix (saw [freq (line freq 1600 5) (line (/ freq 2) 800 5) (line (/ freq 4) freq 5)]))
      (lin-lin (lf-tri (line 2 20 5)) -1 1 200 4000))))

(defmethod live/play-note :pew [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (pew hertz seconds amp)))

(definst c-hat [amp 0.8 t 0.04]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))

(defmethod live/play-note :c-hat [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (c-hat amp seconds)))

(definst trem [freq 440 length 4 amp 0.2 depth 10 rate 6]
  (* (env-gen (perc 0.01 0.8 amp) 1 1 0 1 FREE)
    (saw (+ freq (* depth (sin-osc:kr rate))))))

(defmethod live/play-note :trem [{hertz :pitch seconds :duration amp :amp}]
  (when hertz
    (trem hertz amp seconds)))
