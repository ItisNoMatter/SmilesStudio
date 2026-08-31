# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-01

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: SmilesParser本体実装の設計をグリリングで確定（AnyDR 0012〜0016）。実装はまだ未着手

前回セッション（2026-08-31）でcore-smilesのドメインモデル決定（0003・0004・0005・0008）を
実コードに反映し、コミット済み（`e1c1758`）。今回セッション（2026-09-01）は`/grill-with-docs`で
`SmilesParser.parse()`本体実装の設計方針をグリリングし、5件のAnyDR（0012〜0016）を記録した。
コード変更はまだ無く、次セッションはこれらの決定に沿ってTDDで実装に着手するフェーズ。

## 直近セッションでやったこと（2026-09-01）

1. `/grill-with-docs`で`SmilesParser.parse()`本体実装の設計方針を詰めた。
   - **0012**: 最初のTDD対象スコープを「直鎖+分岐」（例: `CCO`、`CC(=O)O`）に限定。
     環閉包・芳香族表記は次段階に送る。
   - **0013**: パーサー内部はTokenizer分離（文字列→トークン列→再帰下降パース）の
     二段階アーキテクチャで実装する。
   - **0014**: 角括弧原子表記（水素数指定・電荷・同位体、例: `[CH3]`、`[NH4+]`、`[13C]`）は
     今回のスコープから除外し、次のイテレーションに送る。
   - **0015**: 環閉包・芳香族のような構文的には既知だが未対応の記法に遭遇した場合、
     汎用エラーではなく専用の未対応理由を`ParseResult.Failure`で返す。
   - **0016**: `ParseResult.Failure`の型（0008）は変えず、`reason`文字列に位置情報を
     埋め込む（例: "位置5: 不明な文字 'x'"）。
2. 上記いずれもまだ実コードには反映していない（設計のみ確定、TDD未着手）。
   `SmilesParser.kt`は引き続きダミー実装のまま。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0010`: 前回までに反映済み（詳細は割愛、コード上も反映済み）。
- `0011-branch-per-large-change.md`: 通常の変更はmainに直接コミット、大きめの変更や
  AIレビュー時のみブランチを切る運用ルール。コード変更なし。
- `0012-smiles-parser-initial-scope-chain-and-branches.md`: SmilesParser初期TDDスコープを
  直鎖+分岐に限定。**未実装**。
- `0013-smiles-parser-tokenizer-separation.md`: Tokenizer分離（二段階）アーキテクチャを採用。
  **未実装**。
- `0014-defer-bracket-atom-notation.md`: 角括弧原子表記は次イテレーションへ送る決定。
  （実装対象外にする決定のため、コード変更は発生しない）
- `0015-unsupported-notation-specific-error.md`: 未対応記法には専用エラー理由を返す。
  **未実装**。
- `0016-embed-position-in-failure-reason.md`: `Failure.reason`に位置情報を埋め込む。
  **未実装**。

`CONTEXT.md`（リポジトリルート）は今回のセッションで変更なし（Implicit/Explicit Hydrogen Count、
Aromatic Atom、Aromatic Bond、Ring、AtomIdの5用語のまま。今回議論したTokenizer等は実装用語で
あり用語集の対象外と判断）。

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
    HydrogenCount.kt   sealed interface { Implicit, Explicit(count) }
    ParseResult.kt     sealed class { Success(Molecule), Failure(reason: String) }
    Atom.kt    (element, charge, isotope, hydrogenCount: HydrogenCount)
    BondType.kt (SINGLE/DOUBLE/TRIPLE/AROMATIC) / Bond.kt (変更なし)
    Molecule.kt        isAromatic(atomId)。bondsByAtom/aromaticAtomIdsをby lazyでキャッシュ
    SmilesParser.kt    parse()はParseResult.Failureのダミー実装のまま（未着手）
  src/commonTest/kotlin/com/smilestudio/core/
    MoleculeTest.kt (7件、全て日本語メソッド名、グリーン)
    SmilesParserTest.kt (1件、グリーン。「パース処理が未実装の間はFailureを返す」のみ)

ui-compose/                           # kotlin(multiplatform) + Compose Multiplatform（変更なし）
  build.gradle.kts
  src/commonMain/kotlin/com/smilestudio/ui/MoleculeCanvas.kt (空のCanvas)

desktop-app/                          # kotlin(jvm) + compose.desktop.application（変更なし）
  build.gradle.kts
  src/main/kotlin/Main.kt

docs/any-decision-record/  0001〜0016
CONTEXT.md                 5用語（Implicit/Explicit Hydrogen Count, Aromatic Atom, Aromatic Bond, Ring, AtomId）
```

## ⚠️ コードと決定のズレ

- AnyDR 0012・0013・0015・0016: 設計は確定したが、`SmilesParser.parse()`は依然として
  `ParseResult.Failure("SMILES parsing is not implemented yet")`を返すダミー実装のまま
  （`SmilesParser.kt`未変更、Tokenizer型もまだ存在しない）。次セッションでTDDにより
  実装に着手する。

## 既知の注意点（未対応・要フォローアップ）

1. `compose.runtime`等のバージョンカタログ経由アクセサがCompose Multiplatform 1.12.0で
   非推奨警告になっている。ビルドは通るが警告あり（優先度低、未着手）。
2. `Element`の元素セットは有機化学サブセット10種（H,C,N,O,F,P,S,Cl,Br,I）で仮実装。

## 次にやりそうなこと（未着手）

- AnyDR 0012〜0016に沿って`SmilesParser.parse()`をTDDで実装する（Tokenizer→再帰下降パーサー
  の順で進める想定。直鎖+分岐のみ、角括弧・環閉包・芳香族は専用エラーで拒否）。
- `MoleculeCanvas`の実際の描画ロジック（原子・結合の描画）。
- `Ring`（AnyDR 0006）の環検出アルゴリズムは、実際に必要になった時点で着手。
- Koog連携（手描き構造式 → SMILES のマルチモーダル認識）は将来タスク。
