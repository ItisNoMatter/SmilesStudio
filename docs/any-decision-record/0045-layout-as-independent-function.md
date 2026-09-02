# 0045: 2Dレイアウトは`Molecule`のプロパティではなく独立した関数として実装する

- Date: 2026-09-03
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0019]]で確定した固定角度配置アルゴリズムを、[GitHub Issue #5](https://github.com/ItisNoMatter/SmilesStudio/issues/5)として実装するにあたり、計算結果（各原子の2D座標）をどう返すかを決める必要があった。既存の`Molecule.rings`（[[0026]]）は`by lazy`のMoleculeプロパティとして実装されており、同じパターンを踏襲するか、独立した関数として切り出すかが論点だった。

## Decision

2Dレイアウトは`Molecule`のプロパティではなく、独立した関数（`computeLayout(molecule: Molecule): Map<AtomId, Point2D>`の形を想定）として実装する。

## Alternatives

- `Molecule.layout`のような`by lazy`プロパティとして`Molecule`に追加する: 既存の`Molecule.rings`と一貫したアクセスパターンになり、キャッシュの恩恵も自動的に得られるという利点があったが、レイアウトは「描画のための一つの解釈」であり同じ分子でも別のレイアウトアルゴリズムがありえる点で、分子構造そのものから導出される`Ring`とは性質が異なると判断し不採用。`Molecule`を化学的構造のみに専念させる方を優先した。

## Consequences

- `Molecule`は化学的構造（原子・結合・そこから導出されるRing/芳香族性）のみに専念し、描画用の付随情報であるレイアウトの計算責務を持たない。
- 将来別のレイアウト戦略を試したくなった場合も、関数を差し替える・並行して用意するだけで済む。
- キャッシュが必要な場合は呼び出し側（`ui-compose`等）で管理する必要がある。v1スコープでは都度計算で問題ない見込み。

## Related

- [0019-fixed-angle-2d-layout-algorithm](./0019-fixed-angle-2d-layout-algorithm.md)
- [0026-ring-value-type-for-ring-detection](./0026-ring-value-type-for-ring-detection.md)
