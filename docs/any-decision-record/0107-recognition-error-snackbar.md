# 0107: 画像認識エラーはSnackbarで表示する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

画像認識失敗時（ネットワークエラー・レート制限・[[0105]]で決めたAPIキー未設定等）のエラー
表示をどこにどう出すかを検討した。既存の`HomeContent.kt`にはSMILESパースエラー用の
`supportingText`（テキストフィールド直下）があった。

## Decision

`Snackbar`で表示する。APIキー未設定時は「設定を開く」アクションボタンを付ける。既存の
`supportingText`は流用しない。

## Alternatives

- 既存の`supportingText`（SMILESパースエラー表示）を流用する: 新しいUIコンポーネントを
  追加せずに済むが、AnyDR 0105の「設定を開く」アクションボタンを単なるテキストでは表現
  できないこと、また「今のSMILESテキストが不正」（パースエラー）と「画像認識自体が失敗
  した」（認識エラー）という意味の異なるエラーが同じ場所に混在し、AnyDR 0028の「認識失敗時
  も既存のSMILESテキストは変更しない」という方針と食い違って見えることから不採用とした。

## Consequences

- `SnackbarHost`のセットアップが必要になる。
- パースエラーと認識エラーが表示上も明確に分離される。

## Related

- [0105-missing-api-key-error-with-settings-link](./0105-missing-api-key-error-with-settings-link.md)
- [0028-handdrawn-structure-recognition-mvp](./0028-handdrawn-structure-recognition-mvp.md)
