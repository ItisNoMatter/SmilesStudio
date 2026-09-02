# 0035: レンダリングパイプライン（Issue #1の#5〜#7）をIssue #13より先に着手する

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

`/grill-with-docs`によるグリリングセッションで[Issue #13](https://github.com/ItisNoMatter/SmilesStudio/issues/13)（ui-composeのモバイル向け調整）の実質的なスコープを検討した。`MoleculeCanvas`（`ui-compose`）は現時点で中身のない空のCanvasのままで、実際の構造式描画ロジックはまだ存在しない（[[0019]]のレイアウト計算・[[0020]]のKekulize変換に対応するIssue #1の#5・#6・#7がいずれも未着手）。[[0034]]でIssue #11（Shipaton対応）をIssue #1（デスクトップv1）より優先することを決めたが、Shipatonの本丸機能である手描き構造式認識UI（Issue #15）は結局`MoleculeCanvas`の実描画（Issue #1の#7）に依存しており、「モバイル向け調整」を実質的に検証するにも描画ロジックが必要という状態だった。

## Decision

Issue #1の#5（2Dレイアウト計算）・#6（芳香族結合のKekulize変換）・#7（MoleculeCanvasの描画実装）を、Issue #13（ui-composeのモバイル向け調整）より先に着手する。[[0034]]の「Issue #11優先」という大方針自体は維持しつつ、Shipatonのクリティカルパス上で実際に必要な作業として例外的にIssue #1側のこれら3件を先に片付ける。

## Alternatives

- レスポンシブな土台のみ整備し実描画への適応は後回しにする: `MoleculeCanvas`が空の現状に即しスコープが明確という利点があったが、結局近いうちに#5〜#7へ戻る必要があり二度手間になること、モバイルでの実際の見え方をこの状態では検証できないことから不採用。
- Issue #13の中で必要な範囲の描画・入力UIを先に実装してしまう: Shipatonの最短経路を前倒しできる利点があったが、Issue #13という1つのIssueに本来Issue #1側の複数Issueの内容が混ざり込みWayfinderの「1 Issue = 1タスク」という粒度の前提が崩れ、進捗管理が二重帳簿的になるため不採用。

## Consequences

- Wayfinderの1 Issue = 1タスクという粒度を保ったまま、必要な順序（レイアウト計算→Kekulize変換→描画実装→モバイル調整）で進められる。
- 表面上は「Issue #1に戻る」ため[[0034]]の文言と矛盾して見えるが、実際にはShipatonのクリティカルパス実現のための例外である。マップIssue #1・#11双方にこの経緯を明記する。
- 次の着手順は Issue #5 → #6 → #7 → （Issue #13に戻る）。Issue #14（Koog SDK導入）は依存がないため、この順序と並行して着手可能。

## Related

- [0019-fixed-angle-2d-layout-algorithm](./0019-fixed-angle-2d-layout-algorithm.md)
- [0020-kekule-style-aromatic-bond-rendering](./0020-kekule-style-aromatic-bond-rendering.md)
- [0034-prioritize-shipaton-map-over-desktop-v1](./0034-prioritize-shipaton-map-over-desktop-v1.md)
