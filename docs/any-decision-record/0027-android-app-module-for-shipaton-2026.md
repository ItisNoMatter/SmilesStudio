# 0027: Shipaton 2026参加のためandroid-appモジュールを追加する

- Date: 2026-09-02
- Status: Accepted
- Category: architecture
- Deciders: the user

## Context

Shipaton 2026（RevenueCat主催のモバイルアプリハッカソン、開催期間2026/8/1〜9/30）に参加することを決定した。参加要件は、iOS/Androidアプリを新規公開しRevenueCat SDKを導入してアプリ内課金またはWeb課金を実装することであり、Android版のみでも要件を満たせる。現在のプロジェクトはKMP構成で、`core-smiles`（ピュアKotlin）・`ui-compose`（共有Compose Multiplatform UI）・`desktop-app`（Compose for Desktopエントリポイント）から成る（[[0002]]）。

## Decision

新たに`android-app`モジュールをAndroidエントリポイントとして追加する。既存の`core-smiles`・`ui-compose`をそのまま再利用する（[[0002]]で決めたKMPモジュール構成の自然な延長）。iOS対応はハッカソン期間中はスコープ外とし、優先度が低い「余力があれば」項目として扱う。

## Alternatives

- iOSと同時対応: ハッカソンの参加要件はAndroid版のみで満たせるため、期間内の開発リソースをAndroidに集中させる方が現実的と判断し、同時対応は不採用。

## Consequences

- 既存のデスクトップ版v1ロードマップ（[[0017]]〜0026、Wayfinderマップ[Issue #1](https://github.com/ItisNoMatter/SmilesStudio/issues/1)）は独立して継続する。`android-app`は`core-smiles`/`ui-compose`を共有する並行のワークストリームとして追加される。
- CLAUDE.mdにAndroidが対応プラットフォームとして追記される（本AnyDR記録と合わせて更新済み）。
- 既存Wayfinderマップ（Issue #1）の「Fog」セクション（Koog連携・グラフィカルエディタを将来事項として記載）は、本方針転換を反映しておらず古くなっている。これは今後の「上記全体をIssue化して管理」作業のタイミングで別途更新する。

## Related

- [0002-kmp-module-structure-core-smiles-ui-compose](./0002-kmp-module-structure-core-smiles-ui-compose.md)
- [0017-v1-text-input-readonly-rendering-scope](./0017-v1-text-input-readonly-rendering-scope.md)
