# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-09

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: Issue #18のPlay Console非公開テストが実際に開始（14日間カウント進行中）

Issue #14（Koog/Gemini連携）・Issue #24（Safe Area対応）・Issue #25（Material 3
Expressiveデザイン）・Issue #26（ストア掲載素材）に続き、Issue #18のPlay Console側の実操作
（アプリ登録・ストア掲載情報・アプリのコンテンツ宣言・非公開テストトラック設定・テスター
確保）を完了し、2026-09-09に非公開テストが正式に開始された（AnyAR 0034）。android-appは
「ホーム」（SMILES入力→構造式描画）・「使い方」（記法解説）の2タブ構成、指定のpurple系
カラースキーム、TopAppBar/NavigationBar、独自の分子構造アイコンを備え、実機（エミュレータ）
で全機能を確認済み。プライバシーポリシーもGitHub Pagesで公開中。

テスター確保はTesters Community経由（Googleグループ`testers-community@googlegroups.com`を
Play Consoleのメーリングリストに追加済み）。14日間、12人以上のオプトイン状態を維持する
必要がある（〜2026-09-23頃まで）。

## 直近セッションでやったこと（2026-09-09、Issue #18 Play Console実操作）

*   コード変更なし（Play Console上での手動操作のみ）。当初`/wizard`スキルでbashウィザード
    スクリプトを用意したが、実行中のスクリプトファイルを編集してしまい構文エラーで停止する
    事故が発生。ユーザーの提案でウィザード方式をやめ、チャット上でスクリーンショットを
    見ながら一つずつ対話的に案内する方式に切り替えた（詳細はAnyAR 0034参照）。
*   Play Consoleでアプリを作成（パッケージ名`com.smilestudio.android`）、メインのストアの
    掲載情報・ストアの設定（カテゴリ/連絡先）・アプリのコンテンツ宣言（プライバシー
    ポリシーURL・広告なし・データセーフティ等）を入力。
*   非公開テストトラックを作成しAABをアップロード、Googレビューに送信・通過。
*   テスター確保サービスはTesters Community→決済失敗→onTest.app（Pros/Cons比較の上で選定）
    →決済失敗→SwapTest（無料の相互テスト、Pros/Cons比較の上で選定）を検討したが、実行前に
    Testers Communityの決済が最終的に通ったため、当初計画通りTesters Communityで完了。
*   Testers CommunityのGoogleグループをPlay Consoleのテスターのメーリングリストに追加し、
    非公開テストが正式に開始。AnyAR 0034を記録。

## 過去セッションでやったこと（2026-09-06、Issue #24〜#27関連）

1. Issue #24「Safe Area対応」: `/grill-with-docs`で設計（AnyDR 0079〜0083）。当初
   「TextField画面上部固定」で進めていたが、ユーザー指摘で「TextField下部固定＋IME
   パディング」に方針転換（AnyDR 0082が0081を撤回・置き換え）。実装後、`imePadding()`と
   `windowInsetsPadding(safeDrawing.only(Bottom))`の二重適用でTextFieldがキーボードより
   大きく浮くバグをユーザー報告で発見・修正（AnyAR 0029、`WindowInsets.safeDrawing`は
   既に`ime`を含むため片方で足りることが原因）。コミット `449a228` ・ `4256992` 。
2. Issue #25「Material 3 Expressiveデザイン」: ユーザー提示の詳細仕様に基づき実装
   （コミット `492c35e` ）。JetBrains Compose Multiplatformのmaterial3
   （`composeMultiplatform`バンドルの1.12.0-alpha03）では`MaterialExpressiveTheme`が
   まだ`internal`で使えず、android-appはAndroid専用スコープのためAndroidX本家の
   material3アルファ（1.5.0-alpha27）に直接依存する形で解決。実装後、「タップ時の
   軽い縮小フィードバック」が実は機能していなかったバグ（`expressivePressScale()`の
   `interactionSource`が対象コンポーネントと共有されておらず、押下イベントを一切
   観測できていなかった）を発見・修正（AnyAR 0031、コミット `1b583f2` ）。
