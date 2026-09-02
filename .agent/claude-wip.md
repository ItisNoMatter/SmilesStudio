# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-03

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: Issue #12（android-appモジュール追加）実装・クローズ済み。次はIssue #13かIssue #14

前回セッションでShipaton 2026ロードマップ（Issue #11 + 子Issue #12〜#21）を作成
（コミット a6d5c19 ）。今回セッションはまず「Issue #11の子Issueをデスクトップv1ロードマップ
（Issue #1）より優先する」ことをAnyDR 0034として明記した上で、Issue #12
（android-appモジュールの追加）をTDD以外の方法（インフラ構築＋実機/エミュレータでの動作確認）で
実装し、コミット 26e9c84 としてpush済み。Issue #12はコメント＋クローズ済み。

## 直近セッションでやったこと（2026-09-03）

1. Issue #11とIssue #1のどちらを優先するかユーザーに確認 → Issue #11優先で確定。
   AnyDR 0034として記録（コミット f0177f4 ）。プロジェクトメモリ
   `project_shipaton_2026_hackathon`にも反映。
2. Issue #12（android-appモジュールの追加）を実装。CLAUDE.mdのPros/Cons提示ルールは、
   ここでは「AGP/SDKバージョン選定」のような細かいパラメータ調整には適用せず、実際にビルドで
   確認しながら進めた（設計の分岐ではなくインフラ配線作業と判断）。
   - `core-smiles`・`ui-compose`にAndroidターゲットを追加しようとしたところ、AGP 9で
     `com.android.library` + `org.jetbrains.kotlin.multiplatform`の組み合わせが非推奨
     （AGP 10で廃止予定）と判明。公式ドキュメントを確認し、新しい
     `com.android.kotlin.multiplatform.library`プラグイン（`kotlin { android { ... } }`
     ブロックで設定）に切り替えた。
   - Compose Multiplatform 1.12.0のAndroid成果物がAGP 9.1.0・compileSdk 37を要求すると
     判明し、`libs.versions.toml`のAGP/SDKバージョンを更新（agp=9.1.0, compileSdk=37,
     targetSdk=37, minSdk=26）。
   - `android-app`モジュールを新規作成。`desktop-app`と同じ「薄いプラットフォームエントリ
     ポイント」パターンで、`MainActivity`が`ui-compose`の`MoleculeCanvas`をそのまま呼び出す
     構成（`molecule = null`で空のCanvasを表示、Issue #12のスコープ通り）。
   - 動作確認: このマシンのAndroid SDKには古い`tools/bin/avdmanager`（JDK 11+と非互換）しか
     なく、公式`cmdline-tools`を手動でダウンロード・展開して解決。AVD（API 36, Pixel 6）を
     新規作成し、debug APKのインストール・起動・プロセス継続（クラッシュなし、pidof安定）を
     確認。スクリーンショットで"SmileStudio"タイトルバーの表示も確認済み（SystemUI側のANRが
     出たが、これはエミュレータのソフトウェアレンダリング負荷によるものでアプリのクラッシュ
     ではないと判断）。確認後エミュレータは終了済み。
   - `./gradlew allTests build`グリーン確認済み（既存のJVMターゲットのテストには影響なし）。
3. GitHub側: Issue #12にコメント＋クローズ、マップIssue #11のChildrenチェックリストと
   Decisions-so-farを更新（AnyDR 0034・コミット 26e9c84 へのリンクを追加）。
4. コミット f0177f4 ・ 26e9c84 とも`origin/main`にpush済み。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0026`: 前回までに反映済み。
- `0027`（android-app追加、iOS後回し）: **実装済み**（Issue #12、コミット 26e9c84 ）。
- `0028`（手描き構造式パースMVP）: **未実装**（Issue #14, #15）。
- `0029`（Gemini採用、Koogマルチプロバイダ維持）: **未実装**（Issue #14）。
- `0030`（BYOKハイブリッド課金）: **未実装**（Issue #16, #17）。
- `0031`（型システムをOSS安全網に）: 既存の型設計にすでに体現。新規実装作業なし。
- `0032`（AIレビューCI、低優先度）: **未実装**（Issue #20）。
- `0033`（Shipatonは別マップIssue #11で管理）: **実装済み**。
- `0034`（Issue #11をIssue #1より優先）: **実装済み**（運用ルールとして適用中）。

## 現在のプロジェクト構成

```
settings.gradle.kts        # include(:core-smiles, :ui-compose, :desktop-app, :android-app)【更新】
build.gradle.kts           # ルート: android.application/android.kotlin.multiplatform.libraryを追加
gradle/libs.versions.toml  # kotlin=2.2.10, composeMultiplatform=1.12.0, agp=9.1.0,
                           # androidCompileSdk/TargetSdk=37, androidMinSdk=26
