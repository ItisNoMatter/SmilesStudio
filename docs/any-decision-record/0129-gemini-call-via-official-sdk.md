# 0129: Cloud FunctionsからのGemini API呼び出しはGoogle公式のNode.js SDKを使う

- Date: 2026-09-13
- Status: Accepted
- Category: tooling
- Deciders: the user, Claude Code

## Context

Issue #34のCloud Functions内で、実際にGemini APIを呼び出す実装方法（公式SDKか生の
HTTPリクエストか）を検討した。

## Decision

Google公式のNode.js SDK（`@google/genai`）を使う。

## Alternatives

- 生の`fetch`でGemini REST APIを直接叩く: 追加依存が不要という利点はあるが、リクエスト/
  レスポンスの型定義・エラーハンドリングを自前で書く必要があり実装量が増えるため、残り
  期間を踏まえ不採用とした。

## Consequences

- プロジェクトに`@google/genai`という新規依存が1つ増える（軽量）。
- 画像添付を含むリクエストの組み立て・エラーハンドリングが公式SDKに委ねられ、実装量が
  減る。

## Related

- [0126-cloud-function-as-callable](./0126-cloud-function-as-callable.md)
- [0122-serverless-proxy-firebase-nodejs](./0122-serverless-proxy-firebase-nodejs.md)
