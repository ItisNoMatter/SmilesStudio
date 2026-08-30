# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-08-31

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: ドメインモデリング（グリリング）完了、実装への反映が未着手

プロジェクト基盤構築（3モジュール構成）は完了済み。その後`/grill-with-docs`で
`core-smiles`のドメインモデルを詰め、`CONTEXT.md`と複数のAnyDRを追加した。
**ただし、決定した設計はまだ`core-smiles`の実コードに反映されていない**
（下記「⚠️ コードと決定のズレ」を参照）。次のセッションはここから着手する。

## 直近セッションでやったこと（2026-08-29〜2026-08-31）

1. `/mattpocock-skills:setup-matt-pocock-skills`でエンジニアリングスキル基盤を構築。
   - `CLAUDE.md`に`## Agent skills`セクションを追加。
   - `docs/agents/issue-tracker.md`（GitHub Issues + `gh` CLI運用）、
     `docs/agents/domain.md`（単一コンテキスト構成のCONTEXT.md/ADR運用ルール）を追加。
   - `triage`スキルは未インストールのためトリアージラベル関連は未設定。
2. `/grill-with-docs`（後述のカスタム版に改造する前のupstream版）で
   `core-smiles`のドメインモデル全般をテーマにグリリングセッションを実施。
   Q1〜Q6の設計ツリーを全て決着させ、`CONTEXT.md`を新規作成、
   `docs/any-decision-record/0003`〜`0009`を記録した（詳細は次節）。
3. 上記をコミット＆プッシュ済み（コミット`9f92ff0`、`origin/main`に反映済み）。
4. ユーザーからのフィードバック（一問一答形式で進める／実装選択は必ずPros-Cons形式で
   提示する／回答直後にAnyDR記録を確認する）を受けて、`~/.claude/skills/grill-with-docs/`
   （グローバル、プロジェクト外）にカスタム版`/grill-with-docs`を作成。upstreamの
   `grilling`/`domain-modeling`は実際にSkillツールで呼び出す委譲方式を維持しつつ、
   フォーマット面だけ上書きしている。**このファイル自体はリポジトリ外にあるため、
   このプロジェクトの一部としてコミットはされていない。**

CLAUDE.mdの定義に従い、`core-smiles` / `ui-compose` / `desktop-app` の3モジュール構成
(Kotlin Multiplatform + Compose Multiplatform)でのプロジェクト基盤構築が完了した。
`./gradlew build` はグリーン。

## 確定した決定事項（AnyDRに記録済み）

- `docs/any-decision-record/0001-core-smiles-id-based-domain-model.md`:
  core-smilesのドメインモデルをIDベース設計（AtomId value class + Map<AtomId, Atom>）で採用。
- `docs/any-decision-record/0002-kmp-module-structure-core-smiles-ui-compose.md`:
  core-smiles・ui-compose両方を`kotlin("multiplatform")`の真のKMPモジュールとして構成
  （jvm()ターゲットのみで開始）。desktop-appはプレーンなJVMエントリポイント。
- `docs/any-decision-record/0003-atom-hydrogen-count-sealed-interface.md`:
  `Atom.hydrogenCount`を`Int?`から`sealed interface HydrogenCount { Implicit, Explicit(count) }`に
  変更する（**未実装**、下記ズレ参照）。
- `docs/any-decision-record/0004-derive-aromaticity-from-bonds.md`:
  `Atom.isAromatic`フィールドを廃止し、芳香族性は隣接`Bond`が全て`BondType.AROMATIC`かどうかから
  導出する（**未実装**、下記ズレ参照）。
- `docs/any-decision-record/0005-cache-aromaticity-with-lazy.md`:
  芳香族性の導出計算は`Molecule`の`by lazy`プロパティでキャッシュする（**未実装**）。
- `docs/any-decision-record/0006-ring-as-derived-domain-term.md`:
  `Ring`をドメイン用語として定義。`Molecule`には保存せず、グラフから導出するクエリ時の概念とする
  （用語定義のみ、実装（環検出アルゴリズム）はまだ不要・未着手）。
- `docs/any-decision-record/0007-atomid-stability-undefined.md`:
  `AtomId`はパース結果内で一意であることのみ保証し、編集をまたいだ安定性は未定義と
  `CONTEXT.md`に明記（実装変更なし、ドキュメントのみ）。
- `docs/any-decision-record/0008-smiles-parser-result-type.md`:
  `SmilesParser.parse`の戻り値を`sealed class ParseResult { Success(Molecule); Failure(reason) }`
  にする（**未実装**、下記ズレ参照）。
- `docs/any-decision-record/0009-defer-canonical-smiles-writer.md`:
  `Molecule`→SMILES文字列への逆変換（canonical writer）は当面スコープ外。

`CONTEXT.md`（リポジトリルート）にはImplicit/Explicit Hydrogen Count、Aromatic Atom、
Aromatic Bond、Ring、AtomIdの5用語を記録済み。

## ⚠️ コードと決定のズレ（次にやる作業）

グリリングセッションでの決定（0003・0004・0005・0008）は、CLAUDE.mdのTDDサイクル
（Test First → Red → Green → Refactor）に沿って実装するために意図的にコード変更を
まだ行っていない。現在の`core-smiles`の実コードは**古い設計（0001時点）のまま**：

- `Atom.kt`: `hydrogenCount: Int?`, `isAromatic: Boolean`のまま（0003・0004未反映）
- `SmilesParser.kt`: `fun parse(smiles: String): Molecule`のまま、`TODO()`（0008未反映）
- `MoleculeTest.kt`: 古いフィールド（`Int?`/`Boolean`）を前提にしたテストのまま

次にこのプロジェクトに着手する際は、まず`docs/any-decision-record/0003`〜`0008`を読み、
CLAUDE.mdのTDDサイクルに従って
1. `HydrogenCount`sealed interfaceのテスト → Red → Green
2. `Atom.isAromatic`廃止・`Molecule`への導出プロパティ移設のテスト → Red → Green
3. `ParseResult`sealed classへの`SmilesParser`シグネチャ変更のテスト → Red → Green

の順で反映していく想定（優先順位は未確定、着手時に相談）。

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

- 上記「⚠️ コードと決定のズレ」に記載のTDD実装（0003・0004・0005・0008の反映）。
- `SmilesParser.parse()`本体の実装（`ParseResult`化と合わせてTDDで進める）。
- `MoleculeCanvas`の実際の描画ロジック（原子・結合の描画）。
- Koog連携（手描き構造式 → SMILES のマルチモーダル認識）は将来タスク。
