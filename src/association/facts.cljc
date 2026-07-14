(ns association.facts
  "Model-law / self-regulatory rule catalog for the National Association
  of Insurance Commissioners (NAIC) -- a 6th industry-association-level
  source (see cloud-itonami-assoc-6419-jpn-zenginkyo, -6512-jpn-sonpo,
  -6612-jpn-jsda, -6419-deu-bankenverband, -6612-usa-finra for the first
  five) per ADR-2607141700 (cloud-itonami-compliance-fact-federation).
  Aligned to ISIC 6512 (non-life/general insurance) -- the SAME ISIC
  code as sonpo (JPN), enabling a direct cross-country insurance-body
  comparison, mirroring the 6419 (zenginkyo/bankenverband) and 6612
  (jsda/finra) pairs already in this family.

  NAIC is NOT a direct self-regulatory organization the way FINRA is --
  it is a non-governmental standard-setting body of state insurance
  commissioners that publishes MODEL laws for individual states to
  adopt (see `:kind :model-law` below, distinct from `:self-regulatory-code`
  used elsewhere in this family). This distinction is preserved rather
  than glossed over.

  Every entry cites an OFFICIAL naic.org / content.naic.org URL -- never
  fabricated. A rule not in this table has NO spec-basis, full stop;
  extend `catalog`, do not invent an id/url. The Model Laws - About page
  was directly WebFetch-verified; the Unfair Trade Practices Act (Model
  #880) PDF was verified by directly reading its cover/TOC page text via
  the Read tool (same strictest-tier verification as sonpo/bankenverband).")

(def catalog
  "assoc-slug -> vector of entries."
  {"naic"
   [{:association-rule/id "naic.model-laws-program"
     :association-rule/title "Model Laws - About"
     :association-rule/association "naic"
     :association-rule/isic "6512"
     :association-rule/country "USA"
     :association-rule/kind :governance-program
     :association-rule/url "https://content.naic.org/model-laws/about"
     :association-rule/url-provenance :official-association-site
     :association-rule/retrieved-at "2026-07-14"
     :association-rule/topic #{:governance}}
    {:association-rule/id "naic.unfair-trade-practices-act"
     :association-rule/title "Unfair Trade Practices Act (NAIC Model Law #880)"
     :association-rule/association "naic"
     :association-rule/isic "6512"
     :association-rule/country "USA"
     :association-rule/kind :model-law
     :association-rule/url "https://content.naic.org/sites/default/files/model-law-880.pdf"
     :association-rule/url-provenance :official-association-site
     :association-rule/last-revised-date "2024 (Spring 2024 edition)"
     :association-rule/retrieved-at "2026-07-14"
     :association-rule/topic #{:consumer-protection :fair-competition}}]})

(defn spec-basis [assoc-slug] (get catalog assoc-slug))

(defn coverage
  ([] (coverage (keys catalog)))
  ([slugs]
   (let [have (filter catalog slugs)
         missing (remove catalog slugs)]
     {:requested (count slugs)
      :covered (count have)
      :covered-associations (vec (sort have))
      :missing-associations (vec (sort missing))
      :note (str "cloud-itonami-assoc-6512-usa-naic Wave 0 (ADR-2607141700): "
                 (count (get catalog "naic")) " naic entries seeded with an "
                 "official naic.org/content.naic.org citation. Extend "
                 "`association.facts/catalog`, never fabricate an id/url.")})))

(defn by-topic [assoc-slug topic]
  (filterv #(contains? (:association-rule/topic %) topic) (spec-basis assoc-slug)))
