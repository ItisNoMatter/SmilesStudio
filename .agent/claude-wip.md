# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-13

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: Issue #15/#16/#30が完了。Issue #17（課金）は設計完了・Firebase/RevenueCatセットアップ中（Play Console側の銀行口座確認待ちでブロック中）

Issue #16（BYOK設定画面）・Issue #15（手描き構造式認識UI、実APIキーで動作確認済み）・
Issue #30（プライバシーポリシー導線追加・本文更新）が完了・close済み。Issue #17
（RevenueCat課金）は`/grill-with-docs`で設計を完了し（AnyDR 0112〜0125）、5つのsub-issue
（#33〜#37）に分割。Issue #33（Firebase/RevenueCatプロジェクト設定）の大部分が完了したが、
Play Console側の定期購入商品作成が「お支払いプロファイルの受け取り方法（銀行口座）確認待ち」
で止まっている。次はIssue #34（Cloud Functions実装）に着手する。

Issue #18（非公開テスト）は継続中（〜2026-09-23頃まで12人以上のオプトイン維持が必要）。

## 直近セッションでやったこと（2026-09-12〜13、Issue #15/#16/#17/#30）

1. **Issue #16「BYOK設定画面」**: `/grill-with-docs`で設計（AnyDR 0091〜0101）。Android
   Keystore暗号化＋`SharedPreferences`保存の`ApiKeyStore`をTDDで実装、`ApiKeySettingsDialog`
   （マスク入力・削除ボタン・免責文言）を実装。エミュレータで保存・復元・削除を確認。
   コミット `5bb5b73` 、AnyAR 0035。
2. **Issue #15「手描き構造式認識UI」**: `/grill-with-docs`で設計（AnyDR 0102〜0108）。
   カメラ撮影（`TakePicture`+`FileProvider`、権限不要）・ギャラリー選択
   （`PickVisualMedia`、権限不要）・FAB＋`ModalBottomSheet`・全画面ローディング
   オーバーレイ・Snackbarでのエラー表示を実装。`ImageRecognitionCoordinator`をTDDで実装。
   実機確認中に`isRecognizing`がSnackbar表示完了まで`true`のままでオーバーレイが操作を
   ブロックするバグを発見・修正。コミット `fd8685b` 。その後、実際のGemini APIキーで
   検証したところ`gemini-2.5-flash`が新規ユーザー向けに404（廃止）となり
   `gemini-3.5-flash`に切り替え（コミット `a912f4f` ）、ベンゼン環の実画像から`c1ccccc1`
   認識を確認。AnyAR 0036。
3. **Koog Strategy Graphの調査**: `vision-recognition`は未使用（Prompt Executor層のみ）
   であることを確認しHTMLアーティファクトで図解。将来の技術検証としてIssue #29
   （パーサー検証つき自己修正ループ）を起票。
4. **Issue #30「アプリ内プライバシーポリシー導線」**: `/grill-with-docs`で設計
   （AnyDR 0109〜0111）。「このアプリについて」ダイアログにリンク追加（外部ブラウザで
   開く）。プライバシーポリシー本文がIssue #15/#16実装後も「送信しません」のまま実態と
   食い違っていたことに気づき、同時に更新。コミット `3a78837` 、AnyAR 0036。
5. **Issue #17「RevenueCat課金」設計**: `/grill-with-docs`で設計（AnyDR 0112〜0125）。
   無料枠月5回（X/Discordでの問いかけ結果）、価格月額480円/年額3,600円、暦月リセット、
   RevenueCat Paywalls採用。設計途中で「BYOKを使わない全ユーザー向けのAPIキーをどこに
   置くか」が未決だったことが発覚し、Firebase Cloud Functions（Node.js）でGeminiキーを
   サーバー側に保持する方針に転換（無料枠カウンターもFirestoreへ移行、AnyDR 0117を
   AnyDR 0124で撤回）。価格の採算試算も実施（有料1人で無料70〜90人分をカバー可能）。
   有料プランへの利用上限は導入しない（AnyDR 0125）。Issue #17を5つのsub-issue
   （#33〜#37）に分割し依存関係を設定。AnyAR 0037。
