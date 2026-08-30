# 0009: Molecule→SMILES文字列への逆変換（canonical writer）を当面スコープ外とする

- Date: 2026-08-31
- Status: Accepted
- Category: rejected
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるcore-smilesドメインモデルのグリリングセッション（Q6）で、
`SmilesParser`が現在パース方向（SMILES文字列→`Molecule`）のみを扱っており、正規化された
SMILES文字列を書き出す機能（canonical SMILES writer、`Molecule`→SMILES文字列）を
`core-smiles`の責務に含めるかどうかが議題になった。

## Decision
`Molecule`→SMILES文字列への逆変換（canonical SMILES writer）は当面`core-smiles`の
スコープ外とする。

## Alternatives
- **スコープに含める** — 逆変換機能を今のうちに設計・実装する案。`core-smiles`の
  現在のユースケースは「SMILES文字列→`Molecule`」のパース方向のみであり、将来の
  Koog連携（手描き構造式画像からのマルチモーダル認識）のゴールも「画像→SMILES文字列」
  であって、`Molecule`→SMILES文字列への変換は今のユースケースに存在しないため不採用。

## Consequences
原子の走査順（traversal order）や正規化順序といった、逆変換に付随する設計判断は今は
保留する。将来、実際に逆変換の要件（例えばエディタでの構造編集結果をSMILES文字列として
書き出す機能）が出てきた時点で、あらためて複数案を比較して設計し直す。

## Related
- [0006-ring-as-derived-domain-term](./0006-ring-as-derived-domain-term.md)
