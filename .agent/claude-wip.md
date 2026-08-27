# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-08-27

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: プロジェクト基盤構築 完了

CLAUDE.mdの定義に従い、`core-smiles` / `ui-compose` / `desktop-app` の3モジュール構成
(Kotlin Multiplatform + Compose Multiplatform)でのプロジェクト基盤構築が完了した。
`./gradlew build` はグリーン。

## 確定した決定事項（AnyDRに記録済み）

- `docs/any-decision-record/0001-core-smiles-id-based-domain-model.md`:
  core-smilesのドメインモデルをIDベース設計（AtomId value class + Map<AtomId, Atom>）で採用。
- `docs/any-decision-record/0002-kmp-module-structure-core-smiles-ui-compose.md`:
  core-smiles・ui-compose両方を`kotlin("multiplatform")`の真のKMPモジュールとして構成
  （jvm()ターゲットのみで開始）。desktop-appはプレーンなJVMエントリポイント。

## 既存app/モジュールについて

Android Studioデフォルトの`app/`（`com.example.smilestudio`）は**完全削除済み**。バックアップは
残していない（ユーザーが完全削除を選択）。

## 現在のプロジェクト構成

```
settings.gradle.kts        # include(:core-smiles, :ui-compose, :desktop-app)
build.gradle.kts           # ルート: 各プラグインをapply falseで宣言
gradle/libs.versions.toml  # kotlin=2.2.10, composeMultiplatform=1.12.0

core-smiles/                          # kotlin(multiplatform), jvm()ターゲットのみ
  build.gradle.kts
  src/commonMain/kotlin/com/smilestudio/core/
    Element.kt (enum, 有機化学でよく使う元素のサブセット: H,C,N,O,F,P,S,Cl,Br,I)
    AtomId.kt (@JvmInline value class)
    Atom.kt / BondType.kt / Bond.kt / Molecule.kt (AnyDR 0001の設計そのまま)
    SmilesParser.kt (parse()はTODO()の空実装)
  src/commonTest/kotlin/com/smilestudio/core/MoleculeTest.kt (2件、グリーン)

ui-compose/                           # kotlin(multiplatform) + Compose Multiplatform
  build.gradle.kts                    # core-smilesへの依存はapi()(公開APIにMoleculeが露出するため)
  src/commonMain/kotlin/com/smilestudio/ui/MoleculeCanvas.kt (空のCanvas)

desktop-app/                          # kotlin(jvm) + compose.desktop.application
  build.gradle.kts                    # mainClass = "MainKt"
  src/main/kotlin/Main.kt             # Windowを開きMoleculeCanvasを表示するだけの最小実装
```

## 既知の注意点（未対応・要フォローアップ）

1. ~~CLAUDE.mdの頻出コマンド `./gradlew test` はKMPモジュールのテストを実行しない~~
   → **解消済み**。CLAUDE.mdの「重要」節・「頻出コマンド」節を`./gradlew allTests` /
   `./gradlew :core-smiles:jvmTest`に修正し、`./gradlew test`では
   core-smiles/ui-composeのテストが実行されない旨を注記した。

2. `compose.runtime` / `compose.foundation` / `compose.material3` / `compose.ui` の
   バージョンカタログ経由アクセサ(`compose.xxx`)がCompose Multiplatform 1.12.0で
   非推奨警告（deprecation warning）になっている。ビルドは通るが警告が出る。
   将来的に`org.jetbrains.compose.material3:material3:<version>`のような直接の
   artifact座標に置き換えることを検討してもよい（優先度低、未着手）。

3. `Element`の元素セットは有機化学サブセット10種（H,C,N,O,F,P,S,Cl,Br,I）で仮実装。
   将来的に拡張が必要になったら追加する。

## 次にやりそうなこと（未着手）

- `SmilesParser.parse()`の実装（TDDで進める：まずパーステスト → Red → Green）。
- `MoleculeCanvas`の実際の描画ロジック（原子・結合の描画）。
- Koog連携（手描き構造式 → SMILES のマルチモーダル認識）は将来タスク。