6. **Issue #33「Firebase/RevenueCatプロジェクト設定」着手**: Firebaseプロジェクト作成、
   Firebase Auth匿名認証有効化、Firestore作成（デフォルト全拒否ルール維持）、RevenueCat
   アカウント・Androidアプリ登録、GCPサービスアカウント作成・JSONキー発行・Play Console
   側権限付与、RevenueCatエンタイトルメント`pro`作成まで完了。Play Console側の定期購入
   商品作成が「作成」ボタン非表示で詰まり、原因調査の結果お支払いプロファイルの受け取り
   方法（銀行口座）が未登録と判明し登録したが、「確認待ち」のままボタンは出ず未解決
   （原因未確定、銀行口座確認完了待ちと推測）。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0090`: 前回までに反映済み（詳細は過去のWIPノート参照）。
- `0091`〜`0101`（BYOK設定画面）: **実装済み**（Issue #16）。
- `0102`〜`0108`（画像入力・FAB・ローディング・エラー表示・リサイズ）: **実装済み**
  （Issue #15）。
- `0109`〜`0111`（プライバシーポリシー導線・本文更新）: **実装済み**（Issue #30）。
- `0112`〜`0125`（課金パラメータ・サーバーレス構成）: **設計のみ**、実装はsub-issue
  #33〜#37で進行中（#33一部完了、#34〜#37未着手）。

## 現在のプロジェクト構成

```
core-smiles/, ui-compose/: 前回までと変更なし

vision-recognition/src/commonMain/kotlin/com/smilestudio/vision/
  RecognizeStructure.kt  GoogleModels.Gemini3_5Flash使用（2.5-flash廃止対応）
  RunRecognition.kt      変更なし（例外→Failure変換、レート制限文字列マッチング）

android-app/src/main/kotlin/com/smilestudio/android/
  MainActivity.kt, SmilesStudioApp.kt  Scaffold＋FAB＋Snackbar＋各種ダイアログ統括
  HomeContent.kt          MoleculeCanvas上にFAB（画像から認識）を配置
  HowToContent.kt         変更なし
  ApiKeySettingsDialog.kt 【Issue #16】マスク入力・表示切替・削除ボタン・免責文言
  ImageSourceBottomSheet.kt 【Issue #15】カメラ/ギャラリー選択ボトムシート
  RecognitionLoadingOverlay.kt 【Issue #15】全画面ローディングオーバーレイ
  apikey/                 【Issue #16】ApiKeyStore, ApiKeyEncryptor,
                          AndroidKeystoreApiKeyEncryptor, KeyValueStore,
                          SharedPreferencesKeyValueStore
  recognition/            【Issue #15】ImageRecognitionCoordinator（TDD済み）,
                          ImageRecognitionOutcome, AndroidImageResizer, ImageIo
  theme/                  変更なし

android-app/src/test/kotlin/com/smilestudio/android/
  apikey/ApiKeyStoreTest.kt, recognition/ImageRecognitionCoordinatorTest.kt

android-app/src/main/AndroidManifest.xml
  FileProvider追加（file_paths.xml、カメラ撮影用）

android-app/build.gradle.kts
  project(":vision-recognition")依存追加、kotlin("test-junit")追加

docs/
  any-decision-record/  0001〜0125（en/は一部のみ）
  any-action-record/    0001〜0037（en/は一部のみ）
  privacy-policy/index.html  Issue #15/#16実装済み機能を反映して更新済み

GitHub Pages: https://itisnomatter.github.io/SmilesStudio/privacy-policy/ （公開中）

【新規、このマシン上のみ・リポジトリ外】
  Firebaseプロジェクト「SmilesStudio」: Auth（匿名）・Firestore（asia-northeast1）作成済み
  RevenueCatプロジェクト「SmilesStudio」: Androidアプリ登録済み、エンタイトルメント`pro`作成済み、
    Service Account Credentials JSONアップロード済み（検証は伝播待ち〜銀行口座確認待ち）
  GCPサービスアカウント revenuecat-service-account@smilestudio-116a8.iam.gserviceaccount.com
    （Pub/Sub編集者・モニタリング閲覧者ロール、Play Console側にも招待済み）

