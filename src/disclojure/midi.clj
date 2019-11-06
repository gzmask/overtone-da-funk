(ns disclojure.midi
  (:require [leipzig.melody :refer :all]
            [leipzig.scale :as scale]
            [leipzig.chord :as chord]
            [leipzig.temperament  :as temper]
            [disclojure.live :as l]
            [overtone.live :refer :all]
            [disclojure.inst :as di]
            [leipzig.live :as live]
            [overtone.inst.drum :as drum]))

(defn kick4
  [& {:keys [note velocity gate]}]
  (let [freq (temper/equal note)
        amp (/ velocity 127.0)]
    (drum/kick :freq freq :amp-decay amp)))

(defn dub-kick
  [& {:keys [note]}]
  (drum/dub-kick (temper/equal note)))

(defn clap
  [& {:keys [note velocity]}]
  (drum/clap :low (* 10 (temper/equal note))
    :hi (* 15 (temper/equal note))
    :amp (/ velocity 127.0)))

(def ding-player (midi-poly-player clap))

(midi-player-stop ding-player)

(drum/tom)
