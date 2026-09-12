# 0100: APIキー削除ボタンは確認ステップなしで即座に削除する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0099]]で追加を決めた「削除」ボタンの挙動として、誤タップ防止の確認ステップを挟むかどうかを
検討した。

## Decision

削除ボタンは確認ステップなしで即座に削除する。

## Alternatives

- 確認ダイアログ（「本当に削除しますか？」）を挟む: 誤タップによる意図しない削除を防げるが、
  APIキーの削除は再入力すれば復旧できる程度の操作であり、Play Consoleのロールアウトのような
  不可逆・公開系の操作と比べて確認ステップを重ねるコストに見合わないと判断し不採用とした。

## Consequences

- ダイアログの実装がシンプルになる。
- 誤タップでキーを失った場合は再入力が必要になる。

## Related

- [0099-byok-add-delete-key-button](./0099-byok-add-delete-key-button.md)
