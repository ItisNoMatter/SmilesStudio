# 0119: 「BYOKを使う」「購読する」の導線は既存のAPIキー設定ダイアログに統合する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #17のスコープにある「設定画面で『BYOKを使う』か『アプリのAI機能を購読する』かを
選べる導線」を、既存のUIのどこに置くか検討した。

## Decision

既存の「APIキー設定」ダイアログ（Issue #16）を拡張し、「BYOKを使う」（既存のAPIキー入力）
と「購読する」（RevenueCat Paywallを開くボタン、AnyDR 0118）を1つの画面にまとめる。

## Alternatives

- 新しく独立した「プラン」メニュー項目を追加する: 役割ごとに画面が独立する利点はあるが、
  「APIキー設定」と「プラン」のどちらを開けばいいかユーザーが迷う可能性があり、BYOKと購読は
  「AI機能をどう使うか」という同じ問いへの2つの答えであるため、1つの入口にまとめる方が
  自然と判断し不採用とした。

## Consequences

- `ApiKeySettingsDialog`に購読導線（Paywallを開くボタン、現在の購読状態表示）を追加する
  実装が必要になる。
- 新しいメニュー項目・画面遷移は不要。

## Related

- [0118-paywall-uses-revenuecat-paywalls](./0118-paywall-uses-revenuecat-paywalls.md)
- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
