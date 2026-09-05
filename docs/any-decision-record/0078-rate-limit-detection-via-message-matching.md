# 0078: レート制限は例外メッセージの文字列マッチングで検知し専用のFailure文言にする

- Date: 2026-09-06
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[AnyDR 0077](./0077-catch-exceptions-into-recognition-failure.md)で例外を`RecognitionResult.Failure`
に変換する実装を追加した後、「API制限（レート制限）が発生した場合、それを明示的にFailureとして
扱いたい」という要望があった。

調査の結果、Koogの`GoogleLLMClient`はHTTPエラー（429含む）を`ai.koog.prompt.executor.clients
.LLMClientException(message, provider, cause)`という単一の例外クラスにラップしており、ステータス
コードやレート制限ヘッダーを取り出せる公開フィールドは存在しない（`message`文字列に埋め込まれる
のみ）。つまりレート制限かどうかの判定は、現状のKoogのAPIでは例外メッセージの文字列マッチングに
頼らざるを得ない。

この制約を踏まえ、(A)既存の`Failure(reason: String)`型のまま文字列マッチングで判定する案と、
(B)`RecognitionResult.Failure`をsealed型/enumで構造化しレート制限等を型で表現する案を比較した。
また、Koogの公式YouTrack（https://youtrack.jetbrains.com/issues/KG）を確認したところ、
[KG-652](https://youtrack.jetbrains.com/issue/KG-652)「Support HTTP Headers in
KoogHttpClientException」が未解決のまま存在し、まさに「レスポンスヘッダーが破棄されており
プロバイダのレート制限情報を例外から取り出せない」という同じ課題を扱っていることが分かった。

## Decision

(A)案を採用する。`runRecognition`内でキャッチした例外のメッセージに`"429"` `"resource_exhausted"`
`"rate limit"` `"too many requests"` `"quota"`（小文字化した上で部分一致、Gemini/HTTP双方の
表現を想定）のいずれかが含まれる場合、`RecognitionResult.Failure`のreasonを専用の定型文言
（`RATE_LIMIT_FAILURE_REASON`）に差し替える。含まれない場合は従来通り例外メッセージをそのまま
reasonに使う（[AnyDR 0077](./0077-catch-exceptions-into-recognition-failure.md)の挙動を維持）。

KG-652には新規Issueを重複投稿せず、必要であればユーザー自身がコメント・投票で乗る方針とする
（本AnyDRはその参照記録を兼ねる）。

## Alternatives

- (B) `RecognitionResult.Failure`をsealed型/enumで構造化する: UI側で理由ごとに異なる案内を
  出しやすくなる利点はあるが、判定ロジック自体はKoogが構造化エラー情報を提供していない以上
  結局メッセージ文字列のヒューリスティックに依存し、根本解決にはならない。既存のテストや
  今後のUI実装（Issue #15）への影響も大きく、Issue #14のMVPスコープに対して過剰と判断し不採用。
- KG-652の解決を待ってから対応する: KG-652は2025年1月に登録されたまま長期未解決であり、
  Shipaton 2026の期限（2026-09-30）に対して待つのは非現実的なため不採用。

## Consequences

- レート制限判定はメッセージ文字列への部分一致に依存する。Koogやプロバイダ側の例外メッセージの
  文言が変わると検知できなくなる脆さが残る。KG-652が将来解決されヘッダー情報が例外から取得
  できるようになれば、このマッチングロジックをより堅牢な実装に置き換えることを検討する。
- 現時点ではGoogle Gemini提供のエラー表現（`RESOURCE_EXHAUSTED`等）とHTTP標準の表現（`429`
  `rate limit`等）の両方をカバーしているが、他プロバイダを追加する際は表現の違いを都度確認し
  マーカーを追加する必要がある。

## Related

- [0077-catch-exceptions-into-recognition-failure](./0077-catch-exceptions-into-recognition-failure.md)
- [0073-recognition-result-sealed-type](./0073-recognition-result-sealed-type.md)
