# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-06

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: 最小ビルドの核体験（テキスト入力→構造式描画）が実機確認済み。Issue #8のclose判断が最優先

Shipatonロードマップの核となる「テキストでSMILESを入力すると構造式が描画される」体験が、
Android・デスクトップ両方で実機（エミュレータ）確認済み。Issue #14（Koog SDK・Gemini Vision LLM
連携）はclose済み。Issue #18（Android署名設定）はアプリ側の準備が完了し、Play Console側の
手作業（アプリ登録・テスター確保）が残っている。

**⚠️ 最優先で確認すること**: Issue #8「desktop-app: SMILES入力欄とパースエラー表示の実装」が
GitHub上まだOPENのままだが、今セッションで実装した`MoleculeEditor`（desktop-app/android-app
両方に配線済み）で実質的にスコープを満たしている。close判断がまだユーザーに確認されていない。

## 直近セッションでやったこと（2026-09-03〜2026-09-06）

1. TDDワークフローを5ステップに再定義し、ステップごとの確認を撤廃（AnyDR 0065〜0069、
   コミット `fc30e9f` ・ `fe51855` ）。
2. Issue #6「core-smiles: 芳香族結合のKekulize変換」をTDDで実装・クローズ（コミット `49522cb` ）。
3. Issue #7「ui-compose: MoleculeCanvasの描画実装」を2段階（描画計画の純粋関数化→実際の
   Canvas描画）で完了・クローズ（AnyDR 0067・0068、コミット `9fb3785` ・ `6b43e18` ）。目視確認で
   原子ラベルと結合線が重なるバグを発見・修正。
4. Issue #13「ui-composeのモバイル向け調整」をレスポンシブ自動フィットスケーリングとして実装・
   クローズ（AnyDR 0071、コミット `d0851b2` ）。
