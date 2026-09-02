# 0034: Issue #11（Shipaton 2026対応）の子Issueを優先する

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user

## Context

[[0033]]により、デスクトップv1ロードマップ（[Issue #1](https://github.com/ItisNoMatter/SmilesStudio/issues/1)）とShipaton 2026対応ロードマップ（[Issue #11](https://github.com/ItisNoMatter/SmilesStudio/issues/11)）という2つの独立したWayfinderマップが並行稼働することになった。両マップともフロンティア（依存なしで着手可能なIssue）が存在する状態（Issue #1側は#5「2Dレイアウト計算」、Issue #11側は#12「android-appモジュールの追加」・#14「Koog SDK導入とVision LLM呼び出し」）で、次にどちらから着手するかを決める必要があった。

## Decision

今後はIssue #11（Shipaton 2026対応）の子Issueを優先して着手する。デスクトップv1ロードマップ（Issue #1）の残タスク（#5〜#10）は、Issue #11側の作業が一段落するまで一時停止する。

## Alternatives

本文中に明示的な代替案の比較記述はなし。デスクトップv1ロードマップを先に完了させてからShipaton対応に着手する順序も暗黙の選択肢としてあったが、採用されなかった。

## Consequences

- Shipaton 2026の締切（2026-09-30、Play Store申請目標2026-09-20）を踏まえると、この優先順位はプロジェクトメモリ`project_shipaton_2026_hackathon`に記録済みのユーザーの優先順位（Koog MVP→Android対応→課金→ストア申請→Devpost提出）と整合する。
- デスクトップv1ロードマップ（Issue #1）の#5以降は着手が遅れる。ただしIssue #11の#15（手描き構造式認識UI）はIssue #1の#7（MoleculeCanvas描画実装）にクロスマップで`blocked_by`しているため、いずれ#5〜#7ラインへの着手が必要になる。
- 次の着手先はIssue #11のフロンティア（#12「android-appモジュールの追加」または#14「Koog SDK導入とVision LLM呼び出し」）のいずれかとなる。

## Related

- [0033-separate-wayfinder-map-for-shipaton](./0033-separate-wayfinder-map-for-shipaton.md)
