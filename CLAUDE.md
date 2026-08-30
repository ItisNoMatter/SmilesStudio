# SmilesStudio Project Context

このプロジェクトは、化学徒向けのSMILES記法エディタ＆構造式描画ツールです。
Kotlin Multiplatform (KMP) と Compose for Desktop を使用して開発しています。

## 開発ルール・制約
*   **言語 & ビルド:** Kotlin (最新安定版) / Gradle Kotlin DSL (`.kts`) を使用すること。
*   **アーキテクチャ:**
    *   `core-smiles`: ピュアKotlin。SMILESのパースと化学モデル（ドメインロジック）。UIやAndroid固有の依存を持たないこと。
    *   `ui-compose`: Compose Multiplatform。`core-smiles`に依存し、Canvas描画を担当。
    *   `desktop-app`: Compose for Desktop アプリケーションエントリポイント。
*   **AI連携 (Koog):** 将来的にJetBrains Koogを利用したマルチモーダル画像認識（手描き構造式 -> SMILES）を導入予定。

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