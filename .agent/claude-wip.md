# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-01

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: Issue #3（芳香族小文字表記対応）実装・クローズ済み。次はIssue #4（Ring検出）

前回セッションでIssue #2（環閉包記法）を実装（コミット 0f2cfbd ）。今回セッションはWayfinderの
唯一のフロンティアだった [Issue #3](https://github.com/ItisNoMatter/SmilesStudio/issues/3)
（SMILESパーサー: 芳香族小文字表記への対応）をTDDで実装し、コミット 147de49 としてpush済み。
Issue #3はコメント＋クローズ済み、マップIssue #1のチェックリストも更新済み。

## 直近セッションでやったこと（2026-09-01）

1. Issue #3着手前にCLAUDE.mdの方針に従いトークン表現の選択肢を提示（`Token.AtomSymbol`への
   `isAromatic`フラグ追加 vs 別トークン型`Token.AromaticAtomSymbol`の新設）。ユーザーは
   「型レベルの強制力はAI時代において重要」という理由で後者を選択し、AnyDR 0025として記録。
2. TDDで実装（Red→Green、`./gradlew allTests`・`./gradlew build`ともにグリーン確認済み）。
   - `Token.kt`: `AromaticAtomSymbol(element: Element)`を追加。
   - `Tokenizer.kt`: `c,n,o,p,s`を`AromaticAtomSymbol`にマッピング。`b`（芳香族ホウ素）は
     `Element`列挙型に対応ケースがないため引き続き未対応（専用理由のFailureのまま）。
   - `SmilesParser.kt`: `aromaticNotationAtoms: MutableSet<AtomId>`を追加し、原子生成ロジックを
     `addAtom(element, aromatic)`ローカル関数に共通化。結合記号省略時のデフォルト結合種別は
     「両端の原子がともに芳香族小文字表記ならAROMATIC、そうでなければSINGLE」というルールに
     変更。環閉包（Issue #2）のボンド生成にも同じルールを適用するよう修正。
   - `TokenizerTest.kt`/`SmilesParserTest.kt`: ベンゼン（`c1ccccc1`、全結合AROMATIC）・
     トルエン様分子（`Cc1ccccc1`、非芳香族原子との結合はSINGLE）・芳香族ホウ素の未対応エラーの
     テストを追加。
3. GitHub側: Issue #3にコメント＋クローズ、マップIssue #1のChildrenチェックリストと
   Decisions-so-farを更新（AnyDR 0025・コミット 147de49 へのリンクを追加）。
4. コミットSHAの表記ルールをユーザーから指摘され反映（GitHubの自動リンクはバッククォート囲み
   や前後スペースなしの地の文密着では効かないため、半角スペースで両端を囲む）。
   `feedback_github_bare_commit_sha_links`メモリを更新し、`.git/hooks/pre-commit`
   （リポジトリ追跡外）にバッククォート囲み・密着どちらも検知する非ブロッキングの警告を追加。
   Issue #2の既存コメントとマップIssueの過去記載も新しい表記に修正済み。
5. コミット 0f2cfbd ・ 147de49 とも`origin/main`にpush済み。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0016`: 前回までに反映済み。コード上も反映済み。
- `0017`（テキスト入力+読み取り専用描画）・`0019`〜`0023`（レイアウト/Kekulé描画/配布/Issue構成/
  CI）: **未実装**。対応するIssue（#5〜#10）着手時にコードへ反映する。
- `0018`（環閉包＋芳香族小文字表記までのv1文法スコープ）: **実装済み**（環閉包=Issue #2、
  芳香族小文字表記=Issue #3、いずれも実装済み）。
- `0024`（Issue #2は環閉包ラベルを番号のみ対応、結合種別付与記法は未対応）: **実装済み**
  （コミット 0f2cfbd ）。
- `0025`（Issue #3は芳香族小文字表記用に別トークン型を新設、bはElement未対応のため未対応）:
  **実装済み**（コミット 147de49 ）。

## 現在のプロジェクト構成

```
settings.gradle.kts        # include(:core-smiles, :ui-compose, :desktop-app)
build.gradle.kts           # ルート: 各プラグインをapply falseで宣言
gradle/libs.versions.toml  # kotlin=2.2.10, composeMultiplatform=1.12.0

core-smiles/                          # kotlin(multiplatform), jvm()ターゲットのみ
  build.gradle.kts
  src/commonMain/kotlin/com/smilestudio/core/
    Element.kt (enum, 有機化学でよく使う元素のサブセット: H,C,N,O,F,P,S,Cl,Br,I。Bなし)
    AtomId.kt (@JvmInline value class)
    HydrogenCount.kt   sealed interface { Implicit, Explicit(count) }
    ParseResult.kt     sealed class { Success(Molecule), Failure(reason: String) }
    Atom.kt    (element, charge, isotope, hydrogenCount: HydrogenCount)
    BondType.kt (SINGLE/DOUBLE/TRIPLE/AROMATIC) / Bond.kt
    Molecule.kt        isAromatic(atomId)。bondsByAtom/aromaticAtomIdsをby lazyでキャッシュ
    Token.kt           sealed interface { AtomSymbol, AromaticAtomSymbol, BondSymbol,
                        RingClosure(label), LParen, RParen }
    PositionedToken.kt data class(token, position: Int)
    TokenizeResult.kt  sealed class { Success(tokens), Failure(reason) }
    Tokenizer.kt       文字列→PositionedTokenリスト。環閉包・芳香族小文字（c,n,o,p,s）は
                       トークン化済み。b（芳香族ホウ素）・角括弧は引き続き専用理由で拒否
    SmilesParser.kt    parse()はTokenizer+再帰下降で直鎖+分岐+環閉包+芳香族のMoleculeを構築。
                       結合種別省略時は両端が芳香族小文字表記ならAROMATIC、それ以外はSINGLE
  src/commonTest/kotlin/com/smilestudio/core/
    MoleculeTest.kt (7件、日本語メソッド名、グリーン)
    TokenizerTest.kt (13件、グリーン)
    SmilesParserTest.kt (21件、グリーン)

ui-compose/                           # kotlin(multiplatform) + Compose Multiplatform（変更なし）
  build.gradle.kts
  src/commonMain/kotlin/com/smilestudio/ui/MoleculeCanvas.kt (空のCanvas、TODOのみ)

desktop-app/                          # kotlin(jvm) + compose.desktop.application（変更なし）
  build.gradle.kts
  src/main/kotlin/Main.kt (ウィンドウを開いて空のMoleculeCanvasを表示するのみ)

docs/any-decision-record/  0001〜0025
CONTEXT.md                 5用語（Implicit/Explicit Hydrogen Count, Aromatic Atom, Aromatic Bond, Ring, AtomId）。変更なし
GitHub Issues               #1(map) + #4〜#10(未着手、Wayfinder方式)。#2・#3はクローズ済み
.git/hooks/pre-commit       コミットSHA表記チェック用の非ブロッキング警告（リポジトリ追跡外）
```

## ⚠️ コードと決定のズレ

- `0019`（固定角度配置レイアウト）→ Issue #4, #5。座標計算ロジックは未着手。
- `0020`（Kekulé描画）→ Issue #6。
- `0017`（テキスト入力+描画）・実際のCanvas描画 → Issue #7, #8。`MoleculeCanvas`は空、
  `desktop-app`にSMILES入力欄は存在しない。
- `0021`（Windows向けパッケージング）→ Issue #9。
- `0023`（GitHub Actions自動リリース）→ Issue #10。

## 既知の注意点（未対応・要フォローアップ）

1. `compose.runtime`等のバージョンカタログ経由アクセサがCompose Multiplatform 1.12.0で
   非推奨警告になっている。ビルドは通るが警告あり（優先度低、未着手）。
2. `Element`の元素セットは有機化学サブセット10種（H,C,N,O,F,P,S,Cl,Br,I）で仮実装。芳香族小文字
   の`b`（ホウ素）はこのため未対応のまま。

## 次にやりそうなこと（未着手）

- **Issue #4「core-smiles: Ring検出アルゴリズムの実装」から着手**（Issue #3完了により
  依存が解消され、現在の唯一のフロンティアIssue）。TDDで進め、`./gradlew allTests`のグリーンを
  確認する。
- 以降はIssue依存関係の順（#4→#5,#6→#7→#8→#9→#10）に沿って進める。
- Koog連携・グラフィカルな構造エディタ（AnyDR 0017で保留したアプローチB）は、v1リリース後の
  将来タスク。
