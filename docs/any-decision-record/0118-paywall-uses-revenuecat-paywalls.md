# 0118: ペイウォールはRevenueCatのPaywalls機能を利用する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #17で、無料枠を使い切った時・設定画面から購読する時に表示するペイウォールの画面構成を
検討した。

## Decision

RevenueCatが提供する`Paywalls`（リモート設定可能な専用ペイウォールUI）を利用する。
既存の`AlertDialog`パターンを踏襲した自前の簡易ダイアログは採用しない。

## Alternatives

- 既存の`AlertDialog`パターンを踏襲した簡易ダイアログ: `ApiKeySettingsDialog`と同じ実装
  パターンを踏襲でき実装コストは小さいが、価格比較や機能訴求を十分に見せるには手狭で、
  購読を後押しする画面としての訴求力に欠けるため不採用とした。

## Consequences

- RevenueCatダッシュボード側でのペイウォール作成・設定が必要になる。
- デザイン変更をアプリのコード変更なしでリモート設定できるようになる。

## Related

- [0115-show-remaining-count-transparently](./0115-show-remaining-count-transparently.md)
