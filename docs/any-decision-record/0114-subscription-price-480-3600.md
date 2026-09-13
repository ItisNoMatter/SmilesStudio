# 0114: サブスクリプション価格を月額480円・年額3,600円に決定する

- Date: 2026-09-13
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

AnyDR 0113で、価格帯はAnyDR 0036の参考値（月額300〜500円、年額2,000〜3,000円程度）の
範囲内で実装時に確定すると決めていた。具体的な金額をこのグリルセッション内で決める必要が
あった。

## Decision

月額480円・年額3,600円（月換算300円、年払いで約37%引き）に決定する。

## Alternatives

特に比較検討した別案はない。年払いで割安に見せる一般的な価格構成の参考値をそのまま
採用した。

## Consequences

- Issue #17のRevenueCat実装で、この2つの価格を持つサブスクリプション商品を設定する。

## Related

- [0113-price-defaults-to-anydr-0036-reference](./0113-price-defaults-to-anydr-0036-reference.md)
- [0036-plan-b-c-monetization-supersedes-byok-hybrid](./0036-plan-b-c-monetization-supersedes-byok-hybrid.md)
