# 0126: 画像認識プロキシはFirebase Callable Functionsとして実装する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #34（サーバーレス関数（画像認識プロキシ）の実装）で、Cloud Functionsのエンドポイント
形式（Firebase Callable FunctionsまたはHTTPSトリガー関数）を検討した。

## Decision

Firebase Callable Functionsとして実装する。

## Alternatives

- 素のHTTPS Cloud Function: multipart/form-data等で画像バイナリを直接送信でき、Base64の
  オーバーヘッドがない利点はあるが、Firebase AuthのIDトークン検証・CORS設定を自前実装する
  必要があり、認証周りの実装コストと安全性を優先しCallable Functionsを選んだため不採用と
  した。

## Consequences

- クライアント（Android側）はFirebase Functions SDK経由で関数を呼び出す実装になる。
- 画像はBase64エンコードして送信するため、リサイズ済み画像（AnyDR 0108）に対して約33%の
  データ量増加が発生するが、実害は小さいと判断。
- Firebase AuthのIDトークン検証は`context.auth`から自動で取得できる。

## Related

- [0121-serverless-proxy-for-app-provided-api-key](./0121-serverless-proxy-for-app-provided-api-key.md)
- [0122-serverless-proxy-firebase-nodejs](./0122-serverless-proxy-firebase-nodejs.md)