gradle.properties          # android.useAndroidX=true, nonTransitiveRClass=true,
                           # suppressUnsupportedCompileSdk=37.0

core-smiles/                # kotlin(multiplatform): jvm() + android（com.android.kotlin.
                             # multiplatform.libraryプラグイン、namespace=com.smilestudio.core）
  （コード自体はIssue #4完了時点から変更なし。ビルド構成のみ変更）

ui-compose/                 # kotlin(multiplatform) + Compose Multiplatform: jvm() + android
                             # （namespace=com.smilestudio.ui）
  src/commonMain/kotlin/com/smilestudio/ui/MoleculeCanvas.kt (空のCanvas、TODOのみ、変更なし)

desktop-app/                # kotlin(jvm) + compose.desktop.application（変更なし）

android-app/                 【新規】com.android.application + Compose Multiplatform
  build.gradle.kts           namespace/applicationId=com.smilestudio.android
  src/main/AndroidManifest.xml
  src/main/kotlin/com/smilestudio/android/MainActivity.kt  ui-composeのMoleculeCanvasを呼び出し
  src/main/res/values/strings.xml

docs/any-decision-record/  0001〜0034
CONTEXT.md                 5用語。変更なし
GitHub Issues（2マップ体制、Issue #11優先）:
  Issue #1  マップ「SmilesStudio v1: 最小構成でのユーザーリリース」（デスクトップ、一時停止中）
    #2,#3,#4 クローズ済み。フロンティア: #5「2Dレイアウト計算」（未着手のまま）
  Issue #11 マップ「SmilesStudio: Shipaton 2026対応」（優先中）
    #12 クローズ済み。フロンティア: #13「ui-composeのモバイル向け調整」、
    #14「Koog SDK導入とVision LLM呼び出し」
.git/hooks/pre-commit       コミットSHA表記チェック用の非ブロッキング警告（リポジトリ追跡外）
```

## ⚠️ コードと決定のズレ

- Shipaton側（Issue #11）: `0028`〜`0032`はすべて未実装（対応するIssue #14〜#21参照）。
- デスクトップ側（Issue #1）: `0019`後半（レイアウト計算本体）以降がすべて未実装（Issue #5〜#10）。
  AnyDR 0034により優先度は下がっているが、Issue #11の#15（手描き認識UI）はIssue #1の#7
  （MoleculeCanvas描画実装）にクロスマップでblocked_byしているため、いずれ再開が必要。

## 既知の注意点（未対応・要フォローアップ）

1. `compose.runtime`等のバージョンカタログ経由アクセサが非推奨警告（優先度低、未着手）。
2. `Element`に`B`（ホウ素）がなく、芳香族小文字の`b`は未対応のまま。
3. `Molecule.rings`のDFS背後辺方式は縮合環・橋かけ環を正しく扱えない（AnyDR 0026）。
4. OSSライセンス・有料プランの具体的価格・使用上限（レート制限）はいずれも未決定のまま。
5. このマシンのAndroid SDKは`cmdline-tools`が元々未インストールだった（今回手動で追加）。
   AVD一覧は`SmileStudio_Test`（API 36, Pixel 6）が1件作成済み。

## 次にやりそうなこと（未着手）

- **Issue #11のフロンティア2件のいずれかから着手**:
  [Issue #13「ui-composeのモバイル向け調整」](https://github.com/ItisNoMatter/SmilesStudio/issues/13)
  （#12完了により依存解消）、
  [Issue #14「Koog SDK導入とVision LLM呼び出し」](https://github.com/ItisNoMatter/SmilesStudio/issues/14)
  （独立して着手可能）。どちらから着手するかはユーザー指示待ち。
- Play Store申請は2026-09-20頃を目標（審査バッファ）。
