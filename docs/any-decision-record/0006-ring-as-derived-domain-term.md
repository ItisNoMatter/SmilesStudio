# 0006: Ringをderived（導出）ドメイン用語として定義する

- Date: 2026-08-29
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるcore-smilesドメインモデルのグリリングセッション（Q3）で、
SMILESの環閉包（例: `C1CCCCC1`）が現在のモデルでは`Bond`のリストの中に「たまたま閉路を
作る辺がある」という形でしか存在せず、`Molecule`上に`Ring`という概念が明示的に無いことが
議題になった。`Ring`にドメイン用語としての名前を与えるべきかを問うた。

## Decision
`Ring`をドメイン用語として`CONTEXT.md`に定義する。ただし`Molecule`の保存フィールドとしては
持たせず、原子と結合のグラフから必要に応じて導出するクエリ時の概念とする
（0004でのAromatic Atomの扱いと同じ「導出概念」パターン）。

## Alternatives
- **名前を与えず現状維持** — `Ring`という用語を定義せず、環閉包を`Bond`リストの中の
  暗黙的な閉路としてのみ扱う。用語が無いと、将来の環検証やcanonical SMILES生成の議論を
  する際に共通言語が無く、都度説明し直すことになるため不採用。

## Consequences
現時点では`Ring`の具体的なデータ構造・環検出アルゴリズム（SSSR等）は実装しない。用語の
定義のみを`CONTEXT.md`に先に置き、実際に環検証やcanonical SMILES生成など`Ring`を必要と
する機能が要件として出てきた時点で、0004/0005の芳香族性と同様に「導出プロパティ＋
`by lazy`キャッシュ」パターンを検討する。

## Related
- [0004-derive-aromaticity-from-bonds](./0004-derive-aromaticity-from-bonds.md)
- [0001-core-smiles-id-based-domain-model](./0001-core-smiles-id-based-domain-model.md)
