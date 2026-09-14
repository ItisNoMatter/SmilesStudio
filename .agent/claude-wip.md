# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-14

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: Issue #34/#35実装完了、Issue #36実装完了・実機検証待ち。FirebaseプロジェクトをsmilesstudioTest-309a5に移行済み

Issue #17（RevenueCat課金）のsub-issue群のうち、#34（Cloud Functionsプロキシ）・#35
（Firebase Auth/RevenueCat SDK統合）は実装・テストGREEN・実機検証済み。#36
（`ImageRecognitionCoordinator`の非BYOKクラウド経路対応）も実装・テストGREEN済みだが、
実機での最終確認はこれから。#33（Firebase/RevenueCatプロジェクト設定）は引き続き
Play Console側の支払いプロファイル確認待ちでブロック中。

**重要**: Firebaseプロジェクトを`smilestudio-116a8`から`smilesstudio-309a5`に移行した
（詳細は「⚠️ コードと決定のズレ」参照）。旧プロジェクトの扱い（削除するか放置するか）は
未決定。

## 直近セッションでやったこと（2026-09-13〜14、Issue #34/#35/#36）

1. **Issue #34「サーバーレス関数（画像認識プロキシ）の実装」**: `/grill-with-docs`で設計
   （AnyDR 0126〜0129）。Callable Functions・Secret ManagerでのGeminiキー保持・
   Firestore単一ドキュメントモデル・`@google/genai`公式SDKを採用。TDDで
   `entitlementLogic.ts`（純粋関数）を実装、`recognizeImage`・`revenueCatWebhook`を実装。
   コミット `d3fa833`、AnyAR 0038。
2. **Issue #35「Firebase Auth・RevenueCat SDK統合」**: `/grill-with-docs`で設計
   （AnyDR 0130〜0132）。`SmilesStudioApplication`で起動時に匿名認証→RevenueCat初期化、
   `SubscriptionStatus`（Compose State）、`SubscriptionPaywall`（RevenueCat Paywalls UI
   ラッパー）を実装。`PaywallOptions.Builder`の引数名がドキュメントと違う
   （`dismissRequest`が正解）バグを、インストール済みAARのバイトコードを直接読んで解決。
   実機ログでUID一致・リスナー発火を確認。コミット `fcdf014`、AnyAR 0039。
3. **Issue #36「Coordinatorの購読/無料枠対応拡張」設計・実装**: `/grill-with-docs`で設計
   （AnyDR 0133〜0136）。`recognizeImage`のレスポンスに残り回数を追加、検知方式は
   リアクティブ（毎回サーバーに問い合わせ）、`ImageRecognitionOutcome`は
   `Recognized(smiles, remainingFreeCount: Int?)`＋`FreeTierExhausted`に変更
   （`MissingApiKey`は削除）。TDDのRED確認で「TODO()による例外落ちは何も証明しない」と
   指摘を受け、「固定の間違った値を返すだけのブラックボックスなスタブ」に差し替える手法に
   切り替えた（各テストのアサーションが実際に値を検査していることを確認できた）。
   コミット `e320ace`、AnyAR 0040。
4. **Firebaseプロジェクトの権限問題→移行**: 変更したCloud Functionsを旧プロジェクト
   `smilestudio-116a8`に再デプロイしようとしたところ、IAMコンソール上は「オーナー」だが
   `testIamPermissions`は空、という食い違いに遭遇。Blazeプラン・Firebase Management API・
   Cloud Resource Manager APIの有効化、一晩の待機でも解消せず、新規Firebaseプロジェクト
   `smilesstudio-309a5`を作成して移行した。移行の過程でFirebase CLI（v13.7.0→v15.30.0）・
   ローカルNode.js（v18.14.1→v24.19.0、winget経由）・`functions/package.json`の
   `engines.node`（18→20）も更新。新プロジェクトへのデプロイは成功
   （`recognizeImage`・`revenueCatWebhook`とも`us-central1`）。AnyAR 0041。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0125`: 前回までに反映済み（詳細は過去のWIPノート参照）。
- `0126`〜`0129`（Callable Functions・Secret Manager・Firestore単一ドキュメント・
  公式SDK）: **実装済み**（Issue #34）。
- `0130`〜`0132`（起動時匿名認証・リスナーでの購読状態・Applicationクラス）:
  **実装済み**（Issue #35）。
- `0133`〜`0136`（残り回数のレスポンス契約・リアクティブ検知・Outcome型・Auth待機なし）:
  **実装済み**（Issue #36）。実機での最終確認は未実施。

## 現在のプロジェクト構成

```
core-smiles/, ui-compose/: 前回までと変更なし

