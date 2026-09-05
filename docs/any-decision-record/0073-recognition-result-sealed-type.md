# 0073: Koog連携APIの戻り値はParseResultと同じSuccess/Failureパターンを踏襲する

- Date: 2026-09-05
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[AnyDR 0072](./0072-koog-in-new-shared-module.md)で新規共通モジュールへの実装方針を決めた後、Koog連携API（画像→SMILES文字列変換）の戻り値の型設計を`/grill-with-docs`で検討した。

## Decision

`ParseResult`（`SmilesParser`の戻り値）と同じSuccess/Failureのsealed型パターンを踏襲する。

```kotlin
sealed class RecognitionResult {
    data class Success(val smiles: String) : RecognitionResult()
    data class Failure(val reason: String) : RecognitionResult()
}
suspend fun recognizeStructure(imageBytes: ByteArray, apiKey: String): RecognitionResult
```

## Alternatives

- Kotlin標準の`Result<String>`を使う: カスタムコードが不要という利点があったが、失敗理由が`Throwable`止まりで`ParseResult`ほど意図の明確な型にならず、プロジェクト内で確立された「専用のsealed型で結果を表現する」というスタイル（`ParseResult`、`Ring`等）から外れるため不採用。

## Consequences

- `Failure(reason: String)`は、ネットワークエラー・不正なAPIキー・LLMがSMILESとして解釈できない応答を返した等、複数の失敗原因を一律の文字列としてしか表現しない。UI側でエラー種別ごとに異なる案内（例: 「APIキーを確認してください」）を出す必要が生じた場合は、本AnyDRを再訪しFailureをより構造化する検討が必要になる。
- 実装時、この`RecognitionResult`型と`recognizeStructure`関数のシグネチャに沿ってTDDを進める。

## Related

- [0008-smiles-parser-result-type](./0008-smiles-parser-result-type.md)
- [0072-koog-in-new-shared-module](./0072-koog-in-new-shared-module.md)
