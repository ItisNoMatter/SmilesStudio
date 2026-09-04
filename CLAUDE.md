# SmilesStudio Project Context

このプロジェクトは、化学徒向けのSMILES記法エディタ＆構造式描画ツールです。
Kotlin Multiplatform (KMP) を使用して開発しており、Compose for Desktop（既存）と
Android（Shipaton 2026ハッカソン対応、`android-app`モジュール追加）の両方をターゲットにします。

## Shipaton 2026対応（現在の最優先事項）
RevenueCat主催のモバイルアプリハッカソン（2026/8/1〜9/30）に参加中。詳細な方針は
`docs/any-decision-record/0027`〜`0044`、`0048`〜`0049`を参照。要点:
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
*   受賞戦略はNext Gen Award（学生向け。ストア公開・有料開発者アカウント不要、OSSライセンス
    付き公開リポジトリ＋デモ動画のみで応募可）＋ #BuildInPublic Awardの二枚看板に絞る。Kotlin賞
    （JetBrains、iOS/Android両対応が要件）・グランプリ・Design賞・Peace賞は積極的には狙わない
    （0048）。
*   #BuildInPublic運用は、既存AnyDRバックログの遡及投稿（ログ発掘型）と、公開の場で設計判断を
    問いかけ実際の反応を意思決定に反映するフィードバック実証型を主軸とする。他のShipaton参加者
    との相互フォロー・返信などの互恵的関与は「余裕があれば」の扱い（0049）。

### BuildInPublicツイート運用（`/buildinpublic-tweet` Skillが参照）
`docs/any-decision-record/`内の決定を`#BuildInPublicAward`向けにツイートする際の規約（0039〜0044）:
*   ハッシュタグ: `#Shipaton #BuildInPublic`（Shipaton公式ルールで必須/任意と規定。追加の技術系
    ハッシュタグを足すかはその都度判断してよい）
*   RevenueCat公式アカウントへのメンションは不要（ルール上の義務なし）
*   トーン: 完了報告ではなく、「なぜその決定に至ったか」「何を学んだか」を一言添える、開発の
    過程を共有するトーン
*   言語: 英語のみ（日本語版は作らない）
*   リンク先: 該当AnyDRの英語版（`docs/any-decision-record/en/<番号>-*.md`、なければSkillが
    オンデマンドで生成する）

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
設計方針が決定した後は、息を吸うようにテスト駆動開発（TDD）で実装を進めます。以下の5ステップを
1つずつ順番に実行し、**各ステップが終わるごとに状況を報告し、次に進んでよいか確認を挟むこと**。
一気に実装まで進めない。

1. **テストコードの作成**: 実装の前に、要求仕様を満たすテストコードを記述する。この時点では
   テスト対象が存在しないためコンパイルエラーになる想定（構造のRED）。
2. **コンパイルエラーの解消（スケルトン実装）**: 空のクラスやメソッド（戻り値はダミー値や
   `TODO()`など）を作成し、コンパイルを通す。まだ振る舞いは実装しない。
3. **振る舞いのRED確認**: テストを実行し、「期待値と異なる」または例外によりテストが失敗する
   （振る舞いのRED）ことを確認し、失敗した出力結果を提示する。
4. **実装（GREEN）**: テストが通過するように最小限のロジックを実装し、テストがGREENになった
   ことを確認する。
5. **リファクタリング**: コードの可読性を見直し、Kotlinらしい簡潔な書き方にリファクタリングする。

【重要】
*   各ステップの完了後、次のステップに進む前に必ず状況を報告し、確認を待ってください。
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