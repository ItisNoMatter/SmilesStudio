# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-01

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: SmilesParser本体（直鎖+分岐）をAnyDR 0012〜0016に沿ってTDD実装完了

前回セッション（2026-09-01前半）で`/grill-with-docs`により`SmilesParser.parse()`本体実装の
設計方針を確定（AnyDR 0012〜0016）。今回セッション（2026-09-01後半）はその決定に沿って
Tokenizer→再帰下降パーサーの順でTDD実装し、`./gradlew allTests`・`./gradlew build`とも
グリーン。コミットはまだ未実施（作業ツリーに変更あり）。

## 直近セッションでやったこと（2026-09-01後半）

1. Tokenizerを新規実装（TDD、Red→Green）。
   - `Token.kt`: `AtomSymbol(element)` / `BondSymbol(bondType)` / `LParen` / `RParen`の
     sealed interface。
   - `PositionedToken.kt`: トークンと文字列上の開始位置(`position: Int`)の組。
   - `TokenizeResult.kt`: `Success(tokens)` / `Failure(reason)`のsealed class。
   - `Tokenizer.kt`: 有機サブセット文字（C,N,O,F,P,S,H,Cl,Br,I。2文字元素Cl/Brは
     優先的にマッチ）、結合記号(`=`,`#`,`-`)、括弧をトークン化。環閉包数字・芳香族小文字
     ・角括弧は0015に従い専用の未対応理由（例: "位置1: 環閉包表記は未対応です"）を返す。
   - `TokenizerTest.kt`: 11件、全てグリーン。
2. `SmilesParser.parse()`を再帰下降パーサーとして実装（TDD、Red→Green）。ダミー実装
   （常に`Failure`を返す）を置き換えた。
   - Tokenizerの結果を消費し、現在の"接続先原子"(`currentAtom`)と分岐スタック
     (`branchStack`：`(親AtomId, 開き括弧の位置)`のペア)で直鎖+分岐の構造を組み立てる。
   - 結合記号省略時は`BondType.SINGLE`。
   - 異常系はすべて0016に従い`reason`文字列に位置情報を埋め込んで`Failure`を返す
     （不明文字はTokenizerの理由をそのまま透過、結合記号の連続・空文字列・
     結合記号の前後に原子がない・開き/閉じ括弧の不整合など）。
   - `SmilesParserTest.kt`: 旧来のダミー実装用テスト（「パース処理が未実装の間はFailureを
     返す」）は実際の挙動テストに置き換えた。12件、全てグリーン（`CCO`のエタノール、
     `CC(=O)O`の酢酸を含む、AnyDR 0012記載の例に対応）。
3. `./gradlew allTests`・`./gradlew build`を実行しグリーンを確認済み。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0010`: 前回までに反映済み（詳細は割愛、コード上も反映済み）。
- `0011-branch-per-large-change.md`: 通常の変更はmainに直接コミット、大きめの変更や
  AIレビュー時のみブランチを切る運用ルール。コード変更なし。
- `0012-smiles-parser-initial-scope-chain-and-branches.md`: SmilesParser初期TDDスコープを
  直鎖+分岐に限定。**実装済み**。
- `0013-smiles-parser-tokenizer-separation.md`: Tokenizer分離（二段階）アーキテクチャを採用。
  **実装済み**（`Tokenizer.kt` + `SmilesParser.kt`の再帰下降部）。
- `0014-defer-bracket-atom-notation.md`: 角括弧原子表記は次イテレーションへ送る決定。
  **実装済み**（`[`検出時に専用Failureを返す。実際のパースは未対応のまま次段階へ）。
- `0015-unsupported-notation-specific-error.md`: 未対応記法には専用エラー理由を返す。
  **実装済み**（Tokenizerが環閉包数字・芳香族小文字・角括弧をそれぞれ専用理由で拒否）。
- `0016-embed-position-in-failure-reason.md`: `Failure.reason`に位置情報を埋め込む。
  **実装済み**（Tokenizer/パーサー双方のエラーメッセージに"位置N: ..."を埋め込み）。

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
    Token.kt           【新規】sealed interface { AtomSymbol, BondSymbol, LParen, RParen }
    PositionedToken.kt 【新規】data class(token, position: Int)
    TokenizeResult.kt  【新規】sealed class { Success(tokens), Failure(reason) }
    Tokenizer.kt        【新規】文字列→PositionedTokenリスト。環閉包/芳香族/角括弧は専用理由で拒否
    SmilesParser.kt    【実装】parse()はTokenizer+再帰下降で直鎖+分岐のMoleculeを構築
  src/commonTest/kotlin/com/smilestudio/core/
    MoleculeTest.kt (7件、全て日本語メソッド名、グリーン)
    TokenizerTest.kt 【新規】11件、グリーン
    SmilesParserTest.kt (12件、グリーン。ダミー実装用の旧テストは実挙動テストに置き換え済み)

ui-compose/                           # kotlin(multiplatform) + Compose Multiplatform（変更なし）
  build.gradle.kts
  src/commonMain/kotlin/com/smilestudio/ui/MoleculeCanvas.kt (空のCanvas)

desktop-app/                          # kotlin(jvm) + compose.desktop.application（変更なし）
  build.gradle.kts
  src/main/kotlin/Main.kt

docs/any-decision-record/  0001〜0016
CONTEXT.md                 5用語（Implicit/Explicit Hydrogen Count, Aromatic Atom, Aromatic Bond, Ring, AtomId）
```

## 既知の注意点（未対応・要フォローアップ）

1. `compose.runtime`等のバージョンカタログ経由アクセサがCompose Multiplatform 1.12.0で
   非推奨警告になっている。ビルドは通るが警告あり（優先度低、未着手）。
2. `Element`の元素セットは有機化学サブセット10種（H,C,N,O,F,P,S,Cl,Br,I）で仮実装。

## 次にやりそうなこと（未着手）

- 今回の変更（Tokenizer新設 + SmilesParser本体実装）はまだコミットしていない。次セッション
  冒頭 or ユーザー指示待ちでコミットする。
- 次のSMILES文法イテレーション: 環閉包記法（`C1CCCCC1`）または芳香族小文字表記
  （`c1ccccc1`）への対応。現状は両方ともTokenizerが専用理由で拒否するのみ。どちらを先に
  対応するかは未決定（次回グリリング対象）。
- 角括弧原子表記（`[CH3]`、`[NH4+]`、`[13C]`）への対応（AnyDR 0014で次イテレーション送り）。
  `HydrogenCount.Explicit`の実パースはこれが来るまで未検証。
- `MoleculeCanvas`の実際の描画ロジック（原子・結合の描画）。
- `Ring`（AnyDR 0006）の環検出アルゴリズムは、実際に必要になった時点で着手。
- Koog連携（手描き構造式 → SMILES のマルチモーダル認識）は将来タスク。
