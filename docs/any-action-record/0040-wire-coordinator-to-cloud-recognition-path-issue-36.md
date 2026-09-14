# 0040: ImageRecognitionCoordinatorを非BYOKクラウド経路に接続した

- Date: 2026-09-14
- Related AnyDR: 0133, 0134, 0135, 0136
- Related Issue: #36

## Objective
Issue #34（Cloud Functionsの画像認識プロキシ）・Issue #35（Firebase Auth/RevenueCat SDK統合）
完了を受けて、`ImageRecognitionCoordinator`を拡張し、BYOKキーがない場合はサーバーレス関数
経由で認識を行うようにする。残り回数のSnackbar表示、無料枠使い切り時のペイウォール表示も
含む。

## Action
- `/grill-with-docs`でレスポンス契約（AnyDR 0133）、使い切り検知方式（AnyDR 0134）、
  `ImageRecognitionOutcome`のデータ構造（AnyDR 0135）、Firebase Auth待機の要否
  （AnyDR 0136）を1問ずつ検討・決定。
- Cloud Functions側（`entitlementLogic.ts`・`recognizeImage.ts`）に`remainingFreeCount`を
  レスポンスに含める変更をTDDで実装。
- Android側は`ImageRecognitionCoordinator`をBYOK/クラウドの2経路に分岐させ、新規の
  imperative shell`FirebaseCloudRecognizer`（Firebase Functions SDK呼び出し）と
  `CloudRecognitionResult`を追加。`ImageRecognitionOutcome`から`MissingApiKey`を削除し、
  `Recognized`にnullableな`remainingFreeCount`、新規`FreeTierExhausted`を追加。
- `SmilesStudioApp.kt`に無料枠使い切り時のペイウォール（`SubscriptionPaywall`をフルスクリーン
  `Dialog`でラップ）と、残り回数のSnackbar表示を配線。
- 変更したCloud Functionsを`firebase deploy --only functions`で再デプロイしようとしたところ、
  Google Cloud側の認可不整合（IAMコンソール上は「オーナー」と表示されるのに
  `testIamPermissions`が空を返す）に遭遇。Blazeプランへのアップグレード、Firebase
  Management APIの有効化など複数の設定を確認・修正したが、最終的なデプロイはまだ成功して
  いない。

## Result
Android側・Cloud Functions側とも実装完了、`./gradlew allTests :android-app:testDebugUnitTest`
と`npx jest`が全てGREEN。コミット e320ace としてpush済み。ただし非BYOKクラウド経路の
実機での最終確認（実際にデプロイされた関数を叩いての動作確認）は、上記のFirebaseデプロイ
権限問題が解消するまで持ち越し。翌朝以降に再試行する。

## Reflections
TDDの「振る舞いのRED確認」ステップで、最初`TODO()`による例外落ちをRED確認として扱ったところ、
「それでTDDの意味があるのか」と指摘された。全テストが同一の例外で落ちるだけでは、各テストの
`assertEquals`が実際に期待値を検査できているかは何も証明できていない。代わりに、分岐ロジックを
一切知らずに済む「常に固定の間違った値を返すだけの実装」に一時的に差し替えたところ、5件全てが
`expected:<X> but was:<Failed(reason=stub)>`という具体的な差分で落ち、各アサーションが確かに
意味のある値を検査していることが外側から証明できた。ホワイトボックス的な変異（分岐条件を
意図的に壊す）を提案したところそれも「知りすぎている」と却下され、この折衷案に落ち着いた
経緯だった。TODO()に頼らないRED確認の型として、今後も使えそうな手法だと思う。

また、Firebase Cloud Functionsのデプロイ権限トラブルは、IAMロール（オーナー）は正しく
表示されるのに実際の認可バックエンドでは権限がゼロという、Google Cloud側の内部不整合に
行き着いた。表示上のロールと実効権限が食い違うことがある、という事実は今後同種のトラブルに
遭遇したときの手がかりになりそうだ。
