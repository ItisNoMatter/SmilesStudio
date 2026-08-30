# 0010: テストメソッド名は日本語で統一する

- Date: 2026-08-31
- Status: Accepted
- Category: naming
- Deciders: the user

## Context
AnyDR 0003（`Atom.hydrogenCount`のsealed interface化）をTDDで反映する作業中、
`MoleculeTest.kt`に新しいテストケースをKotlinのバッククォート記法・英語で追記した
（例: `` `explicit hydrogen count carries the count specified in bracket notation` ``）。
この場面でユーザーから、テストメソッド名は日本語で統一する方針にしたいと明示的な指示があった。

## Decision
Kotlinのバッククォート記法によるテスト関数名は、英語ではなく日本語で記述して統一する。

## Alternatives
本決定は複数案の比較検討を経たものではなく、ユーザーが直接の方針として決定した。

## Consequences
`MoleculeTest.kt`に既存する英語のテストメソッド名（例:
`` `molecule holds atoms and bonds by AtomId reference` ``）は日本語に書き換える必要がある。
今後`core-smiles`・`ui-compose`を含むプロジェクト全体で新規に書くテストメソッド名も
日本語で統一する。
