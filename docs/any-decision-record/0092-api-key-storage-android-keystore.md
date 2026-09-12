# 0092: APIキーの安全な保存にAndroid Keystoreを直接利用する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #16（BYOK設定画面）でユーザーが入力するLLM APIキーを安全に保存する方式を検討した。
候補は、Jetpack Securityライブラリの`EncryptedSharedPreferences`と、Android Keystoreを直接
使い暗号化した値を通常の保存先に格納する自前実装の2つだった。

## Decision

Android Keystoreを直接利用し、暗号化した値を保存する方式を採用する。`EncryptedSharedPreferences`
（`androidx.security:security-crypto`）は採用しない。

## Alternatives

- `EncryptedSharedPreferences`: 実装がシンプルで標準APIだが、`androidx.security:security-crypto`
  が長らく`1.1.0-alpha`系のままGAに至っておらず、将来のメンテナンス状況が不透明なため、
  APIキーという機密性の高い値の保存先としてリスクがあると判断し不採用とした。

## Consequences

- 暗号化・復号のコードを自前で実装する必要があり、`EncryptedSharedPreferences`利用時より
  コード量が増える。
- 保存先（SharedPreferences/DataStore等）と暗号化ロジックを分離できるため、保存先の選定は
  別途の決定事項として残る。

## Related

- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
- [0029-gemini-default-vision-llm-provider](./0029-gemini-default-vision-llm-provider.md)
