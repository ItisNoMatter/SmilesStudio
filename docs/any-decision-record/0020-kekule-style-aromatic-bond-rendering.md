# 0020: 芳香族結合の描画はKekulé構造として表現する

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0019]]で2Dレイアウトの方式を固定角度配置アルゴリズムに確定した後、AROMATIC結合（`BondType.AROMATIC`）をどのような線種・図形で描画するかを決める必要があった。化学的な描画方針として、単結合・二重結合の交互パターンで表現するKekulé構造と、環の中心に円を描いて非局在化を表現する方式の2案を比較検討した。

## Decision

芳香族結合の描画は、単結合・二重結合の交互パターン（Kekulé構造）として表現する。環の中心に円を描く非局在化表現は採用しない。

## Alternatives

- 円（非局在化）表現: 環がすべてaromaticな原子で構成されている場合、環の中心に円を描く方式。「非局在化したπ電子系」という`CONTEXT.md`のAromatic Bondの定義と表現が一貫し、Kekulize変換のような複雑な変換ロジックが不要という利点があったが、ユーザーはKekulé構造（アプローチA）を選択したため不採用。

## Consequences

- AROMATIC結合の並びをSINGLE/DOUBLE交互パターンに変換する「Kekulize変換」ロジックが新たに必要になる。[[0018]]・[[0019]]によりv1スコープは単環・単純な分岐に限定されているため、変換対象は単純な偶数員芳香環（ベンゼンなど）が中心となり、一意に交互パターンを決められる範囲に収まる想定。
- 既存のSINGLE/DOUBLE結合の描画ロジックをそのまま流用でき、新しい描画プリミティブ（円）の追加は不要。
- 「非局在化したπ電子系」というAromatic Bondの定義（`CONTEXT.md`）と、Kekulé表現という描画上の近似との間には概念的な乖離が残る。将来、縮合環など単純な偶数員環に収まらない構造へスコープを広げる際は、Kekulize変換が一意に決まらないケースが生じうるため、本AnyDRを再訪する必要がある。

## Related

- [0018-v1-grammar-scope-rings-and-aromatics](./0018-v1-grammar-scope-rings-and-aromatics.md)
- [0019-fixed-angle-2d-layout-algorithm](./0019-fixed-angle-2d-layout-algorithm.md)