vision-recognition/: 前回までと変更なし（Gemini3_5Flash使用）

android-app/src/main/kotlin/com/smilestudio/android/
  MainActivity.kt, SmilesStudioApp.kt  Scaffold＋FAB＋Snackbar＋各種ダイアログ＋Paywall統括
  SmilesStudioApplication.kt 【Issue #35】起動時匿名認証→RevenueCat初期化
  HomeContent.kt, HowToContent.kt      変更なし
  ApiKeySettingsDialog.kt    【Issue #16】マスク入力・表示切替・削除ボタン・免責文言
  ImageSourceBottomSheet.kt  【Issue #15】カメラ/ギャラリー選択ボトムシート
  RecognitionLoadingOverlay.kt 【Issue #15】全画面ローディングオーバーレイ
  apikey/                    【Issue #16】ApiKeyStore, ApiKeyEncryptor,
                             AndroidKeystoreApiKeyEncryptor, KeyValueStore,
                             SharedPreferencesKeyValueStore
  recognition/               【Issue #15/#36】ImageRecognitionCoordinator（BYOK/クラウド
                             2経路、TDD済み）, ImageRecognitionOutcome
                             （Recognized/Failed/FreeTierExhausted）,
                             CloudRecognitionResult, FirebaseCloudRecognizer
                             （imperative shell、Callable Function呼び出し）,
                             AndroidImageResizer, ImageIo
  subscription/              【Issue #35】SubscriptionStatus（Compose State）,
                             Paywall（SubscriptionPaywall、RevenueCat Paywalls UIラッパー）
  theme/                     変更なし

android-app/src/test/kotlin/com/smilestudio/android/
  apikey/ApiKeyStoreTest.kt, recognition/ImageRecognitionCoordinatorTest.kt
  （BYOK/クラウド両経路、5テストケース、全GREEN）

functions/ 【Issue #34、Node.js/TypeScript、Gradleビルド対象外】
  src/entitlementLogic.ts    純粋関数、TDD済み、5テストGREEN
  src/firestoreEntitlement.ts  Firestoreトランザクションラッパー
  src/geminiClient.ts        @google/genai公式SDK
  src/recognizeImage.ts      Callable Function（smiles + remainingFreeCount返却）
  src/revenueCatWebhook.ts   RevenueCat Webhook受信（isSubscribed更新）
  src/index.ts               エクスポート
  engines.node: "20"（package.json、Node 18廃止対応で更新）

docs/
  any-decision-record/  0001〜0136（en/は一部のみ）
  any-action-record/    0001〜0041（en/は一部のみ）
  privacy-policy/index.html  Issue #15/#16実装済み機能を反映して更新済み

GitHub Pages: https://itisnomatter.github.io/SmilesStudio/privacy-policy/ （公開中）

【新規、このマシン上のみ・リポジトリ外】
  Firebaseプロジェクト「SmilesStudio」（ID: smilesstudio-309a5、2026-09-14作成）:
    Auth（匿名）・Firestore（asia-northeast1、ロックモード）・Blazeプラン・
    Secret Manager（GEMINI_API_KEY・REVENUECAT_WEBHOOK_SECRET）設定済み。
    Cloud Functionsデプロイ済み（recognizeImage・revenueCatWebhook、us-central1）。
    android-app/google-services.json も新プロジェクトのものに差し替え済み。
  旧Firebaseプロジェクト「SmilesStudio」（ID: smilestudio-116a8）: IAMの原因不明の不整合
    により放棄。削除するか調査を続けるかは未決定（下記「既知の注意点」参照）。
  RevenueCatプロジェクト「SmilesStudio」: エンタイトルメント`pro`作成済みだが、
    Google Play連携（サービスアカウント認証情報）・Webhook URLは旧プロジェクト向けの
    ままで未更新（下記「⚠️ コードと決定のズレ」参照）。

