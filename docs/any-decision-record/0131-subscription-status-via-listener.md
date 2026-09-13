# 0131: 購読状態はRevenueCatのリスナーでリアルタイムに監視する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #35で、購読状態（エンタイトルメント`pro`の有無）をアプリ側でどう取得・監視するか
検討した。

## Decision

RevenueCatのリスナー（`addCustomerInfoUpdateListener`）を登録し、購読状態の変化をリアル
タイムでアプリの状態に反映する。

## Alternatives

- 必要なタイミングで都度`getCustomerInfo()`を呼んで確認する: 実装がシンプルという利点は
  あるが、購読直後にキャッシュが古いままの可能性がありリアルタイム性に欠けるため不採用と
  した。

## Consequences

- リスナーの登録・解除というライフサイクル管理が必要になる。
- 購読直後（Paywallを閉じた直後）に即座にアプリ側の状態が更新される。

## Related

- [0130-anonymous-auth-at-app-launch](./0130-anonymous-auth-at-app-launch.md)
- [0118-paywall-uses-revenuecat-paywalls](./0118-paywall-uses-revenuecat-paywalls.md)
