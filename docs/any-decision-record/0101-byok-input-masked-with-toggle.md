# 0101: APIキー入力欄はマスク表示＋表示切り替えアイコンにする

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

BYOK設定ダイアログのAPIキー入力欄で、入力文字をパスワードのように隠すかどうかを検討した。

## Decision

`PasswordVisualTransformation`相当のマスク表示にし、表示/非表示を切り替えるアイコンを添える。

## Alternatives

- 常に平文表示: 実装が最もシンプルだが、画面を覗かれた場合にAPIキーがそのまま見えてしまう
  リスクがあり、[[0092]]でAndroid Keystoreによる暗号化保存を選んだ方針と一貫性が取れないため
  不採用とした。

## Consequences

- 表示切り替え状態の管理・アイコンボタンの実装が必要になる。
- 入力ミスの確認は表示切り替えアイコンをタップすれば可能。

## Related

- [0092-api-key-storage-android-keystore](./0092-api-key-storage-android-keystore.md)
- [0097-byok-settings-as-alert-dialog](./0097-byok-settings-as-alert-dialog.md)
