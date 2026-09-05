# 0025: Issue #14のクローズ判断とRecognitionResult.Failureの実装ギャップ解消

- Date: 2026-09-06
- Related AnyDR: 0077, 0078
- Related Issue: #14

## Objective
Issue #14（Koog SDK導入とVision LLM呼び出しの実装）をcloseできるか棚卸しし、closeできる状態
でなければギャップを埋める。特にユーザーからの要望で、API制限（レート制限）を明示的に
`RecognitionResult.Failure`として扱えるようにする。

## Action
*   Issue #14のスコープ（Koog SDK導入・Gemini呼び出し・BYOK・MVP画像1枚→SMILES1候補）と
    実装済みコードを照合。表面的なスコープは満たしていたが、[AnyDR 0073](../any-decision-record/0073-recognition-result-sealed-type.md)
    の決定（Failureはネットワークエラー等を表現する）に対し、実装では例外が素通しで伝播する
    ギャップを発見。
*   ユーザー判断で「closeせず先に実装する」を選択。TDDで`runRecognition`に例外→Failure変換を
    追加（`CancellationException`は再送出）。
*   ユーザーからレート制限の明示的な扱いを要望されたため、実装案A（文字列マッチング）とB
    （`RecognitionResult`の型構造化）をPros/Cons形式で提示。
*   ユーザー指示でKoogの公式YouTrack（JetBrains/koog、`KG`プロジェクト）をREST API経由で調査し、
    完全に一致する未解決issue [KG-652](https://youtrack.jetbrains.com/issue/KG-652)
    （HTTPヘッダー未対応でレート制限情報を取り出せない）を発見。新規issueは投稿せず、A案
    （文字列マッチング）を採用してTDDで実装。
*   [AnyDR 0077](../any-decision-record/0077-catch-exceptions-into-recognition-failure.md)・
    [0078](../any-decision-record/0078-rate-limit-detection-via-message-matching.md)として記録。
*   ユーザーがKG-652に現状の回避策（メッセージ文字列マッチング）を含むコメントを投稿。
*   `allTests`のGREENを確認後コミット・push、Issue #14をclose。

## Result
*   `vision-recognition`モジュールの全テストGREEN（`./gradlew allTests`成功）。
*   コミット833a62dをorigin/mainにpush。
*   AnyDR 0077・0078を追加。
*   [KG-652](https://youtrack.jetbrains.com/issue/KG-652/Support-HTTP-Headers-in-KoogHttpClientException#focus=Comments-27-14372140.0-0)
    にコメント投稿済み。
*   Issue #14をclose。

## Reflections
Issueをcloseする前の棚卸しが、AnyDRで明文化した設計意図（0073）と実際のコードの乖離を見つける
良いきっかけになった。「スコープの文言は満たしているが、関連する過去の設計決定までは満たして
いない」というギャップは、Issue本文だけを見ていては気づけなかった。

またKoogの`LLMClientException`がHTTPステータスコードやレスポンスヘッダーを公開しておらず、
エラー種別判定を文字列マッチングに頼らざるを得ないという制約は、自分たちだけの問題ではなく
JetBrains/koog側でも既知の未解決issue（KG-652、2025年1月登録のまま長期未解決）だった。
OSSの依存先の制約にぶつかった時、自前で無理に回避策を作り込む前に上流のissue trackerを
確認する価値を実感した。
