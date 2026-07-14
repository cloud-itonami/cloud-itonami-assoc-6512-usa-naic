(ns association.facts-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [association.facts :as facts]))

(deftest naic-has-spec-basis
  (let [sb (facts/spec-basis "naic")]
    (is (= 2 (count sb)))
    (is (every? #(or (str/starts-with? (:association-rule/url %) "https://www.naic.org/")
                      (str/starts-with? (:association-rule/url %) "https://content.naic.org/"))
                sb))
    (is (every? #(= "6512" (:association-rule/isic %)) sb))))

(deftest unknown-association-has-no-spec-basis
  (is (nil? (facts/spec-basis "keidanren")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["naic" "keidanren"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["keidanren"] (:missing-associations c)))))

(deftest by-topic-filters
  (is (= ["naic.unfair-trade-practices-act"]
         (mapv :association-rule/id (facts/by-topic "naic" :consumer-protection))))
  (is (empty? (facts/by-topic "naic" :labor)))
  (is (empty? (facts/by-topic "keidanren" :governance))))
