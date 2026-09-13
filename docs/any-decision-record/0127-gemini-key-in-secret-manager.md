# 0127: 開発者のGemini APIキーはGoogle Cloud Secret Managerで保持する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #34のCloud Functions（AnyDR 0126）で、開発者のGemini APIキーをどう保持するか検討した。

## Decision

Google Cloud Secret Managerで保持する。

## Alternatives

- Cloud Functionsの環境変数（`.env`ファイルやFirebase Functions Config）: 設定が簡単で
  追加のサービス有効化が不要という利点はあるが、環境変数はデプロイ設定やログに露出する
  リスクがSecret Managerより高く、`functions.config()`は非推奨化が進んでいるため不採用と
  した。

## Consequences

- Secret Managerの有効化・シークレット登録・Cloud Functionsへのアクセス権限付与という
  追加の設定手順が必要になる。
- BYOKでAndroid Keystoreを使った経緯（AnyDR 0092）と一貫性のある、機密情報の安全な
  取り扱いになる。

## Related

- [0126-cloud-function-as-callable](./0126-cloud-function-as-callable.md)
- [0092-api-key-storage-android-keystore](./0092-api-key-storage-android-keystore.md)
