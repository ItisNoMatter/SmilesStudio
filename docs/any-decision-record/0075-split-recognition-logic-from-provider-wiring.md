# 0075: Koog連携ロジックを実行ロジックとプロバイダ組み立ての2関数に分離しMockExecutorでテストする

- Date: 2026-09-05
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[AnyDR 0074](./0074-provider-enum-encapsulates-koog.md)でプロバイダをenumで受け取る設計を決めた後、実際にテスト可能な形にする関数構成を`/grill-with-docs`で検討した。調査の結果、Koogは公式に実ネットワーク呼び出しなしでテストできる`MockExecutor`（`getMockExecutor()`、`onRequestContains()`等のパターンマッチング）を提供していることが分かった（[Testing - Koog](https://docs.koog.ai/testing/)）。

## Decision

「実行ロジック」と「プロバイダ組み立て」を2つの関数に分離する。

```kotlin
// テスト対象: MockExecutorを注入してユニットテスト可能
suspend fun runRecognition(promptExecutor: PromptExecutor, imageBytes: ByteArray): RecognitionResult

// 薄いアダプタ: 実際のプロバイダ・APIキーからKoogのクライアントを組み立てて上記に委譲
suspend fun recognizeStructure(imageBytes: ByteArray, apiKey: String, provider: LLMProvider): RecognitionResult
```

`runRecognition`はKoogの`MockExecutor`を注入してTDDで検証し、`recognizeStructure`（[AnyDR 0074](./0074-provider-enum-encapsulates-koog.md)で決めたシグネチャ）は薄いアダプタとして実装する。

## Alternatives

- `recognizeStructure`1関数のみとし、テストは行わず目視確認のみとする: 関数が1つで済みシンプルという利点があったが、Koogが公式にテスト機構（MockExecutor）を提供しているにもかかわらずそれを使わないことになり、AnyDR 0066/0069のTDD方針にも反するため不採用。

## Consequences

- `computeLayout`・`kekulize`・`planMoleculeDrawing`・`computeFitScale`に続き、「独立関数を切り出してテスト可能にし、薄いアダプタ部分は別にする」というこのプロジェクトで確立されたパターンをそのまま踏襲する形になる。
- 公開APIの呼び出し口は`recognizeStructure`（薄いアダプタ）を主とし、`runRecognition`は主にテスト・内部実装用として位置づける。

## Related

- [0067-drawing-plan-as-pure-function](./0067-drawing-plan-as-pure-function.md)
- [0074-provider-enum-encapsulates-koog](./0074-provider-enum-encapsulates-koog.md)
