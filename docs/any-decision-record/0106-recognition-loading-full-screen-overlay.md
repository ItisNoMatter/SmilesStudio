# 0106: 画像認識中は画面全体をブロックする半透明オーバーレイを表示する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #15の画像認識中（LLM呼び出し中）のローディング状態表示をどう実装するか検討した。

## Decision

画面全体を覆う半透明オーバーレイ＋ローディングインジケーターを表示し、認識中は他の操作を
ブロックする。

## Alternatives

- FABの中身を`CircularProgressIndicator`に差し替え、他の操作はブロックしない: 実装が小さく
  済み、待っている間もテキストフィールドを直接編集できるという利点で当初推奨したが、ユーザー
  の判断で不採用とした。「画像認識中にテキストを直接編集したいというニーズは少ない」という
  想定利用パターンを踏まえ、多重リクエスト対策も兼ねて画面全体をブロックする方式を選んだ。

## Consequences

- 画面全体を覆う新しいレイアウト要素（オーバーレイ）の実装が必要になる。
- 認識中の多重リクエストは、操作自体がブロックされるため別途対策を考える必要がない。

## Related

- [0104-image-input-fab-primary-action](./0104-image-input-fab-primary-action.md)
- [0105-missing-api-key-error-with-settings-link](./0105-missing-api-key-error-with-settings-link.md)
