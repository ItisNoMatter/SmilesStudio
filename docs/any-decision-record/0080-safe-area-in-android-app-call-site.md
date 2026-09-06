# 0080: Safe Area対応はandroid-app呼び出し側に実装し、共有Composableには入れない

- Date: 2026-09-06
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[AnyDR 0079](./0079-safe-area-tracked-as-own-issue.md)で切り出したIssue #24（android-appの
Safe Area対応）の実装方針を`/grill-with-docs`で検討した。インセット対応（`WindowInsets.safeDrawing`
等）を、`ui-compose`の共有Composable`MoleculeEditor`自身に持たせるか、それとも呼び出し側
（`android-app`の`MainActivity.kt`）に持たせるかを決める必要があった。

## Decision

Safe Area対応（`enableEdgeToEdge()`の呼び出し・`Modifier.safeDrawingPadding()`の適用）は
`android-app`の`MainActivity.kt`（呼び出し側）に実装する。`ui-compose`の`MoleculeEditor`
自体には一切手を入れない。

## Alternatives

- `MoleculeEditor`内部でインセット対応する: `MoleculeEditor`を使う限りセーフエリア対応が
  保証され、将来Composeベースの別ホストが増えても一箇所で完結するという利点があった。しかし
  `enableEdgeToEdge()`自体が`ComponentActivity`（`androidx.activity:activity-compose`）のAPIで
  Activity起点でしか呼び出せず、結局インセット対応の主要な設定はandroid-app側のActivityに
  書くことになる。`WindowInsets`はAndroid以外のターゲット（desktop-app）では意味を持たない
  概念でもあり、共有コンポーネントにAndroid固有の関心事を持ち込むのは`ui-compose`＝「見た目の
  描画のみを担当する」という既存の役割分担（CLAUDE.mdの記述）と整合しないため不採用。

## Consequences

- Android固有の関心事は`android-app`に閉じ、`desktop-app`には一切影響しない。
- 将来Android以外のCompose UIホスト（例: Compose for Web）が増えた場合、そのホスト側でも
  同様のインセット対応を都度実装する必要がある。ただし現時点でそのような計画はない。
- 具体的な実装（画面全体を一律インセットするか、`MoleculeCanvas`は背景としてedge-to-edgeの
  ままにし`TextField`だけインセットするか等）は別途検討する。

## Related

- [0079-safe-area-tracked-as-own-issue](./0079-safe-area-tracked-as-own-issue.md)
- [0027-android-app-module-for-shipaton-2026](./0027-android-app-module-for-shipaton-2026.md)
