# 0039: Android アプリに Firebase Auth・RevenueCat SDK を統合した

- Date: 2026-09-14
- Related AnyDR: 0130, 0131, 0132
- Related Issue: #35

## Objective
Issue #34（Cloud Functions側の画像認識プロキシ）実装完了を受けて、Android アプリ側に
Firebase Auth（匿名認証）と RevenueCat SDK を組み込み、アプリ起動時に匿名認証のUIDを
RevenueCatの`app_user_id`として初期化する（AnyDR 0123の実現）。購読状態の取得とPaywalls
表示導線の土台も用意する（実際の画面組み込みはIssue #36のスコープ）。

## Action
- `gradle/libs.versions.toml`にFirebase BoM 34.19.0、RevenueCat Purchases 10.15.1、
  Google Servicesプラグイン4.5.0を追加。
- `android-app/build.gradle.kts`で、`keystore.properties`と同じパターンで
  `google-services.json`・`revenuecat.properties`（いずれもgitignore対象）の有無に応じて
  条件付きでGoogle Servicesプラグインを適用し、`BuildConfig.REVENUECAT_API_KEY`を生成。
- `SmilesStudioApplication`（新規`Application`サブクラス）を作成し、`onCreate`で
  `Firebase.auth.signInAnonymously()`を実行、成功したUIDで`Purchases.configure(...)`を初期化。
- `SubscriptionStatus`（Compose State保持オブジェクト）と、RevenueCatの
  `UpdatedCustomerInfoListener`から購読状態（`entitlements["pro"]`）を更新する配線を実装。
- `SubscriptionPaywall`（RevenueCat Paywalls UIのラッパーComposable）を実装。
- 実機（エミュレータ）で動作確認: `./gradlew :android-app:compileDebugKotlin`、
  `./gradlew allTests :android-app:testDebugUnitTest`（全GREEN、既存テストに回帰なし）、
  `./gradlew :android-app:installDebug`、logcatでFirebase AuthのUIDとRevenueCatの
  "Initial App User ID"が一致すること、リスナー発火とCustomerInfo取得成功を確認。
  Play Console側の定期購入プロダクトが未作成（Issue #33がブロック中）のため
  "no offerings"・エミュレータの`BILLING_UNAVAILABLE`のエラーログが出るが、これは想定内。

## Result
Issue #35を実装・検証完了。コミット `fcdf014`としてpush済み（`origin/main`）。
Firebase Auth・RevenueCatの初期化・UID一致・リスナー発火まで実機で確認済み。
Issue #36（Coordinatorへの非BYOKパス配線、残り回数のSnackbar表示、Paywall起動トリガー）と
Issue #37（APIキー設定ダイアログへの「購読する」導線追加）がこれでブロック解除された。

## Reflections
RevenueCat公式ドキュメントのPaywalls UIサンプルコードが`onDismiss`という引数名を使っていたが、
実際にインストールされた`purchases-ui:10.15.1`では`dismissRequest`という名前だった
（コンパイルエラーで発覚）。今回は`unzip`+`strings`でインストール済みAARの`.class`ファイルから
実際のシンボル名を直接抽出して確認した。このセッションを通じて、Koogの`GoogleModels`列挙値や
`@google/genai`のSDK形状でも同じパターン（ドキュメント・記憶を信じずインストール済み実体を
直接検証する）が繰り返し有効だった。サードパーティAPIは「ドキュメント通り」を前提にせず、
コンパイルエラーが出た時点で実体を確認しにいく習慣が今回も時間を救った。
