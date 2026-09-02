# SmilesStudio Project Context

このプロジェクトは、化学徒向けのSMILES記法エディタ＆構造式描画ツールです。
Kotlin Multiplatform (KMP) を使用して開発しており、Compose for Desktop（既存）と
Android（Shipaton 2026ハッカソン対応、`android-app`モジュール追加）の両方をターゲットにします。

## Shipaton 2026対応（現在の最優先事項）
RevenueCat主催のモバイルアプリハッカソン（2026/8/1〜9/30）に参加中。詳細な方針は
`docs/any-decision-record/0027`〜`0038`を参照。要点:
*   Android対応が最優先（`android-app`モジュール追加、実装済み）。iOS対応は優先度低。
*   手描き構造式画像 → Koog経由のVision LLM → SMILES変換 → 再描画、というMVP機能を実装する
    （画像1枚→SMILES候補1つ→再描画のみ。詳細は0028）。
*   Vision LLMは開発・デモ用にGemini APIをデフォルト採用しつつ、KoogのマルチプロバイダBYOKは
    維持する（0029）。
*   課金はB案（回数制フリーミアム）またはC案（即ペイウォール）に一本化する（開発が順調なら
    B、期限が厳しくなればCにフォールバック）。トライアルは廃止（0036、0030を置き換え）。
    ダークパターンは不採用。
*   OSS戦略は「三層防御」構想（型システム・AIレビュー・標準装備の期待値テストハーネス）を
    採用する（0037、0031/0032を統合・拡張）。FIR/K2プラグインは将来構想として先送り。
*   OSSライセンスはMITを採用する（0038）。

## 開発ルール・制約
*   **言語 & ビルド:** Kotlin (最新安定版) / Gradle Kotlin DSL (`.kts`) を使用すること。
*   **アーキテクチャ:**
    *   `core-smiles`: ピュアKotlin。SMILESのパースと化学モデル（ドメインロジック）。UIやAndroid固有の依存を持たないこと。
    *   `ui-compose`: Compose Multiplatform。`core-smiles`に依存し、Canvas描画を担当。
    *   `desktop-app`: Compose for Desktop アプリケーションエントリポイント。
    *   `android-app`: Androidアプリケーションエントリポイント（Shipaton 2026対応で追加、`core-smiles`/`ui-compose`を再利用）。
*   **AI連携 (Koog):** Shipaton 2026のMVPスコープとしてJetBrains Koogによるマルチモーダル画像認識（手描き構造式 -> SMILES）を実装する。詳細は`docs/any-decision-record/0028`・`0029`。

## アーキテクチャと意思決定（トレードオフの提示）
あなたは優秀なシニアエンジニアであり、私の設計のスパーリングパートナーです。
新しい機能の実装、データ構造の設計、またはライブラリの選定を行う際は、勝手に1つのアプローチに決めて実装を進めないでください。

必ず以下のフォーマットで**複数の実装案（2〜3案）**を提示し、私の判断を仰いでください。
1.  **アプローチAの概要**
    *   Pros (メリット): パフォーマンス、保守性、拡張性などの観点
    *   Cons (デメリット)
2.  **アプローチBの概要**
    *   Pros
    *   Cons
3.  **あなたの推奨案とその理由**

【厳守】選択肢を提示した後は、**ユーザーからの回答（選択）があるまで実際の実装（ファイルの編集）には進まず、待機**してください。

## 開発ワークフロー（TDDサイクルの徹底）
設計方針が決定した後は、息を吸うようにテスト駆動開発（TDD）で実装を進めます。

1. **Test First**: 実装の前に、要求仕様を満たすテストコード（JUnit 5など）を記述する。
2. **Red**: テストを実行し、意図通りに失敗することを確認する。
3. **Green**: テストをパスさせるための最小限の実装を行う。
4. **Refactor**: 全てのテストがグリーンになった後、コードの設計や可読性を整理する。

【重要】
*   変更を加えた後は、必ず `./gradlew allTests` を実行し、成功した結果を確認してから私に報告してください。
*   テストが失敗した場合は、原因を分析して自律的にコードを修正し、グリーンになるまでサイクルを回してください。

## 頻出コマンド
Claude, 作業の際は以下のコマンドを必要に応じて自律的に活用してください:
*   ビルド: `./gradlew build`
*   全テスト実行: `./gradlew allTests`（`core-smiles`/`ui-compose`はKotlin Multiplatform
    モジュールのため、`test`という名前のタスクは存在しない。`./gradlew test`では
    これらのテストは実行されない点に注意）
*   core-smilesのテスト: `./gradlew :core-smiles:jvmTest`
*   デスクトップアプリ起動: `./gradlew :desktop-app:run`

## Agent skills

### Issue tracker

Issues are tracked in this repo's GitHub Issues (`ItisNoMatter/SmilesStudio`), using the `gh` CLI. See `docs/agents/issue-tracker.md`.

### Domain docs

Single-context layout: `CONTEXT.md` + `docs/adr/` at the repo root. See `docs/agents/domain.md`.