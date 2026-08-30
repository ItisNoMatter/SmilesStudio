# 0004: 芳香族性をAtomのフィールドではなくBondから導出する計算プロパティにする

- Date: 2026-08-29
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるcore-smilesドメインモデルのグリリングセッション（Q2）で、
`Atom.isAromatic: Boolean`（0001で導入）と`BondType.AROMATIC`が互いに独立して存在し、
両者の整合性を強制する仕組みがないという課題が見つかった。例えば`isAromatic=true`の原子に
`SINGLE`結合しか繋がっていない、という矛盾した`Molecule`も型上は構築できてしまう。
CLAUDE.mdの複数案提示ルールに従い、Claude Codeが3案を提示し、ユーザーが選択した。

## Decision
`Atom.isAromatic`フィールドを廃止し、芳香族性は「原子に隣接する`Bond`がすべて
`BondType.AROMATIC`かどうか」から導出する計算プロパティとして`Molecule`側に持たせる
（グリリングQ2のアプローチCを採用）。`Atom`単体では芳香族性を判定できず、`Molecule`
コンテキスト（隣接`Bond`情報）が必要になる。

## Alternatives
- **アプローチA: 今は不変条件を無視して現状維持** — `Atom.isAromatic`と
  `BondType.AROMATIC`をそのまま独立して持ち、整合性チェックを行わない。実装コストは
  ゼロだが、型システムが矛盾した`Molecule`の構築を防げないままであり、将来見えないバグの
  温床になりうるため不採用。
- **アプローチB: `Molecule`に`validate()`検証関数を追加** — 既存のデータ構造を変更せず、
  明示的な検証関数で矛盾を検出する方式。データ構造を変えずに済むが、検証呼び出しを忘れれば
  意味がなく強制力が弱い（コンパイル時ではなく任意の実行時チェック）ため不採用。

## Consequences
芳香族性の判定には`Molecule`全体（隣接`Bond`）の情報が必要になり、`Atom`単体では判定
できなくなる。判定のたびにグラフ走査が発生しうるため、パフォーマンス上のキャッシュが
必要になる見込みだが、その具体的な実装方法（`lazy`によるMolecule内キャッシュ／生成時
eager計算／呼び出し側委譲）は本決定の時点ではまだ確定しておらず、別途決定する。既存の
`Atom.isAromatic`を参照する箇所（現状はテストのみ）は本変更に伴い書き換えが必要。

## Related
- [0001-core-smiles-id-based-domain-model](./0001-core-smiles-id-based-domain-model.md)
- [0003-atom-hydrogen-count-sealed-interface](./0003-atom-hydrogen-count-sealed-interface.md)
