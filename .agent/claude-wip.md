# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-08-31

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: グリリングで決定した設計（0003・0004・0005・0008）をTDDで実コードに反映済み（未コミット）

前回セッションで詰めた`core-smiles`のドメインモデル決定（AnyDR 0003・0004・0005・0008）を、
CLAUDE.mdのTDDサイクル（Test First → Red → Green）に沿って実コードに反映した。
**すべて実装済みで`./gradlew allTests`・`./gradlew build`ともグリーン。ただしまだコミットしていない**
（下記「現在のプロジェクト構成」のファイル群が未コミット状態）。

## 直近セッションでやったこと（2026-08-31）

1. AnyDR 0003〜0008のうち未実装だった3件を、ユーザーの指定順（0003 → 0004/0005 → 0008）で
   TDD実装した。
   - **0003**: `Atom.hydrogenCount`を`Int?`から`sealed interface HydrogenCount { Implicit, Explicit(count) }`
     に変更（新規`HydrogenCount.kt`）。
   - **0004/0005**: `Atom.isAromatic`フィールドを削除し、`Molecule`に`isAromatic(atomId): Boolean`を追加。
     内部で`bondsByAtom`（隣接Bondマップ）と`aromaticAtomIds`（芳香族原子の集合）を`by lazy`で
     キャッシュし、隣接する全Bondが`BondType.AROMATIC`かどうかから導出する。
   - **0008**: `SmilesParser.parse`の戻り値を`Molecule`から`sealed class ParseResult { Success(Molecule); Failure(reason) }`
     （新規`ParseResult.kt`）に変更。スコープはユーザーとの相談の結果「型変更のみ」に限定し、
     実装本体は`ParseResult.Failure("SMILES parsing is not implemented yet")`を返すダミー実装のまま
     （実際のパースロジックは引き続き未着手、下記「次にやりそうなこと」参照）。
   - 各ステップとも`MoleculeTest.kt`（0003・0004/0005）・新規`SmilesParserTest.kt`（0008）を
     先に更新/追加してRedを確認してから実装している。
2. ユーザーからの新規指示により、**テストメソッド名は日本語で統一する**方針を決定し
   `docs/any-decision-record/0010-japanese-test-method-names.md`に記録。既存の英語テスト
   メソッド名（`MoleculeTest.kt`）もすべて日本語に書き換え済み。今後書く全テストも日本語で統一する。
3. `./gradlew allTests`・`./gradlew build`で最終確認しグリーンを確認済み（コミット前）。

## 確定した決定事項（AnyDRに記録済み）

- `0001-core-smiles-id-based-domain-model.md`: IDベース設計（AtomId value class + Map<AtomId, Atom>）。実装済み。
- `0002-kmp-module-structure-core-smiles-ui-compose.md`: core-smiles・ui-composeを真のKMPモジュールとして構成。実装済み。
- `0003-atom-hydrogen-count-sealed-interface.md`: `HydrogenCount` sealed interface化。**実装済み**（今回反映）。
- `0004-derive-aromaticity-from-bonds.md`: 芳香族性をBondから導出。**実装済み**（今回反映）。
- `0005-cache-aromaticity-with-lazy.md`: 芳香族性の導出を`by lazy`でキャッシュ。**実装済み**（今回反映）。
- `0006-ring-as-derived-domain-term.md`: `Ring`をドメイン用語として定義（実装は未着手・環検出アルゴリズムはまだ不要）。
- `0007-atomid-stability-undefined.md`: `AtomId`の安定性は未定義とCONTEXT.mdに明記（ドキュメントのみ、実装変更なし）。
- `0008-smiles-parser-result-type.md`: `SmilesParser.parse`の戻り値を`ParseResult`に変更。**型のみ実装済み**（今回反映）。パース本体は未着手。
- `0009-defer-canonical-smiles-writer.md`: canonical SMILES writerは当面スコープ外。
- `0010-japanese-test-method-names.md`: テストメソッド名は日本語で統一する。**実装済み**（今回反映、既存テストも書き換え済み）。

`CONTEXT.md`（リポジトリルート）にはImplicit/Explicit Hydrogen Count、Aromatic Atom、
Aromatic Bond、Ring、AtomIdの5用語を記録済み（変更なし）。

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
    HydrogenCount.kt   (新規) sealed interface { Implicit, Explicit(count) }
    ParseResult.kt     (新規) sealed class { Success(Molecule), Failure(reason) }
    Atom.kt    (isAromaticフィールド削除済み、hydrogenCountはHydrogenCount型)
    BondType.kt / Bond.kt (変更なし)
    Molecule.kt        isAromatic(atomId)を追加。bondsByAtom/aromaticAtomIdsをby lazyでキャッシュ
    SmilesParser.kt    parse()の戻り値はParseResult。中身はParseResult.Failureのダミー実装
  src/commonTest/kotlin/com/smilestudio/core/
    MoleculeTest.kt (7件、全て日本語メソッド名、グリーン)
    SmilesParserTest.kt (新規、1件、グリーン)

ui-compose/                           # kotlin(multiplatform) + Compose Multiplatform（変更なし）
  build.gradle.kts
  src/commonMain/kotlin/com/smilestudio/ui/MoleculeCanvas.kt (空のCanvas)

desktop-app/                          # kotlin(jvm) + compose.desktop.application（変更なし）
  build.gradle.kts
  src/main/kotlin/Main.kt
```

## 既知の注意点（未対応・要フォローアップ）

1. 上記の変更一式（`HydrogenCount.kt`・`ParseResult.kt`・`SmilesParserTest.kt`・
   `Atom.kt`/`Molecule.kt`/`SmilesParser.kt`/`MoleculeTest.kt`の変更・
   `docs/any-decision-record/0010-...md`）が**まだコミットされていない**。次のセッション、
   またはこのセッションの続きでコミットするか要確認。
2. `compose.runtime`等のバージョンカタログ経由アクセサがCompose Multiplatform 1.12.0で
   非推奨警告になっている。ビルドは通るが警告あり（優先度低、未着手）。
3. `Element`の元素セットは有機化学サブセット10種（H,C,N,O,F,P,S,Cl,Br,I）で仮実装。

## 次にやりそうなこと（未着手）

- 未コミットの変更をコミットする（要ユーザー確認）。
- `SmilesParser.parse()`本体の実装（`ParseResult`は型として存在するので、これに実際のSMILES
  構文解析ロジックを実装していく。TDDで進める）。
- `MoleculeCanvas`の実際の描画ロジック（原子・結合の描画）。
- `Ring`（AnyDR 0006）の環検出アルゴリズムは、実際に必要になった時点で着手。
- Koog連携（手描き構造式 → SMILES のマルチモーダル認識）は将来タスク。
