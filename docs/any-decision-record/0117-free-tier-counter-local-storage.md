# 0117: 無料枠カウンターはローカル保存のみとする

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #17の無料枠カウンター（AnyDR 0112: 月5回、AnyDR 0116: 暦月リセット）をどこに保存
するか検討した。

## Decision

`SharedPreferences`を使ったローカル保存のみとする。`ApiKeyStore`（Issue #16）と同様の
パターンを踏襲する。RevenueCatのSubscriber Attributesによる同期は採用しない。

## Alternatives

- RevenueCatのSubscriber Attributesで同期して保存する: 端末をまたいだ一貫管理・再インストール
  回避の防止という利点はあるが、Subscriber Attributes自体クライアントから書き込み可能な値で
  本来カウンター用途ではなく、バックエンドを持たない本アプリでは真に信頼できるサーバーサイド
  カウント管理がそもそも実現できないため、実装コストに見合わないと判断し不採用とした。

## Consequences

- 新規依存なしで実装できる。
- アプリの再インストールや端末変更でカウンターがリセットされ、無料枠を使い切っても回避
  できてしまう既知の制約が残る（別Issueで追跡）。

## Related

- [0112-free-tier-monthly-count-five](./0112-free-tier-monthly-count-five.md)
- [0116-free-tier-counter-calendar-month-reset](./0116-free-tier-counter-calendar-month-reset.md)
