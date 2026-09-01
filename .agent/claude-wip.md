# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-02

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: Issue #4（Ring検出）実装・クローズ済み。次はIssue #5（2Dレイアウト計算）

前回セッションでIssue #3（芳香族小文字表記）を実装（コミット 147de49 ）。今回セッションは
Wayfinderの唯一のフロンティアだった
[Issue #4](https://github.com/ItisNoMatter/SmilesStudio/issues/4)
（core-smiles: Ring検出アルゴリズムの実装）をTDDで実装し、コミット f7bc955 としてpush済み。
Issue #4はコメント＋クローズ済み、マップIssue #1のチェックリストも更新済み。

## 直近セッションでやったこと（2026-09-02）

1. Issue #4着手前にCLAUDE.mdの方針に従い結果表現の選択肢を提示（Ring検出結果を専用の値型
   `Ring(atoms: List<AtomId>)`で表現 vs 生の`List<List<AtomId>>`を直接返す）。ユーザーは
   前者（専用型）を選択し、AnyDR 0026として記録。
2. TDDで実装（Red→Green、`./gradlew allTests`・`./gradlew build`ともにグリーン確認済み）。
   - `Ring.kt`【新規】: `data class Ring(val atoms: List<AtomId>)`。
   - `Molecule.kt`: `rings: List<Ring>`を`by lazy`の派生プロパティとして追加。結合グラフに
     対するDFSで背後辺（back edge）を検出し、1本の背後辺につき1つの環を`parent`マップから
     逆再構成する方式。v1スコープ（AnyDR 0018/0019、単環・非縮合環）では十分だが、縮合環に
     対応する場合は本格的なSSSRアルゴリズムへの再訪が必要（AnyDR 0026に明記）。
   - `MoleculeTest.kt`: 直鎖・分岐（環なし）でringsが空になること、3員環・6員環で環を一周する
     原子の並び順が正しく返ることのテストを追加。
3. GitHub側: Issue #4にコメント＋クローズ、マップIssue #1のChildrenチェックリストと
   Decisions-so-farを更新（AnyDR 0026・コミット f7bc955 へのリンクを追加）。
4. コミット 147de49 ・ f7bc955 とも`origin/main`にpush済み。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0016`: 前回までに反映済み。コード上も反映済み。
- `0017`（テキスト入力+読み取り専用描画）・`0020`〜`0023`（Kekulé描画/配布/Issue構成/CI）:
  **未実装**。対応するIssue（#6〜#10）着手時にコードへ反映する。
- `0018`（環閉包＋芳香族小文字表記までのv1文法スコープ）: **実装済み**（Issue #2・#3）。
- `0019`（固定角度配置レイアウト）: **一部の前提（Ring検出）のみ実装済み**。座標計算本体は
  Issue #5で未着手。
- `0024`（Issue #2は環閉包ラベルは番号のみ対応）: **実装済み**（コミット 0f2cfbd ）。
- `0025`（Issue #3は芳香族小文字表記用に別トークン型を新設）: **実装済み**（コミット 147de49 ）。
- `0026`（Issue #4はRing検出結果を専用の値型`Ring`で表現、DFS背後辺方式）: **実装済み**
  （コミット f7bc955 ）。

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
    Ring.kt            【新規】data class Ring(val atoms: List<AtomId>)
    Molecule.kt        isAromatic(atomId)・rings: List<Ring>。bondsByAtom/aromaticAtomIds/rings
                       をby lazyでキャッシュ。rings はDFS背後辺検出（1背後辺=1環、縮合環は未対応）
    Token.kt           sealed interface { AtomSymbol, AromaticAtomSymbol, BondSymbol,
                        RingClosure(label), LParen, RParen }
    PositionedToken.kt data class(token, position: Int)
    TokenizeResult.kt  sealed class { Success(tokens), Failure(reason) }
    Tokenizer.kt       文字列→PositionedTokenリスト。環閉包・芳香族小文字（c,n,o,p,s）は
                       トークン化済み。b（芳香族ホウ素）・角括弧は引き続き専用理由で拒否
    SmilesParser.kt    parse()はTokenizer+再帰下降で直鎖+分岐+環閉包+芳香族のMoleculeを構築。
                       結合種別省略時は両端が芳香族小文字表記ならAROMATIC、それ以外はSINGLE
  src/commonTest/kotlin/com/smilestudio/core/
    MoleculeTest.kt (11件、日本語メソッド名、グリーン。ring検出のテストを追加)
    TokenizerTest.kt (13件、グリーン)
    SmilesParserTest.kt (21件、グリーン)

ui-compose/                           # kotlin(multiplatform) + Compose Multiplatform（変更なし）
  build.gradle.kts
  src/commonMain/kotlin/com/smilestudio/ui/MoleculeCanvas.kt (空のCanvas、TODOのみ)

desktop-app/                          # kotlin(jvm) + compose.desktop.application（変更なし）
  build.gradle.kts
  src/main/kotlin/Main.kt (ウィンドウを開いて空のMoleculeCanvasを表示するのみ)

docs/any-decision-record/  0001〜0026
CONTEXT.md                 5用語（Implicit/Explicit Hydrogen Count, Aromatic Atom, Aromatic Bond, Ring, AtomId）。変更なし
GitHub Issues               #1(map) + #5〜#10(未着手、Wayfinder方式)。#2・#3・#4はクローズ済み
.git/hooks/pre-commit       コミットSHA表記チェック用の非ブロッキング警告（リポジトリ追跡外）
```

## ⚠️ コードと決定のズレ

- `0019`（固定角度配置レイアウト、座標計算本体）→ Issue #5。Ring検出は済んでいるが座標計算は未着手。
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
3. `Molecule.rings`のDFS背後辺方式は縮合環・橋かけ環を正しく扱えない（AnyDR 0026）。v1スコープ
   では問題ないが、将来スコープを広げる際は要再検討。

## 次にやりそうなこと（未着手）

- **Issue #5「core-smiles: 2Dレイアウト計算（固定角度配置アルゴリズム）」から着手**（Issue #4
  完了により依存が解消され、現在の唯一のフロンティアIssue）。`Molecule.rings`を使って環を正多角形
  として配置するロジックを実装する。TDDで進め、`./gradlew allTests`のグリーンを確認する。
- 以降はIssue依存関係の順（#5→#6→#7→#8→#9→#10）に沿って進める。
- Koog連携・グラフィカルな構造エディタ（AnyDR 0017で保留したアプローチB）は、v1リリース後の
  将来タスク。
