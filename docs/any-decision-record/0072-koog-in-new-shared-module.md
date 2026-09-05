# 0072: Koog連携ロジックは新規の共通KMPモジュールに実装する

- Date: 2026-09-05
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[Issue #14](https://github.com/ItisNoMatter/SmilesStudio/issues/14)（Koog SDK導入とVision LLM呼び出し）に着手するにあたり、`/grill-with-docs`で実装方針を検討した。公式ドキュメント（GitHub README、公式ブログ）の間でKoogのKotlin Multiplatform対応ターゲットの記載に食い違いがあり（「JVM・JS・WasmJS・iOS」とする記載と「Android含む」とする記載が両方存在）、`core-smiles`/`ui-compose`が使うKMPの`android`ターゲット（`com.android.kotlin.multiplatform.library`）で正式に動作するか公式情報だけでは確証が持てなかった。

ユーザーの指示により、判断の前に実際の技術検証を行った: `ai.koog:koog-agents:1.2.0`を`core-smiles`の`commonMain`に一時的に追加し、`./gradlew :core-smiles:compileAndroidMain :core-smiles:compileKotlinJvm`を実行したところ、両方のターゲットでコンパイルが成功した（このプロジェクトの構成: AGP 9.1.0、compileSdk 37、minSdk 26）。検証用の変更は直後に元に戻した。

## Decision

Koog連携ロジック（画像→SMILES文字列変換のAPI呼び出し部分）は、`core-smiles`とは別の新しい共通KMPモジュール（`jvm()`＋`android {}`構成、名称は実装時に決定）に実装する。`android-app`はこのモジュールに依存する。画像認識・AI連携ロジックは化学ドメインロジックではないため、`core-smiles`本体には混ぜない。

## Alternatives

- `android-app`モジュール内に直接実装する: 新規モジュール・共通化が不要でシンプルという利点があり、技術検証前は「androidターゲットでの動作が未確認」というリスクを回避する目的で推奨していたが、実際に検証した結果このリスクが解消されたため、再利用性のなさというCons（デスクトップ等への将来的な展開余地がない）を理由に不採用に切り替えた。

## Consequences

- 新しいGradleモジュールの追加作業が実装時に発生する（`settings.gradle.kts`への登録、`build.gradle.kts`のKMP構成含む）。
- Koog連携ロジックはUIから独立してJVMユニットテストで検証できる（Issue #14の「UIとは独立して、API呼び出し部分単体でテスト可能な形で先行実装する」という要件を満たす）。
- 将来的にデスクトップやiOSで同機能を再利用する可能性を残せる（現時点でその計画はないが、モジュール分離により選択肢として残る）。

## Related

- [0028-handdrawn-structure-recognition-mvp](./0028-handdrawn-structure-recognition-mvp.md)
- [0029-gemini-default-vision-llm-provider](./0029-gemini-default-vision-llm-provider.md)
- [0002-kmp-module-structure-core-smiles-ui-compose](./0002-kmp-module-structure-core-smiles-ui-compose.md)