GitHub Issues:
  Issue #11 マップ「Shipaton 2026」:
    #12,#13,#14,#15,#16,#24,#25,#26,#30 クローズ済み。#18は非公開テスト実行中（未close）。
    #17は5つのsub-issueに分割: #34/#35 実装済み（未close）、#36 実装済み・実機検証待ち、
    #33 一部完了・Play Console決済プロファイル待ちでブロック中、#37 未着手。
    #38（匿名認証失敗時のハンドリング未実装、#35の実装漏れ、Issue #36中に発見）新規。
    フロンティア: #22（テストハーネス）、#27（英語対応）、#28（複数プロバイダ対応、将来）、
    #29（Strategy Graph技術検証、将来）、#31（本番リリース国/地域設定、Issue #23の子）、
    #32（無料枠カウンター再インストール回避、AnyDR 0124で解消見込みだが未close）
```

## ⚠️ コードと決定のズレ

- **Firebaseプロジェクト移行に伴う未更新箇所**: RevenueCatダッシュボードのGoogle Play連携
  （サービスアカウント認証情報）とWebhook URLが、まだ旧プロジェクト`smilestudio-116a8`
  向けの設定のまま。新プロジェクト`smilesstudio-309a5`向けに更新する必要がある
  （Play Console決済プロファイルのブロックが解消してから着手するのが自然）。
- Issue #36: コード実装・自動テストはGREENだが、実機（エミュレータ/実機）でクラウド経路の
  動作確認がまだ済んでいない。次にやるべきこと。
- Issue #27（英語対応）: Issueは作成したが実装は未着手。
- `0037`のテストハーネス層（三層防御OSS戦略）→ Issue #22: 未実装。
- Issue #17完全実装後、RevenueCat/Firebase Auth導入を反映したプライバシーポリシーの
  再更新が必要（Issue #30の対応範囲外として明示的に先送りした）。
- Issue #32（無料枠カウンターの再インストール回避）: AnyDR 0124の方針転換で実質解消見込み
  だが、まだcloseしていない。

## 既知の注意点（未対応・要フォローアップ）

1. **旧Firebaseプロジェクト`smilestudio-116a8`のIAM不整合**: IAMコンソール上は
   オーナーロールが正しく表示・保存されているのに、`testIamPermissions`が実効権限ゼロを
   返す状態が、Blazeプラン・複数API有効化・一晩の待機でも解消しなかった。原因未特定のまま
   新プロジェクトへ移行した。この旧プロジェクトを削除するかどうかは未決定（残しておくと
   Google Cloud Supportへの問い合わせ材料として使えるが、放置コストも僅かにある）。
2. レート制限の判定はKoogの例外メッセージの文字列マッチングに依存する脆い実装（AnyDR 0078）。
   JetBrains/koogのYouTrack [KG-652](https://youtrack.jetbrains.com/issue/KG-652)にユーザーが
   コメント投稿済み。解決されれば置き換え候補。
3. `Element`に`B`（ホウ素）がなく、芳香族小文字の`b`は未対応のまま。
4. `Molecule.rings`のDFS背後辺方式・`computeLayout`の固定角度配置は縮合環・橋かけ環を正しく
   扱えない（v1スコープでは問題ない）。
5. `ai.koog:prompt-executor-google-client`は`koog-agents`本体（1.2.0安定版）とは独立
   バージョニングでまだbeta（1.1.1-beta）。
6. android-appは`androidx.compose.material3:material3:1.5.0-alpha27`という不安定版に
   直接依存している。将来安定版で解決されたらJetBrains CMP側のmaterial3に戻すか検討の余地
   あり。
7. このマシンのAndroid SDKは`D:\Android\Sdk`。AVD`SmileStudio_Test`（API 36）が1件作成済み。
8. Play Console側で定期購入商品の「作成」ボタンが表示されない問題が未解決。お支払い
   プロファイルの受け取り方法（銀行口座）を登録したが「確認待ち」のまま変化なし。

## 次にやりそうなこと（未着手）

- **Issue #36の実機検証**: 新しい`google-services.json`でアプリをビルドし直し、
  エミュレータ/実機で非BYOKクラウド経路（残り回数Snackbar・無料枠使い切り時のペイウォール）
  を確認する。次に着手する想定。
- **Issue #33の残り**: Play Console決済プロファイル確認完了待ち→定期購入商品作成→
  RevenueCatのOffering/Paywallデザイン→Google Play連携・Webhook URLを新Firebaseプロジェクト
  向けに更新。
- Issue #18: 非公開テストのテスター数を定期的に確認。
- 並行着手可能: Issue #27（英語対応）・#22（テストハーネス）・Issue #37（APIキー設定
  ダイアログへの購読導線追加、Issue #35完了で着手可能）。
- Issue #17完全実装後: プライバシーポリシーの再更新。