GitHub Issues:
  Issue #11 マップ「Shipaton 2026」:
    #12,#13,#14,#15,#16,#24,#25,#26,#30 クローズ済み。#18は非公開テスト実行中（未close）。
    #17は5つのsub-issueに分割（#33一部完了、#34〜#37未着手）。
    フロンティア: #22（テストハーネス）、#27（英語対応）、#28（複数プロバイダ対応、将来）、
    #29（Strategy Graph技術検証、将来）、#31（本番リリース国/地域設定、Issue #23の子）、
    #32（無料枠カウンター再インストール回避、AnyDR 0124で解消見込みだが未close）
```

## ⚠️ コードと決定のズレ

- Issue #17: AnyDR 0112〜0125で設計は完了しているが、コード実装は未着手（sub-issue
  #33〜#37で今後進める。#33のみ一部完了）。
- Issue #27（英語対応）: Issueは作成したが実装は未着手。
- `0037`のテストハーネス層（三層防御OSS戦略）→ Issue #22: 未実装。
- Issue #17実装完了後、RevenueCat/Firebase Auth導入を反映したプライバシーポリシーの
  再更新が必要（今回のIssue #30の対応範囲外として明示的に先送りした）。
- Issue #32（無料枠カウンターの再インストール回避）: AnyDR 0124の方針転換で実質解消見込み
  だが、まだcloseしていない。

## 既知の注意点（未対応・要フォローアップ）

1. レート制限の判定はKoogの例外メッセージの文字列マッチングに依存する脆い実装（AnyDR 0078）。
   JetBrains/koogのYouTrack [KG-652](https://youtrack.jetbrains.com/issue/KG-652)にユーザーが
   コメント投稿済み。解決されれば置き換え候補。
2. `Element`に`B`（ホウ素）がなく、芳香族小文字の`b`は未対応のまま。
3. `Molecule.rings`のDFS背後辺方式・`computeLayout`の固定角度配置は縮合環・橋かけ環を正しく
   扱えない（v1スコープでは問題ない）。
4. `ai.koog:prompt-executor-google-client`は`koog-agents`本体（1.2.0安定版）とは独立
   バージョニングでまだbeta（1.1.1-beta）。Geminiのモデル廃止（2.5-flash→3.5-flash対応済み）
   のように、今後も上流のモデルライフサイクル変化への追従が必要になる可能性がある。
5. android-appは`androidx.compose.material3:material3:1.5.0-alpha27`という不安定版に
   直接依存している。将来安定版で解決されたらJetBrains CMP側のmaterial3に戻すか検討の余地
   あり。
6. このマシンのAndroid SDKは`D:\Android\Sdk`。AVD`SmileStudio_Test`（API 36）が1件作成済み。
   エミュレータは長時間セッションでメモリ逼迫しやすく、ANRや起動遅延・システムカメラアプリの
   ANRが起きることがある（Issue #15検証時に発生、実機ではユーザーが手動確認）。
7. Play Console側で定期購入商品の「作成」ボタンが表示されない問題が未解決。お支払い
   プロファイルの受け取り方法（銀行口座）を登録したが「確認待ち」のまま変化なし。原因は
   未確定（銀行口座確認完了待ちと推測）。数日待って再確認するか、Play Consoleサポートへの
   問い合わせが必要かもしれない。

## 次にやりそうなこと（未着手）

- **Issue #34「サーバーレス関数（画像認識プロキシ）の実装」**: 次に着手する想定。Issue #33の
  Play Console側ブロックとは独立に進められる。
- **Issue #33の残り**: 銀行口座確認完了待ち→Play Console定期購入商品作成→RevenueCatの
  Offering/Paywallデザイン→Webhook設定（Issue #34のCloud Functionsのエンドポイント確定後）。
- Issue #18: 〜2026-09-23頃まで、Testers Community経由のテスターが12人以上オプトインした
  状態を維持できているか定期的に確認。
- 並行着手可能: Issue #27（英語対応）・#22（テストハーネス）。
- Issue #17完了後: プライバシーポリシーの再更新（RevenueCat/Firebase Auth導入を反映）。
