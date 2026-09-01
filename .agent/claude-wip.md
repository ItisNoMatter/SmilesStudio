# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-01

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: v1ロードマップをグリリングで確定し、GitHub Issueに落とし込み完了。次はIssue #2から実装再開

前回セッション（2026-09-01前半）でSmilesParser本体（直鎖+分岐）をTDD実装・コミット済み
（コミット`60b5e09`）。今回セッション（2026-09-01後半）は`/grill-with-docs`により
「最小構成でユーザーにリリースできるところまで」のv1ロードマップをグリリングで確定し、
AnyDR 0017〜0023として記録した上で、GitHub Issueに落とし込んだ（マップIssue #1 + 子Issue
#2〜#10、Wayfinder方式）。コード変更は今回セッションではなし。

## 直近セッションでやったこと（2026-09-01後半）

1. `/grill-with-docs`でv1ロードマップのグリリングセッションを実施。以下7件をAnyDRとして記録
   （いずれもまだコード未反映、詳細は下記「コードと決定のズレ」参照）。
   - `0017`: v1のコア体験はテキスト編集＋読み取り専用の構造式描画（グラフィカル編集は保留）
   - `0018`: v1のSMILES文法スコープは環閉包＋芳香族小文字表記まで
   - `0019`: 2Dレイアウトは固定角度配置アルゴリズムを自前実装
   - `0020`: 芳香族結合の描画はKekulé構造として表現
   - `0021`: 配布形態はjpackageネイティブインストーラ、GitHub Releases、まずWindowsのみ
   - `0022`: Issue構成はWayfinder方式（マップ+子Issue、依存関係追跡）
   - `0023`: リリース自動化はGitHub Actions
2. `docs/agents/issue-tracker.md`のWayfinder手順に従い、GitHub Issueを作成（`wayfinder:map`・
   `wayfinder:task`ラベルを新規作成した上で使用）。
   - マップIssue: [#1 「SmilesStudio v1: 最小構成でのユーザーリリース」](https://github.com/ItisNoMatter/SmilesStudio/issues/1)
   - 子Issue（sub-issues APIでマップに紐付け済み、native issue dependenciesで`blocked_by`設定済み）:
     - #2 SMILESパーサー: 環閉包記法（リング）への対応 — **依存なし、次の着手先**
     - #3 SMILESパーサー: 芳香族小文字表記への対応 — blocked by #2
     - #4 core-smiles: Ring検出アルゴリズムの実装 — blocked by #2
     - #5 core-smiles: 2Dレイアウト計算（固定角度配置アルゴリズム） — blocked by #4
     - #6 core-smiles: 芳香族結合のKekulize変換 — blocked by #3, #4
     - #7 ui-compose: MoleculeCanvasの描画実装 — blocked by #5, #6
     - #8 desktop-app: SMILES入力欄とパースエラー表示の実装 — blocked by #7
     - #9 desktop-app: Windows向けネイティブインストーラのパッケージング設定 — blocked by #8
     - #10 GitHub Actionsによる自動ビルド・自動リリースワークフローの構築 — blocked by #9
3. `.agent/plan.md`に残っていた古い「Android MVP」ブリーフ（Navigation 3, Material Adaptive等）は
   現行のCLAUDE.md（KMP + Compose for Desktop）と矛盾するピボット前の名残と判断。ユーザー確認の
   上、該当箇所を削除済み（冒頭の「Compose for Desktopでアプリケーションをつくりたい」の1行のみ
   残存）。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0016`: 前回までに反映済み。コード上も反映済み（詳細は割愛）。
- `0017`〜`0023`（今回セッションで新規記録、上記参照）: **いずれも未実装**。次セッション以降、
  Issue #2〜#10の順に実装していく。

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
    Token.kt           sealed interface { AtomSymbol, BondSymbol, LParen, RParen }
    PositionedToken.kt data class(token, position: Int)
    TokenizeResult.kt  sealed class { Success(tokens), Failure(reason) }
    Tokenizer.kt       文字列→PositionedTokenリスト。環閉包/芳香族/角括弧は専用理由で拒否
    SmilesParser.kt    parse()はTokenizer+再帰下降で直鎖+分岐のMoleculeを構築
  src/commonTest/kotlin/com/smilestudio/core/
    MoleculeTest.kt (7件、日本語メソッド名、グリーン)
    TokenizerTest.kt (11件、グリーン)
    SmilesParserTest.kt (12件、グリーン)

ui-compose/                           # kotlin(multiplatform) + Compose Multiplatform（変更なし）
  build.gradle.kts
  src/commonMain/kotlin/com/smilestudio/ui/MoleculeCanvas.kt (空のCanvas、TODOのみ)

desktop-app/                          # kotlin(jvm) + compose.desktop.application（変更なし）
  build.gradle.kts
  src/main/kotlin/Main.kt (ウィンドウを開いて空のMoleculeCanvasを表示するのみ)

docs/any-decision-record/  0001〜0023
CONTEXT.md                 5用語（Implicit/Explicit Hydrogen Count, Aromatic Atom, Aromatic Bond, Ring, AtomId）。今回セッションで変更なし
GitHub Issues               #1(map) + #2〜#10(v1ロードマップ、Wayfinder方式)
```

## ⚠️ コードと決定のズレ

AnyDR 0017〜0023はいずれも設計方針の確定のみで、コードには未反映。対応するGitHub Issueは
上記「直近セッションでやったこと」参照。

- `0018`（環閉包＋芳香族小文字表記対応）→ Issue #2, #3。`Tokenizer`/`SmilesParser`は現状
  直鎖+分岐のみ対応で、環閉包・芳香族小文字は専用Failureで拒否したまま。
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

- **Issue #2「SMILESパーサー: 環閉包記法（リング）への対応」から着手**（依存なしの唯一の
  フロンティアIssue）。TDDで進め、`./gradlew allTests`のグリーンを確認する。
- 以降はIssue依存関係の順（#2→#3,#4→#5,#6→#7→#8→#9→#10）に沿って進める。
- Koog連携（手描き構造式 → SMILES のマルチモーダル認識）・グラフィカルな構造エディタ
  （AnyDR 0017で保留したアプローチB）は、v1リリース後の将来タスク。
