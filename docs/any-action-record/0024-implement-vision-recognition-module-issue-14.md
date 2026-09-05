# 0024: vision-recognitionモジュールを実装しKoog経由の構造認識を追加

- Date: 2026-09-06
- Related AnyDR: 0072, 0073, 0074, 0075, 0076
- Related Issue: #14

## Objective
Shipaton 2026 MVP機能（手描き構造式画像 → Vision LLM → SMILES変換）の土台として、
Koogを用いた画像認識ロジックを担う新しいKMPモジュールを実装する。

## Action
`vision-recognition`モジュール（jvm/androidターゲット）を新規作成し、以下を実装した。

*   `RecognitionResult`（Success/Failureのsealed型）
*   `LLMProvider` enum（Koogのプロバイダ別クライアントをカプセル化）
*   `runRecognition`のコアロジック（Koog自身の`MockExecutor`で検証、実APIコールなし）
*   上記コアに薄く被せた`recognizeStructure`アダプタ

実装と並行して、モジュール配置・`RecognitionResult`の型設計・プロバイダenumによる
カプセル化・認識ロジックとプロバイダ配線の分離・モジュール名、の5件をAnyDR
0072〜0076として記録した。あわせて技術スパイクを行い、KoogがプロジェクトのKMP
android targetで解決・コンパイル可能であることを確認した。

## Result
13ファイル変更・310行追加（`gradle/libs.versions.toml`、`settings.gradle.kts`、
モジュール本体、テストを含む）。`RunRecognitionTest`はKoogの`MockExecutor`に対して
GREEN。

## Reflections
`ai.koog:prompt-executor-google-client`が`koog-agents`本体（安定版1.2.0）とは独立して
バージョニングされており、まだbeta止まり（1.1.1-beta）であることが技術スパイク中に
判明した。Gemini連携を本格化する際にAPI面がまだ動く可能性がある点は要注意。
`MockExecutor`によるテストで実APIを叩かずにコア認識ロジックを検証できたのは、
今後Vision LLMまわりを触る上でも再利用できるパターンだと感じた。
