# 0050: any-action-recordの主目的をAction/Result/Objectiveによる「開発の旅路」の記録と定める

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

`/grill-with-docs`によるグリリングセッションで、AnyDR（Any Decision Record）と対を成す新しい記録媒体`any-action-record`（AnyAR）の設計を検討した。既存の`update-wip`スキルは「セッション再開用のスナップショット」であり、自身のドキュメント内で「詳細な履歴はgit履歴とAnyDRが担う」と明記している。一方、gitのコミットメッセージも実際には「何を・なぜ・結果どうなったか」を書き込んでいる。`any-action-record`がこの2つと具体的にどう異なる立ち位置を狙うのかを確認する必要があった。

## Decision

`any-action-record`の主目的は、BuildInPublicツイート・デモ動画向けの「開発の旅路」のネタ元を、タスク単位（Objective/Action/Result）で振り返りやすい形で残すことと定める。AnyDRが「なぜその決定をしたか」を残す記録媒体であるのに対し、AnyARは「何を目指し、何をして、どうなったか」を残す対概念として位置づける（[[0037]]の三層防御OSS戦略・[[0043]]の「学びを共有するトーン」の狙いと直結する）。

## Alternatives

本文中に明示的な代替案の比較記述はなし。「gitコミット単位に縛られない粗い/細かい活動履歴」という別の狙い（(b)案）も検討したが、ユーザーはBuildInPublic向けの狙い（(a)案）を主目的として選択した。

## Consequences

- `any-action-record`のファイル構造・トリガー方式（作業の節目での自動発火）は、この主目的（BuildInPublicのネタ元）に沿って設計する。
- `update-wip`（セッション再開用スナップショット、都度上書き）とは役割が異なり、AnyDRと同様に1エントリ=1ファイルの追記型（過去のエントリを上書きしない）で設計する見込み。
- `buildinpublic-tweet`Skill（[[0039]]）がAnyDRだけでなくAnyARからもツイート素材を拾うようになる可能性があるが、これは本AnyDRのスコープ外とし、別途検討する。

## Related

- [0037-three-layer-defense-oss-strategy](./0037-three-layer-defense-oss-strategy.md)
- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0043-tweet-hashtags-and-tone](./0043-tweet-hashtags-and-tone.md)
