# 0074: Koog連携APIはプロバイダをenumで受け取り、Koogのクライアントクラスをモジュール内に閉じ込める

- Date: 2026-09-05
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[AnyDR 0073](./0073-recognition-result-sealed-type.md)で戻り値の型を決めた後、Koog連携APIがプロバイダ（Gemini/OpenAI等）をどう扱うかを`/grill-with-docs`で検討した。Issue #14のスコープには「BYOK方式を最初から組み込む。特定プロバイダに固定しない設計とする」とある。

当初、Koogの`LLMClient`インスタンスを直接受け取るアプローチ（呼び出し側がプロバイダごとのクライアントを構築して渡す）を推奨したが、ユーザーから「プロバイダをenumで受け取る案も責務分離としては明確では」という指摘があり、再検討した。再検討の結果、「Koogの各プロバイダ固有のクライアントクラスへの結合がこのモジュール内に閉じ込められる」という点は当初Consとして挙げていたが、実際にはカプセル化という長所であり、CLAUDE.mdが定める「特定の技術依存を1つのモジュール内に閉じ込め、他のレイヤーには漏らさない」という既存のアーキテクチャ原則（`core-smiles`がUIやAndroid固有の依存を持たない、と同じ考え方）に忠実であると判断した。

## Decision

Koog連携APIはプロバイダをenumで受け取る設計とする。

```kotlin
enum class LLMProvider { GOOGLE_GEMINI, OPENAI }
suspend fun recognizeStructure(imageBytes: ByteArray, apiKey: String, provider: LLMProvider): RecognitionResult
```

内部で`provider`に応じてKoogの`GoogleLLMClient`/`OpenAILLMClient`等を組み立てる。Koogの具体的なクライアントクラスを知っているのはこの新モジュールだけとし、呼び出し側（将来のIssue #16 BYOK設定画面のUIコード等）はenumと文字列のAPIキーだけを扱えばよい設計にする。

## Alternatives

- KoogのLLMClientインスタンスを直接受け取る（当初推奨案）: 「特定プロバイダに固定しない」という要件に文字通り忠実で、新しいプロバイダ対応時もこのモジュールの変更が不要という利点があったが、呼び出し側がKoogの具体的なクライアント構築APIを直接扱う必要があり、結果としてKoog依存がUI層にまで漏れ出すことになるため不採用。再検討の結果、enumで受け取りモジュール内にKoog依存を閉じ込める方が、このプロジェクトの一貫したアーキテクチャ原則（技術依存の1モジュールへのカプセル化）により忠実と判断した。

## Consequences

- サポートするプロバイダが増えるたびに、このモジュール内のenum・マッピングロジックを更新し続ける必要がある。MVPスコープ（Gemini中心、将来的に数プロバイダ程度）では許容範囲と判断する。
- 将来のIssue #16（BYOK設定画面）のUIコードは、Koogを直接importせず、enumと文字列のAPIキーだけを扱えばよくなる。

## Related

- [0028-handdrawn-structure-recognition-mvp](./0028-handdrawn-structure-recognition-mvp.md)
- [0029-gemini-default-vision-llm-provider](./0029-gemini-default-vision-llm-provider.md)
- [0072-koog-in-new-shared-module](./0072-koog-in-new-shared-module.md)
- [0073-recognition-result-sealed-type](./0073-recognition-result-sealed-type.md)
