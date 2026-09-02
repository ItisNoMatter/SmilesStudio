# 0028: Implement the hand-drawn structure recognition MVP via a Koog-mediated vision LLM

- Date: 2026-09-02
- Status: Accepted
- Category: architecture
- Deciders: the user

## Context

As a new feature for Shipaton 2026 ([[0027]]), we planned a feature that sends a hand-drawn chemical
structure image through JetBrains Koog to a vision-capable LLM, converts it to a SMILES string, and
displays it again as a structure diagram using the existing `SmilesParser`/`Molecule`/rendering
pipeline. CLAUDE.md had previously described Koog integration only as a distant "planned for the
future" concept; this AnyDR promotes it to the hackathon's MVP scope.

## Decision

The MVP scope is limited to "one image → one SMILES candidate → re-rendered structure." Presenting
multiple candidates or a confidence score is out of scope for now. The SMILES string returned by the
LLM is reflected directly into the existing SMILES text input field ([[0017]]) so the user can review
and correct it (no new graphical editing UI is added). As a fallback for failed or incorrect
recognition, a path for typing the SMILES string directly by hand must always remain available.

## Alternatives

- Presenting multiple candidates / a confidence score: useful for showing the user how confident the
  recognition is, but explicitly deferred for now as out of scope for the MVP.

## Consequences

- This design simply reuses the one-way pipeline decided in [[0017]] ("text editing + read-only
  structure rendering"), and does not affect Approach B (a graphical structure editor), which was
  rejected there. The image-recognition result only ever passes through the text field — direct
  editing on the canvas is still not being introduced.
- It also does not affect [[0009]] (deferring implementation of a canonical SMILES writer), since this
  feature only needs SMILES parsing (text → Molecule), not the reverse serialization
  (Molecule → SMILES).
- The UI itself will mainly live in `android-app`/`ui-compose`, but the core parsing/rendering logic
  in `core-smiles` stays platform-independent and needs no changes.

## Related

- [0009-defer-canonical-smiles-writer](./0009-defer-canonical-smiles-writer.md)
- [0017-v1-text-input-readonly-rendering-scope](./0017-v1-text-input-readonly-rendering-scope.md)
- [0027-android-app-module-for-shipaton-2026](./0027-android-app-module-for-shipaton-2026.md)
