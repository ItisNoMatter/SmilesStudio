# 0093: 暗号化済みAPIキーの保存先にSharedPreferencesを使う

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0092]]でAndroid Keystoreを使ってAPIキーを暗号化する方式を決めたが、暗号化済みの値そのものを
どこに保存するかは別途の決定事項として残っていた。候補は`SharedPreferences`とJetpack
DataStore（Preferences DataStore）の2つだった。

## Decision

暗号化済みAPIキーの保存先に`SharedPreferences`を使う。DataStoreは採用しない。

## Alternatives

- Jetpack DataStore: Coroutines/Flowベースで非同期・Composeとの相性が良く、Googleが今後推奨する
  保存先だが、保存するデータが暗号化済みAPIキー1個程度の単純なキーバリューであり、DataStoreの
  非同期API・Flow購読の恩恵をほぼ活かせない。また`androidx.datastore`という新規依存の追加が
  必要になる点もコストと判断し、不採用とした。

## Consequences

- `android-app`に新規依存（`androidx.datastore`）を追加せずに済む。
- 将来、設定値が増えて複雑化した場合はDataStoreへの移行を再検討する余地がある。

## Related

- [0092-api-key-storage-android-keystore](./0092-api-key-storage-android-keystore.md)
- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
