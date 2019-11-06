(ns disclojure.speech
  (:require [leipzig.melody :refer :all]
            [leipzig.scale :as scale]
            [leipzig.chord :as chord]
            [disclojure.live :as l]
            [overtone.live :refer :all]
            [disclojure.inst :as di]
            [leipzig.live :as live]))

((speech-buffer "what is this black magic?" :voice :alex) :rate 1 :loop? false :out-bus 0)
((speech-buffer "what is this white magic?" :voice :boing) :rate 1 :loop? false :out-bus 0)
