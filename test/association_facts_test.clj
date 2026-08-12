(ns association-facts-test
  (:require [clojure.java.io :as io] [clojure.java.shell :as shell]
            [clojure.test :refer [deftest is testing]]
            [kotoba.compiler.core :as compiler] [kotoba.kir :as ir]))
(def source (slurp "src/association_facts.kotoba"))
(defn call [kir f & xs] (ir/execute kir f (vec xs)))
(defn present [x] (when (second x) (nth x 2)))
(def fields ["id" "title" "association" "isic" "country" "kind" "url" "url-provenance"
             "established-date" "last-revised-date" "retrieved-at"])
(def expected
  [{"id" "naic.model-laws-program" "title" "Model Laws - About"
    "association" "naic" "isic" "6512" "country" "USA" "kind" "governance-program"
    "url" "https://content.naic.org/model-laws/about"
    "url-provenance" "official-association-site" "established-date" nil
    "last-revised-date" nil "retrieved-at" "2026-07-14"}
   {"id" "naic.unfair-trade-practices-act"
    "title" "Unfair Trade Practices Act (NAIC Model Law #880)"
    "association" "naic" "isic" "6512" "country" "USA" "kind" "model-law"
    "url" "https://content.naic.org/sites/default/files/model-law-880.pdf"
    "url-provenance" "official-association-site" "established-date" nil
    "last-revised-date" "2024 (Spring 2024 edition)" "retrieved-at" "2026-07-14"}])
(deftest reference-preserves-authority
  (let [kir (:kir (compiler/compile-source source :js-kotoba-v1))
        observed (mapv (fn [i] (into {} (map (fn [f] [f (present (call kir 'entry-field "naic" i f))]) fields))) [0 1])]
    (is (= expected observed))
    (is (= [[nil nil] [nil "2024 (Spring 2024 edition)"]]
           (mapv (fn [i] (mapv #(present (call kir 'entry-field "naic" i %)) ["established-date" "last-revised-date"])) [0 1])))
    (is (= [["governance"] ["consumer-protection" "fair-competition"]]
           (mapv (fn [i] (mapv #(present (call kir 'topic "naic" i %)) (range (call kir 'topic-count "naic" i)))) [0 1])))
    (is (= "naic.model-laws-program" (present (call kir 'by-topic-id "naic" "governance" 0))))
    (is (= "naic.unfair-trade-practices-act" (present (call kir 'by-topic-id "naic" "consumer-protection" 0))))
    (is (= #{} (set (:effects kir))))
    (testing "fail closed"
      (is (zero? (call kir 'entry-count "national-association-of-insurance-commissioners")))
      (is (zero? (call kir 'entry-count "finra")))
      (is (nil? (present (call kir 'entry-field "naic" 2 "id"))))
      (is (nil? (present (call kir 'entry-field "naic" 0 "established-date"))))
      (is (nil? (present (call kir 'topic "naic" 0 1))))
      (is (zero? (call kir 'by-topic-count "naic" "labor")))
      (is (nil? (present (call kir 'by-topic-id "naic" "governance" 1)))))))
(defn compiler-root [] (nth (iterate #(.getParent ^java.nio.file.Path %)
  (java.nio.file.Path/of (.toURI (io/resource "kotoba/compiler/core.clj")))) 4))
(defn base64 [x] (.encodeToString (java.util.Base64/getEncoder) x))
(deftest restricted-js-and-wasm-conform-semantically
  (let [js (compiler/compile-source source :js-kotoba-v1) wasm (compiler/compile-source source :wasm32-browser-kotoba-v1)
        js64 (base64 (.getBytes ^String (:source js) "UTF-8")) wasm64 (base64 ^bytes (:bytes wasm))
        p (shell/sh "node" "--input-type=module" "-e"
            (str "import(process.argv[1]).then(async h=>{const j=await import('data:text/javascript;base64," js64 "');const w=await h.instantiateKotoba(Buffer.from(process.argv[2],'base64'));const r=x=>{if(x['entry-field']('naic',0n,'established-date')[1]!==false||x['entry-field']('naic',0n,'last-revised-date')[1]!==false||x['entry-field']('naic',1n,'established-date')[1]!==false||x['entry-field']('naic',1n,'last-revised-date')[2]!=='2024 (Spring 2024 edition)')throw Error('dates');if(x['topic-count']('naic',0n)!==1n||x['by-topic-id']('naic','consumer-protection',0n)[2]!=='naic.unfair-trade-practices-act'||x['entry-count']('national-association-of-insurance-commissioners')!==0n)throw Error('authority');};r(j.instantiateKotoba({}));r(w.instance.exports)}).catch(e=>{console.error(e);process.exit(99)})")
            (.toString (.toUri (.resolve (compiler-root) "runtime/browser-host.mjs"))) wasm64)]
    (is (zero? (:exit p)) (str (:out p) (:err p)))))
(deftest production-source-authority
  (is (= ["src/association_facts.kotoba"] (->> (file-seq (io/file "src")) (filter #(.isFile %)) (map str) sort vec))))
