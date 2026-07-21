# ADR 0001: Kotoba is the NAIC catalog source authority

- Status: Accepted
- Date: 2026-07-21

`src/association_facts.kotoba` is the sole production source. It preserves NAIC's
standard-setting/model-law distinction, both absent establishment dates, the
first absent revision date, the literal `2024 (Spring 2024 edition)` second
revision value, official citations, and asymmetric ordered topic sets. Unknown
associations, aliases, fields, topics, and indexes fail closed; no effects are
declared.

Conformance is observable semantics across the reference evaluator, restricted
JavaScript, and instantiated typed WebAssembly, including typed ABI, bounds,
effects, and rejection behavior. Compiler-output byte identity is not a language
gate. Clojure and the JVM are compiler/test hosts only.
