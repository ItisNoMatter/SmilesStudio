# 0115: 認識機能利用のたびに残り回数を表示し、使い切ったらペイウォールを表示する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #17（RevenueCat SDK導入と有料プラン実装）で、AnyDR 0036に参考値のなかった
「アップセルのタイミング・見せ方」を検討した。無料枠（AnyDR 0112: 月5回）をいつ・どう
ユーザーに知らせるかが論点だった。

## Decision

画像認識機能を使うたびに残り回数を表示し、無料枠を使い切ったタイミングでペイウォールを
表示する。

## Alternatives

- 無料枠を使い切った瞬間に初めてペイウォールを表示する（残り回数の事前表示なし）:
  実装はシンプルだが、ユーザーが心づもりなく突然制限される体験になり、AnyDR 0036の
  「ダークパターンは不採用」という方針と相性が悪いため不採用とした。

## Consequences

- 残り回数を表示するUI（Snackbarへの追記等）の実装が追加で必要になる。
- 透明性の高い、ダークパターン不採用の方針と一貫した体験になる。

## Related

- [0036-plan-b-c-monetization-supersedes-byok-hybrid](./0036-plan-b-c-monetization-supersedes-byok-hybrid.md)
- [0112-free-tier-monthly-count-five](./0112-free-tier-monthly-count-five.md)
