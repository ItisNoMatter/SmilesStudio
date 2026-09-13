# 0112: 無料枠の月間回数を月5回に決定する

- Date: 2026-09-13
- Status: Accepted
- Category: process
- Deciders: the user

## Context

AnyDR 0055で定めたフィードバック実証型プロセスに従い、B案（回数制フリーミアム）の未確定
パラメータのうち「無料枠の月間回数」について公開の場で問いかけを実施した。X上でのアンケート
機能を使った投稿は票が集まらなかったため、Discordコミュニティにも投げかけたところ、1名から
理由付きの返信があった。

## Decision

無料枠の月間回数を月5回に決定する。

## Alternatives

- 月10回: 検討時の比較対象として挙がったが、Discordでの返信いわく「10回ではユーザーが
  課金したくなる前に満足してしまう」という理由で採用しなかった。投票の集計結果ではなく
  返信欄の理由付きコメントを優先根拠とするというAnyDR 0055の判断ロジックに沿って、この
  理由付けをそのまま採用した。

## Consequences

- Issue #17のB案実装で、月5回のカウンター管理を実装する必要がある。
- 残る未確定パラメータ（価格帯、アップセルのタイミング・見せ方）は本AnyDRの対象外で、
  引き続き未定のまま。

## Related

- [0055-feedback-driven-monetization-poll-process](./0055-feedback-driven-monetization-poll-process.md)
- [0036-plan-b-c-monetization-supersedes-byok-hybrid](./0036-plan-b-c-monetization-supersedes-byok-hybrid.md)
