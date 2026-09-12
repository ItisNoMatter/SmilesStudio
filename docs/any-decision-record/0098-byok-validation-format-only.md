# 0098: APIキー保存時は形式チェックのみ行い、実呼び出しでの検証はしない

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

BYOK設定画面でAPIキーを保存する際、その妥当性をどこまで検証するかを検討した。

## Decision

保存時は形式チェックのみ行う（空文字でなければ保存を許可）。実際にKoog経由でテスト呼び出しを
行って有効性を確認する処理は追加しない。

## Alternatives

- 保存時に実際にKoog経由でテスト呼び出しを行い有効性を確認する: 保存した時点でキーが機能する
  ことを保証できる利点はあるが、保存操作のたびにAPIクォータを消費する（プロバイダによっては
  課金が発生しうる）、保存ダイアログにローディング/エラー表示の実装が必要になる、オフライン
  環境では保存自体ができなくなる、という3つのコストがあり不採用とした。

## Consequences

- 無効なキーでも保存できてしまい、実際に間違いに気づくのはIssue #15で画像認識を初めて使った
  タイミングになる。そのフィードバックは既存の`RecognitionResult.Failure`によるエラー表示に
  委ねる設計とする。
- 保存操作自体がオフラインで完結し、追加のネットワーク通信・APIクォータ消費が発生しない。

## Related

- [0097-byok-settings-as-alert-dialog](./0097-byok-settings-as-alert-dialog.md)
- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