3. Issue #18を`/grill-with-docs`で設計（AnyDR 0084〜0090）:
   - 初回アップロードはmainブランチそのまま（#24・#25含む、AnyDR 0084）
   - テスター確保は有料サービス「Testers Community」を利用（AnyDR 0085・0086）
   - ストア掲載素材（アイコン・プライバシーポリシー・スクリーンショット）が一切
     存在しないことが判明し、Issue #26として独立させ#18の`blocked_by`に設定
     （AnyDR 0087）
   - プライバシーポリシーはGitHub Pagesでホスト（AnyDR 0088）
   - アイコンはClaudeがSVGで作成（AnyDR 0089）
   - 英語対応は今回の提出に含めず、Play Consoleの14日カウントはアプリ更新で
     リセットされないことを確認した上で、非公開テストと並行するIssue #27として
     別進行に（AnyDR 0090）
4. Issue #26「ストア掲載素材」を実施（AnyAR 0032）: 分子構造モチーフのアダプティブ
   アイコンをSVGで作成、Android Studio「Image Asset Studio」で全サイズ・Play Store用
   512x512アイコンを生成（コミット `281119f` 、副次的に`app_name`の表記ゆれ
   「SmileStudio」→「SmilesStudio」も修正）。プライバシーポリシーを
   `docs/privacy-policy/index.html`に作成しGitHub Pagesで公開
   （https://itisnomatter.github.io/SmilesStudio/privacy-policy/ 、コミット `4714dc7` ）。
   ストア用スクリーンショット2枚を`docs/store-assets/screenshots/`に保存
   （コミット `fa3eac5` ）。Issue #26をclose。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0078`: 前回までに反映済み（詳細は割愛）。
- `0079`〜`0083`（Safe Area設計、途中でTextField位置を撤回・変更）: **実装済み**
  （Issue #24）。
- `0084`（Play Console初回アップロードは#24・#25を除外しない）: **実装済み**（Issue #18）。
- `0085`・`0086`（テスター確保は有料サービス、Testers Community使用）: **実行済み**
  （Issue #18、onTest.app/SwapTestを一時検討したが最終的に当初計画通りTesters Communityで
  実行。詳細はAnyAR 0034）。
- `0087`（ストア掲載素材を独立Issue化）・`0088`（プライバシーポリシーはGitHub Pages）・
  `0089`（アイコンはClaudeがSVGで作成）: **実装済み**（Issue #26）。
- `0090`（英語対応はIssue #18と並行する別Issue #27で進める）: **Issue化のみ**
  （実装未着手）。

## 現在のプロジェクト構成

```
core-smiles/, ui-compose/（MoleculeCanvas.kt, MoleculeDrawing.kt, MoleculeEditor.kt）:
  前回までと変更なし（詳細は過去のWIPノート参照）

vision-recognition/: 前回までと変更なし

android-app/src/main/
  AndroidManifest.xml    android:icon/roundIconを追加
  MainActivity.kt        SmilesStudioTheme { Surface { SmilesStudioApp() } }
  SmilesStudioApp.kt     【新規】Scaffold（TopAppBar+NavigationBar）、ホーム/使い方タブの
                         AnimatedContent切り替え、more_vertメニュー、タップ時の縮小
                         フィードバック（expressivePressScale、全タップ要素に配線済み）
  HomeContent.kt         【新規】MoleculeCanvas＋OutlinedTextField（resolveMoleculeEditorState
                         を再利用、MoleculeEditor.ktとは別実装）
  HowToContent.kt        【新規】SMILES記法6例をライブプレビュー付きで一覧表示
  theme/Theme.kt         【新規】MaterialExpressiveTheme＋指定カラースキーム
  theme/PressScale.kt    【新規】expressivePressScale() Modifier（interactionSource必須引数）
  res/drawable/ic_launcher_{background,foreground}.xml  【新規】分子構造モチーフのベクター
  res/mipmap-*/          【新規】アダプティブアイコン各サイズ・モノクロレイヤー
  res/values/strings.xml app_nameを「SmilesStudio」に修正
  ic_launcher-playstore.png  【新規】Play Store掲載用512x512アイコン（build対象外、素材のみ）

build.gradle.kts（android-app）:
  androidx.compose.material3:material3:1.5.0-alpha27 に直接依存（JetBrains CMPのmaterial3
  ではMaterialExpressiveThemeがinternalなため）。compose.materialIconsExtendedも追加。

docs/
  any-decision-record/  0001〜0090（en/は一部のみオンデマンド生成、欠番は正常）
  any-action-record/    0001〜0032（en/は一部のみ）
  privacy-policy/index.html  【新規】GitHub Pagesで公開中
  store-assets/screenshots/  【新規】ホーム・使い方タブのスクリーンショット2枚
  .nojekyll              【新規】GitHub PagesのJekyll処理を無効化

GitHub Pages: main branch /docs から配信、有効化済み・ビルド確認済み
  https://itisnomatter.github.io/SmilesStudio/privacy-policy/

keystore.properties, ~/.smilestudio-keys/upload-keystore.jks
  gitignore対象・リポジトリ外、このマシンにのみ存在（変更なし）

GitHub Issues（2マップ体制）:
  Issue #1  マップ「SmilesStudio v1」: #2〜#8クローズ済み。フロンティア: #9→#10
  Issue #11 マップ「Shipaton 2026」（子Issue14件、#24〜#27を今セッションで追加）:
    #12,#13,#14,#24,#25,#26 クローズ済み。#18は非公開テスト実行中（未close、14日間経過待ち）。
    フロンティア: #15,#16,#17,#22（Phase 2、未着手）、#27（英語対応、非公開テストと並行進行）
GitHubマイルストーン: Phase 1（期限2026-09-08、経過）。#18の非公開テストは2026-09-09開始、
  〜2026-09-23頃まで12人以上のオプトイン維持が必要。Phase 2（期限2026-09-22）。
```

