# 0113: 残る課金パラメータはAnyDR 0055のデフォルト採用フォールバックを使い実装に進む

- Date: 2026-09-13
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

AnyDR 0055のフィードバック実証型プロセス対象3パラメータのうち、無料枠の月間回数は
AnyDR 0112で決着した。残る2つ（価格帯、アップセルのタイミング・見せ方）について、
Play申請目標09-20まで残り7日という状況で、追加のX/Discordでの問いかけを行うかどうかを
検討した。

## Decision

残る2パラメータについて追加の公開問いかけは行わず、AnyDR 0055が用意している
「反応が薄くても締切時点でAnyDR 0036の参考値をデフォルト採用し、実装スケジュールを
人質に取らない」というフォールバックをそのまま適用する。価格帯はAnyDR 0036の参考値
（月額300〜500円、年額2,000〜3,000円程度）を採用する。アップセルのタイミング・見せ方には
AnyDR 0036に具体的な参考値がないため、本グリルセッション内でPros/Cons形式の設計判断として
別途詰める。

## Alternatives

- 残り2パラメータも同様にX/Discordで問いかけてから決める: フィードバック実証型運用の
  一貫性を保てる利点はあるが、1パラメータあたり最短でも半日〜1日かかり、残り7日という
  スケジュールに対するリスクの方が大きいと判断し不採用とした。

## Consequences

- 価格帯は月額300〜500円、年額2,000〜3,000円程度という参考値の範囲内で実装時に確定する。
- アップセルのタイミング・見せ方は、このグリルセッションの後続の質問で設計判断として決める。
- フィードバック実証型のコンテンツ機会を2つ逃すことになる。

## Related

- [0112-free-tier-monthly-count-five](./0112-free-tier-monthly-count-five.md)
- [0055-feedback-driven-monetization-poll-process](./0055-feedback-driven-monetization-poll-process.md)
- [0036-plan-b-c-monetization-supersedes-byok-hybrid](./0036-plan-b-c-monetization-supersedes-byok-hybrid.md)