5. Issue #14「Koog SDK導入とVision LLM呼び出し」をTDDで実装、`vision-recognition`モジュール
   新設（AnyDR 0072〜0076、コミット `a23e49a` ）。close判断の棚卸しで、AnyDR 0073の設計
   （Failureはネットワークエラー等を表現する）と実装の乖離（例外が素通しで伝播していた）を
   発見し、例外ハンドリングとレート制限の明示的な検知を追加実装（AnyDR 0077・0078、コミット
   `833a62d` ）。Koogの`LLMClientException`はHTTPステータス/ヘッダーを公開しないため、
   レート制限はメッセージ文字列マッチングで検知する設計とし、JetBrains/koogのYouTrack
   [KG-652](https://youtrack.jetbrains.com/issue/KG-652)にコメントを投稿。Issue #14をclose。
6. Issue #18の署名設定に進む前に「実際にユーザーが操作できるレベルか」を確認したところ、
   `android-app`・`desktop-app`のどちらにも、SMILES文字列を入力する手段（`TextField`等）が
   リポジトリ全体に一つも存在しないことを発見（`MoleculeCanvas(molecule = null, ...)`を
   直書きしているだけだった）。ui-composeに`MoleculeEditor`（状態を呼び出し側にホイストする
   設計、AnyDR 0028が前提とするIssue #15の将来連携を見据えて選定）をTDDで新規実装し、両アプリに
   配線（コミット `8a7883a` ）。エミュレータでの実機確認中、`AndroidManifest.xml`にテーマ指定が
   なくデフォルトActionBarがCompose UIの先頭（TextField）を覆い隠す別バグを発見・修正
   （`NoActionBar`テーマを追加）。ベンゼン環の描画・不正入力時のエラー表示（直前の描画は保持）を
   実機で確認済み。
7. Issue #18向けにandroid-appのリリース署名設定を実施: `build.gradle.kts`に
   `keystore.properties`がある場合のみ署名する条件付き設定を追加（コミット `5f9ba9d` ）。
   `/wizard`スキルでキーストア生成スクリプトを作成・実行（パスワードはClaude側からは不可視）。
   Git Bash特有の2つの環境問題（`keytool`がPATHに無い、`keystore.properties`の`storeFile`が
   POSIX形式のパスでWindowsネイティブのGradleに認識されない）に遭遇し解決。`jarsigner -verify`
   で署名済みAABを検証済み。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0064`: 前回までに反映済み（詳細は割愛）。
- `0065`〜`0069`（TDDワークフロー5ステップ化、ステップごと確認の撤廃）: **実装済み**（運用ルール、
  コード変更なし）。
- `0067`・`0068`（描画計画の純粋関数化、骨格式ラベル方針）: **実装済み**（Issue #7）。
- `0070`（AnyAR5件おきのツイート候補提案）: **実装済み**（運用ルール）。
- `0071`（MoleculeCanvasはレスポンシブ対応のみ、グラフィカル編集エディタは不採用）:
  **実装済み**（Issue #13）。
- `0072`〜`0076`（vision-recognitionモジュール設計: 新規モジュール配置、RecognitionResult型、
  LLMProvider enum、ロジックとプロバイダ配線の分離、モジュール名）: **実装済み**（Issue #14）。
- `0077`・`0078`（LLM呼び出し例外のFailure変換、レート制限のメッセージマッチング検知）:
  **実装済み**（Issue #14クローズ後の追補）。

## 現在のプロジェクト構成

```
core-smiles/src/commonMain/kotlin/com/smilestudio/core/
  SmilesParser.kt, Tokenizer.kt, ParseResult.kt, TokenizeResult.kt  トークナイズ＋パース
  Molecule.kt, Atom.kt, AtomId.kt, Bond.kt, BondType.kt, Element.kt, HydrogenCount.kt  化学モデル
  Ring.kt          環検出（DFS背後辺方式、縮合環は対象外）
  Layout.kt        computeLayout(molecule): 2Dレイアウト（鎖=ジグザグ、分岐=Y字、環=正多角形）
  Kekulize.kt      芳香族結合のKekulize変換（Issue #6）
  Point2D.kt       座標のdata class

ui-compose/src/commonMain/kotlin/com/smilestudio/ui/
  MoleculeDrawing.kt   planMoleculeDrawing(molecule): DrawCommandのリストを計算する純粋関数
  MoleculeCanvas.kt    Composable。DrawCommandを実際にCanvas APIで描画。レスポンシブ自動フィット
  MoleculeEditor.kt    【新規】Composable。TextField＋エラー表示＋MoleculeCanvas。状態は呼び出し側
                       にホイスト。resolveMoleculeEditorState()が純粋関数としてテスト可能

vision-recognition/src/commonMain/kotlin/com/smilestudio/vision/  【新規モジュール】(jvm+android)
  RecognitionResult.kt   sealed class Success(smiles)/Failure(reason)
  LLMProvider.kt         enum（現状GOOGLE_GEMINIのみ）
  RunRecognition.kt      runRecognition(): Koog呼び出し・例外→Failure変換・レート制限検知
  RecognizeStructure.kt  recognizeStructure(): 薄いプロバイダ配線アダプタ

android-app/src/main/
  AndroidManifest.xml         【修正】NoActionBarテーマを追加（Compose UIを覆い隠すバグの修正）
  MainActivity.kt             MoleculeEditorを配線、状態はremember { mutableStateOf("") }
  build.gradle.kts            【修正】keystore.properties があれば条件付きでrelease署名

desktop-app/src/main/kotlin/Main.kt   MoleculeEditorを配線（android-appと同じ構造）

docs/any-decision-record/  0001〜0078
                            en/  0028, 0062, 0066がオンデマンド生成済み（欠番あり、正常）
docs/any-action-record/    0001〜0027
                            en/  0005, 0025がオンデマンド生成済み（欠番あり、正常）

keystore.properties, ~/.smilestudio-keys/upload-keystore.jks
  【新規、gitignore対象・リポジトリ外】リリース署名用。このマシンにのみ存在、バックアップ未実施

GitHub Issues（2マップ体制）:
  Issue #1  マップ「SmilesStudio v1: 最小構成でのユーザーリリース」
    #2,#3,#4,#5,#6,#7 クローズ済み。フロンティア: #8（未close判断、下記参照）→#9→#10
  Issue #11 マップ「SmilesStudio: Shipaton 2026対応」（子Issue12件）
    #12,#13,#14 クローズ済み。フロンティア: #15,#16,#17（依存解消済み・未着手）、#18（進行中）
GitHubマイルストーン: Phase 1（期限2026-09-08）残りは#18のみ。Phase 2（期限2026-09-22）。
```

## ⚠️ コードと決定のズレ

- **Issue #8「desktop-app: SMILES入力欄とパースエラー表示の実装」がGitHub上まだOPEN**だが、
  今セッションで実装した`MoleculeEditor`（desktop-app/android-app両方に配線済み）で実質的に
  スコープを満たしている。close判断がまだユーザーに確認されていない。次セッションの最優先確認
  事項。
- Issue #15（手描き認識UI）は#7完了により依存は解消済みだが、#14 close後もまだ未着手。
- Issue #16（BYOK設定画面）・#17（RevenueCat課金）も#14 close済みで着手可能だが未着手。
  `0036`（B/C課金プラン方針）はまだコードに反映されていない。
- `0037`のテストハーネス層（三層防御OSS戦略）→ Issue #22: 未実装。

## 既知の注意点（未対応・要フォローアップ）

1. レート制限の判定はKoogの例外メッセージの文字列マッチングに依存する脆い実装（AnyDR 0078）。
   JetBrains/koogのYouTrack [KG-652](https://youtrack.jetbrains.com/issue/KG-652)が解決されれば
   置き換え候補。
2. `MoleculeEditor`のTextField、Material3デフォルトスタイルだと未フォーカス時の視覚コントラスト
   が低く存在に気づきにくい（機能面は問題ないが要UI磨き込み）。
3. `Element`に`B`（ホウ素）がなく、芳香族小文字の`b`は未対応のまま。
4. `Molecule.rings`のDFS背後辺方式・`computeLayout`の固定角度配置は縮合環・橋かけ環を正しく
   扱えない（v1スコープでは問題ない）。
5. 有料プランの具体的価格・使用上限（レート制限）は未決定のまま。
6. `ai.koog:prompt-executor-google-client`は`koog-agents`本体（1.2.0安定版）とは独立バージョニング
   でまだbeta（1.1.1-beta）。
7. このマシンのAndroid SDKは`D:\Android\Sdk`（`GRADLE_USER_HOME`も`D:\Android\.gradle`）。
   AVD`SmileStudio_Test`（API 36）が1件作成済み。

## 次にやりそうなこと（未着手）

- **最優先**: Issue #8をcloseすべきか確認する（`MoleculeEditor`で実質実装済み）。
- Issue #18の残り: Google Play Console側の作業（アプリ登録・非公開テストトラック設定・
  テスター12人以上の確保・14日間運用）— Claude側では代行不可、ユーザー主導。
- 並行着手可能: Issue #15（手描き認識UI）・#16（BYOK設定画面）・#17（RevenueCat課金）。
- Phase 1マイルストーン期限は2026-09-08（あと2日）。
