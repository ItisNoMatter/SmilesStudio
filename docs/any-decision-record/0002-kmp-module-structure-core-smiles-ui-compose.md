# 0002: core-smiles・ui-composeを真のKotlin Multiplatformモジュールとして構成

- Date: 2026-08-27
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
CLAUDE.mdの定義に従い、`core-smiles` / `ui-compose` / `desktop-app` の3モジュール構成で
Gradleプロジェクトを構築するにあたり、`core-smiles`と`ui-compose`をKotlin JVMプラグイン
（デスクトップ専用）で組むか、`kotlin("multiplatform")`プラグインで真のマルチプラットフォーム
モジュールとして組むかを決める必要があった。CLAUDE.mdの複数案提示ルールに従い、
Claude Codeが3案を提示し、ユーザーが選択した。

## Decision
`core-smiles`・`ui-compose`の両モジュールを`kotlin("multiplatform")`プラグインで構成し、
`commonMain`/`jvmMain`ソースセットを用意する（現時点では`jvm()`ターゲットのみ）。
`desktop-app`はプレーンなJVMエントリポイントのまま`compose.desktop.application`プラグインで
構成する。

## Alternatives
- **アプローチA: 全モジュールをKotlin JVMプラグインで統一** — `core-smiles`・`ui-compose`・
  `desktop-app`すべて`kotlin("jvm")`とし、`ui-compose`はデスクトップ専用のCompose
  Multiplatformアーティファクトを利用する構成。Gradle設定が最もシンプルで、デスクトップ
  専用アプリとしては十分だが、真の意味での「Multiplatform」ではなく、将来Android/iOS/Wasm
  ターゲットを追加する際に`commonMain`/`jvmMain`ソースセット構成への作り直しが発生するため
  不採用。
- **アプローチC: core-smilesはJVM、ui-composeのみ真のKMPに** — `core-smiles`は依存ゼロの
  ピュアKotlinなので`kotlin("jvm")`のまま、CLAUDE.mdが明示的に「Compose Multiplatform」と
  定義する`ui-compose`のみ`kotlin("multiplatform")`にする折衷案。複雑さを本当に必要な
  モジュールだけに払える一方、モジュール間でKotlinプラグインの種類が揃わず一貫性に欠け、
  Android追加時に結局`core-smiles`も1段階の移行が必要になる点が決め手となり不採用
  （ユーザーがアプローチBを選択）。

## Consequences
現状デスクトップアプリしか作らない段階で、`commonMain`/`jvmMain`のソースセット分割など
Gradleのボイラープレートが増える。その代わり、将来Androidなどのターゲットを追加する際は
`target`ブロックの追加だけで済み、モジュールの作り直しは不要になる。CLAUDE.mdが謳う
「KMP」「将来のKoog連携」という長期方針とも整合する構成。

## Related
- [0001-core-smiles-id-based-domain-model](./0001-core-smiles-id-based-domain-model.md)
