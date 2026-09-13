# 0116: 無料枠カウンターは暦月リセットとする

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #17のB案（無料枠月5回、AnyDR 0112）における「月あたり」の意味を検討した。

## Decision

暦月リセット（毎月1日にカウンターを0に戻す）を採用する。

## Alternatives

- 初回利用から30日間のローリングウィンドウ: ユーザーごとに公平な猶予期間になる利点は
  あるが、「いつ使ったか」の履歴保持が必要になり実装が複雑化するため、ハッカソンの残り
  期間を踏まえて不採用とした。

## Consequences

- 現在の年月とカウンターの記録年月を比較するだけのシンプルな実装で済む。
- 月末に使い始めたユーザーは実質数日しか無料枠を使えないが許容する。

## Related

- [0112-free-tier-monthly-count-five](./0112-free-tier-monthly-count-five.md)
