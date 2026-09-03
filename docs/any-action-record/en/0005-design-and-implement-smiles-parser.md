# 0005: Designed and implemented SmilesParser.parse() with a chain+branch scope via a separate tokenizer

- Date: 2026-09-01
- Related AnyDR: 0012, 0013, 0014, 0015, 0016

## Objective

Implement real SMILES parsing logic in `SmilesParser.parse()`, which had been a dummy
implementation up to this point.

## Action

Used `/grill-with-docs` to settle the first TDD iteration's scope (chains and branches only —
ring closures and aromatics deferred to a later stage) and internal architecture (a two-stage
"string → Tokenizer → recursive-descent parse" design; the user chose the separate-tokenizer
approach over the single-pass recursive descent that Claude Code had recommended) — recorded as
AnyDR 0012–0016. Then implemented the Tokenizer and SmilesParser via TDD.

## Result

The parser can now handle chain+branch SMILES such as `CCO` (ethanol) and `CC(=O)O` (acetic acid).
Implementation completed and tests green at `60b5e09`.

## Reflections

Instead of the "single-pass recursive descent" Claude Code recommended for its smaller
implementation footprint, the user chose the separate-tokenizer design for easier future extension
(new token types can cover ring closures and aromatics later). It was a bet on lower future
extension cost over lower upfront implementation cost — and it paid off exactly as expected: the
following ring-closure and aromatic-notation issues were both implemented by adding new token
types, with no rework of the existing design.