## ⚠️ コードと決定のズレ

- Issue #27（英語対応）: Issueは作成したが実装は未着手。全UI文字列が`strings.xml`化
  されておらずKotlinコードに直書きのまま。
- Issue #15（手描き認識UI）・#16（BYOK設定画面）・#17（RevenueCat課金）: 依存解消済みだが
  未着手のまま。`0036`（B/C課金プラン方針）は未反映。
- `0037`のテストハーネス層（三層防御OSS戦略）→ Issue #22: 未実装。

## 既知の注意点（未対応・要フォローアップ）

1. レート制限の判定はKoogの例外メッセージの文字列マッチングに依存する脆い実装（AnyDR 0078）。
   JetBrains/koogのYouTrack [KG-652](https://youtrack.jetbrains.com/issue/KG-652)にユーザーが
   コメント投稿済み（現状の回避策を共有）。解決されれば置き換え候補。
2. `Element`に`B`（ホウ素）がなく、芳香族小文字の`b`は未対応のまま。
3. `Molecule.rings`のDFS背後辺方式・`computeLayout`の固定角度配置は縮合環・橋かけ環を正しく
   扱えない（v1スコープでは問題ない）。
4. 有料プランの具体的価格・使用上限（レート制限）は未決定のまま。
5. `ai.koog:prompt-executor-google-client`は`koog-agents`本体（1.2.0安定版）とは独立
   バージョニングでまだbeta（1.1.1-beta）。
6. android-appは`androidx.compose.material3:material3:1.5.0-alpha27`という不安定版
   （Expressive API公開待ち）に直接依存している。将来安定版で`MaterialExpressiveTheme`が
   公開されたら、JetBrains CMP側のmaterial3に戻すか検討の余地あり。
7. このマシンのAndroid SDKは`D:\Android\Sdk`（`GRADLE_USER_HOME`も`D:\Android\.gradle`）。
   AVD`SmileStudio_Test`（API 36）が1件作成済み。エミュレータは長時間セッションで
   メモリ逼迫（swap多用）しやすく、ANRや起動遅延が起きることがある。

## 次にやりそうなこと（未着手）

- **Issue #18の残り**: 14日間（〜2026-09-23頃）、Testers Community経由のテスターが
  12人以上オプトインした状態を維持できているか定期的に確認。維持できたら本番アクセス申請
  （Issue #23、未作成）に進む。Claude側では代行不可、ユーザー主導。
- 並行着手可能: Issue #27（英語対応）・#15（手描き認識UI）・#16（BYOK設定画面）・#17
  （RevenueCat課金）。
- 14日間経過後: 本番アクセス申請（Issue #23）→ Devpost提出（Issue #19）。
