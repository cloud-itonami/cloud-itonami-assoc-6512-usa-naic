# cloud-itonami-assoc-6512-usa-naic

Model-law / self-regulatory rule catalog for the **National Association
of Insurance Commissioners** (NAIC) — a 6th industry-association-level
source alongside
[`cloud-itonami-assoc-6419-jpn-zenginkyo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-jpn-zenginkyo),
[`cloud-itonami-assoc-6512-jpn-sonpo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6512-jpn-sonpo),
[`cloud-itonami-assoc-6612-jpn-jsda`](https://github.com/cloud-itonami/cloud-itonami-assoc-6612-jpn-jsda),
[`cloud-itonami-assoc-6419-deu-bankenverband`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-deu-bankenverband),
and
[`cloud-itonami-assoc-6612-usa-finra`](https://github.com/cloud-itonami/cloud-itonami-assoc-6612-usa-finra).
Part of the [`cloud-itonami`](https://github.com/cloud-itonami)
compliance-fact family (ADR-2607141700,
`cloud-itonami-compliance-fact-federation`, in `com-junkawasaki/root`).

Aligned to **ISIC 6512** (non-life/general insurance) — the SAME code
as sonpo (JPN), enabling a direct cross-country insurance-body
comparison via the federation query.

## A note on NAIC's nature

Unlike zenginkyo/sonpo/jsda/bankenverband/FINRA, **NAIC is not itself a
direct self-regulatory organization**. It is a non-governmental
standard-setting body governed by state insurance commissioners that
publishes **model laws** for individual US states to voluntarily adopt.
This distinction is preserved in the data (`:association-rule/kind
:model-law` vs `:self-regulatory-code`), not glossed over.

## Scope

A **read-only reference/archive** catalog — not an Advisor⊣Governor
actuation actor. It proposes or executes nothing on NAIC's behalf.

Coverage is reported honestly by the fail-closed exported Kotoba ABI: an
association not explicitly admitted has **no spec-basis**, full stop — never
fabricate one.

## Data

- `src/association_facts.kotoba` — the sole production catalog authority.
- `schema/association-rule.edn` — DataScript schema.
- `data/datascript-tx.edn` — derived DataScript tx-data (query this
  alongside other `cloud-itonami`/`etzhayyim` compliance-fact sources via
  `com-junkawasaki/root`'s `scripts/compliance-fact-query.cljs`).

The "Model Laws - About" page was directly WebFetch-verified; the
Unfair Trade Practices Act (NAIC Model Law #880, Spring 2024 edition)
PDF was verified by directly reading its cover/table-of-contents page
text via the Read tool.

The catalog compiles through `kotoba-lang/compiler` to the reference evaluator,
restricted JavaScript, and typed WebAssembly. Clojure/JVM and Node are test and
compiler hosts only; neither is production authority. Compatibility is checked
by observable values, typed ABI, empty effects, bounds, and fail-closed
rejections—not compiler-output byte identity.

## License

AGPL-3.0-or-later (matches the `cloud-itonami-iso3166-*` /
`-municipality-*` / `-assoc-*` / `-lei-*` convention). Document text
itself remains NAIC's; this repo stores only citation metadata
(id/title/url/dates), not full text.
