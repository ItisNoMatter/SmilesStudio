# 0065: Kekulize変換は`computeLayout`と同じ独立関数パターンで実装する

- Date: 2026-09-04
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[Issue #6](https://github.com/ItisNoMatter/SmilesStudio/issues/6)（芳香族結合のKekulize変換）の実装に着手するにあたり、CLAUDE.mdの方針に従い実装前に選択肢を提示した。`BondType.AROMATIC`の結合を単結合・二重結合の交互パターン（Kekulé構造、[AnyDR 0020](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0020-kekule-style-aromatic-bond-rendering.md)）に変換した結果をどう表現するかが論点だった。

## Decision

Kekulize変換は`Molecule`から独立した関数として実装する: `fun kekulize(molecule: Molecule): List<Bond>`。芳香族結合をSINGLE/DOUBLEに解決した新しいBondリストを返し、非芳香族結合はそのまま含める。`Molecule`自体は変更しない。[AnyDR 0045](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0045-layout-as-independent-function.md)の`computeLayout(molecule): Map<AtomId, Point2D>`と同じ「独立関数」パターンを踏襲する。

## Alternatives

- `Map<Bond, BondType>`を返す（芳香族結合のみ）: 「芳香族結合だけを解決する」という意図が明確になる利点があったが、`Bond`はdata classでありキーの衝突リスクが理論上あること、呼び出し側で毎回「芳香族でなければ元のtypeを使う」というフォールバック処理を書く必要があることから不採用。
- `Molecule`自体を書き換える（AROMATIC結合を直接SINGLE/DOUBLEに置換した新しい`Molecule`を返す）: 呼び出し側が`molecule.bonds`をそのまま使えるという利点があったが、[AnyDR 0020](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0020-kekule-style-aromatic-bond-rendering.md)が「Kekulé表現は描画上の近似であり、化学的な芳香族性（[AnyDR 0004](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0004-derive-aromaticity-from-bonds.md)のSSOT）とは別物」と明記しているため、この案は検討の対象から除外した。`Molecule`を書き換えると`isAromatic()`等の既存ロジックがKekulize後には機能しなくなる懸念があり、化学的な芳香族性と描画上の近似という2つの異なる概念が1つのデータ構造に混在してしまう。

## Consequences

- `MoleculeCanvas`（Issue #7）は`kekulize(molecule)`の返り値をそのままイテレートして描画すればよく、特別なフォールバック処理が不要になる。
- `Molecule.isAromatic()`等、既存のAROMATIC SSOTロジックには一切影響しない。
- v1スコープ（単純な偶数員芳香環）を前提としたアルゴリズムになるため、縮合環など複雑な構造への対応が必要になった場合は本AnyDRを再訪する必要がある（AnyDR 0020のConsequencesと同様）。

## Related

- [0004-derive-aromaticity-from-bonds](./0004-derive-aromaticity-from-bonds.md)
- [0020-kekule-style-aromatic-bond-rendering](./0020-kekule-style-aromatic-bond-rendering.md)
- [0045-layout-as-independent-function](./0045-layout-as-independent-function.md)
