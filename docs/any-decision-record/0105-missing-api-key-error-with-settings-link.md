# 0105: APIキー未設定時はFABのタップを許可しエラー＋設定画面への導線を出す

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0104]]で「画像から認識」をFABとして配置すると決めたが、BYOK設定（Issue #16）でAPIキーが
未設定の状態でタップされた場合の挙動が未定だった。

## Decision

FABのタップは常に許可する。APIキーが未設定の場合はエラーメッセージを表示し、そこから
「APIキー設定を開く」導線（Issue #16で実装した`ApiKeySettingsDialog`を開く）を出す。

## Alternatives

- APIキー未設定時はFAB自体を無効化（disabled）表示にする: 使えない操作を事前に防げるが、
  なぜ無効化されているのかが視覚的に伝わりにくく、無効化理由を説明するツールチップ等の
  追加実装が必要になるため不採用とした。

## Consequences

- 未設定時に一度無駄なタップ（ボトムシート表示→エラー）が発生するが、そのままエラー
  メッセージから設定画面に誘導できるため実害は小さい。
- FABの見た目は常に同じ（有効/無効の出し分けなし）で済み、実装がシンプルになる。

## Related

- [0104-image-input-fab-primary-action](./0104-image-input-fab-primary-action.md)
- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
