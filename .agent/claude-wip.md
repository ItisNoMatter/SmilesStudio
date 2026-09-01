# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-01

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: Issue #2（環閉包記法対応）実装・クローズ済み。次はIssue #3（芳香族小文字表記）

前々回セッションでv1ロードマップをグリリングで確定しAnyDR 0017〜0023・GitHub Issue #1〜#10を作成
（コミット`8aa21e4`）。今回セッションはWayfinderの唯一のフロンティアだった
[Issue #2](https://github.com/ItisNoMatter/SmilesStudio/issues/2)（SMILESパーサー: 環閉包記法
への対応）をTDDで実装し、コミット`0f2cfbd`としてpush済み。Issue #2はコメント＋クローズ済み、
マップIssue #1のチェックリストも更新済み。

## 直近セッションでやったこと（2026-09-01）

1. Issue #2着手前にCLAUDE.mdの方針に従い実装スコープの選択肢を提示（環閉包ラベルへの結合種別
   付与記法`C=1...`を含めるか）。ユーザーはA（番号のみ対応、結合種別付与は未対応）を選択し、
   AnyDR 0024として記録。
2. TDDで実装（Red→Green、`./gradlew allTests`・`./gradlew build`ともにグリーン確認済み）。
   - `Token.kt`: `RingClosure(label: Int)`を追加。
   - `Tokenizer.kt`: 数字を`RingClosure`トークンとして返すよう変更（従来は専用Failureで拒否）。
   - `SmilesParser.kt`: `pendingRingClosures: MutableMap<Int, Pair<AtomId, Int>>`
     （label→(atom, position)）を追加し、同じラベルの再出現でボンドを生成。自己参照ラベル・
     未クローズラベル・結合種別付与記法はすべて位置情報付きFailureを返す。
   - `TokenizerTest.kt`/`SmilesParserTest.kt`: 旧「環閉包は未対応」テストを実際の挙動テストに
     置き換え、複数桁ラベル・ラベル再利用・自己参照・未クローズ・結合種別付与拒否のテストを追加。
3. GitHub側: Issue #2にコメント＋クローズ、マップIssue #1のChildrenチェックリストと
   Decisions-so-farを更新（AnyDR 0024・コミット`0f2cfbd`へのリンクを追加）。
4. コミット`8aa21e4`・`0f2cfbd`とも`origin/main`にpush済み。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0016`: 前回までに反映済み。コード上も反映済み。
- `0017`（テキスト入力+読み取り専用描画）・`0019`〜`0023`（レイアウト/Kekulé描画/配布/Issue構成/
  CI）: **未実装**。対応するIssue（#5〜#10）着手時にコードへ反映する。
- `0018`（環閉包＋芳香族小文字表記までのv1文法スコープ）: **部分的に実装済み**。環閉包（Issue #2）
  は実装済み、芳香族小文字表記（Issue #3）は未実装。
- `0024`（Issue #2は環閉包ラベルを番号のみ対応、結合種別付与記法は未対応）: **実装済み**
  （コミット`0f2cfbd`）。

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
    BondType.kt (SINGLE/DOUBLE/TRIPLE/AROMATIC) / Bond.kt
    Molecule.kt        isAromatic(atomId)。bondsByAtom/aromaticAtomIdsをby lazyでキャッシュ
    Token.kt           sealed interface { AtomSymbol, BondSymbol, RingClosure(label), LParen, RParen }
    PositionedToken.kt data class(token, position: Int)
    TokenizeResult.kt  sealed class { Success(tokens), Failure(reason) }
    Tokenizer.kt       文字列→PositionedTokenリスト。環閉包の数字はRingClosureトークン化済み。
                       芳香族小文字/角括弧は引き続き専用理由で拒否
    SmilesParser.kt    parse()はTokenizer+再帰下降で直鎖+分岐+環閉包のMoleculeを構築
  src/commonTest/kotlin/com/smilestudio/core/
    MoleculeTest.kt (7件、日本語メソッド名、グリーン)
    TokenizerTest.kt (12件、グリーン。環閉包ラベルのトークン化テストを追加)
    SmilesParserTest.kt (18件、グリーン。環閉包記法のパーステストを追加)

ui-compose/                           # kotlin(multiplatform) + Compose Multiplatform（変更なし）
  build.gradle.kts
  src/commonMain/kotlin/com/smilestudio/ui/MoleculeCanvas.kt (空のCanvas、TODOのみ)

desktop-app/                          # kotlin(jvm) + compose.desktop.application（変更なし）
  build.gradle.kts
  src/main/kotlin/Main.kt (ウィンドウを開いて空のMoleculeCanvasを表示するのみ)

docs/any-decision-record/  0001〜0024
CONTEXT.md                 5用語（Implicit/Explicit Hydrogen Count, Aromatic Atom, Aromatic Bond, Ring, AtomId）。変更なし
GitHub Issues               #1(map) + #3〜#10(未着手、Wayfinder方式)。#2はクローズ済み
```

## ⚠️ コードと決定のズレ

- `0018`（芳香族小文字表記）→ Issue #3。`Tokenizer`は引き続き小文字芳香族記号を専用Failureで拒否。
- `0019`（固定角度配置レイアウト）→ Issue #4, #5。座標計算ロジックは未着手。
- `0020`（Kekulé描画）→ Issue #6。
- `0017`（テキスト入力+描画）・実際のCanvas描画 → Issue #7, #8。`MoleculeCanvas`は空、
  `desktop-app`にSMILES入力欄は存在しない。
- `0021`（Windows向けパッケージング）→ Issue #9。
- `0023`（GitHub Actions自動リリース）→ Issue #10。

## 既知の注意点（未対応・要フォローアップ）

1. `compose.runtime`等のバージョンカタログ経由アクセサがCompose Multiplatform 1.12.0で
   非推奨警告になっている。ビルドは通るが警告あり（優先度低、未着手）。
2. `Element`の元素セットは有機化学サブセット10種（H,C,N,O,F,P,S,Cl,Br,I）で仮実装。

## 次にやりそうなこと（未着手）

- **Issue #3「SMILESパーサー: 芳香族小文字表記への対応」から着手**（Issue #2完了により
  依存が解消され、現在の唯一のフロンティアIssue）。TDDで進め、`./gradlew allTests`のグリーンを
  確認する。
- 以降はIssue依存関係の順（#3→#4→#5,#6→#7→#8→#9→#10）に沿って進める。
- Koog連携・グラフィカルな構造エディタ（AnyDR 0017で保留したアプローチB）は、v1リリース後の
  将来タスク。
