# 0077: runRecognitionで例外をキャッチしRecognitionResult.Failureに変換する

- Date: 2026-09-06
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #14（Koog SDK導入とVision LLM呼び出しの実装）をcloseできるか棚卸ししたところ、
[AnyDR 0073](./0073-recognition-result-sealed-type.md)で決めた「`RecognitionResult.Failure`は
ネットワークエラー・不正なAPIキー・LLMがSMILESとして解釈できない応答等、複数の失敗原因を
表現する」という設計に対し、実際の`RunRecognition.kt`はLLMが空文字を返すケースしか
Failureとして扱っておらず、`promptExecutor.execute()`が例外を投げるケース（ネットワーク
エラー・不正なAPIキー等）は素通しで例外がそのまま伝播していた。この乖離を埋めてから
closeするかどうかをユーザーに確認し、「closeせず先に実装する」という回答を得た。

## Decision

`runRecognition`内で`promptExecutor.execute()`の呼び出しをtry/catchし、送出された例外を
`RecognitionResult.Failure(reason = 例外メッセージ)`に変換する。ただし`CancellationException`
は構造化並行性を壊さないよう再送出し、Failureには変換しない。

検証は、テスト用に独自の`PromptExecutor`サブクラス（`ThrowingPromptExecutor`、テストファイル
内のprivateクラス）が任意の例外を投げるケースをTDDで追加して行った。

## Alternatives

- `catch (e: Throwable)`で全キャッチ: `Error`まで握りつぶしOOM等の致命的な状況を隠蔽して
  しまうため不採用。
- `CancellationException`も含めて一律Failureにする: コルーチンのキャンセルが正しく伝播しなく
  なり、呼び出し元がキャンセルされたはずの処理を「失敗」として誤認するため不採用。

## Consequences

- `Failure(reason: String)`には例外メッセージがそのまま入る。UI側で「ネットワークエラー」
  「APIキー不正」等を区別した案内を出したい場合は、引き続きAnyDR 0073で触れられている通り
  Failureをより構造化する再検討が必要になる。
- 例外ケースのテストはモックの`PromptExecutor`サブクラスで代替しており、実際の
  `GoogleLLMClient`が投げる例外の型・メッセージ形式までは検証していない。

## Related

- [0073-recognition-result-sealed-type](./0073-recognition-result-sealed-type.md)
- [0072-koog-in-new-shared-module](./0072-koog-in-new-shared-module.md)
