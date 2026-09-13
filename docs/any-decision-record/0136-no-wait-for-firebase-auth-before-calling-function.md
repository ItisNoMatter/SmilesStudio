# 0136: Cloud Function呼び出し前にFirebase Authの完了を待たない

- Date: 2026-09-14
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #36で、`ImageRecognitionCoordinator`から`recognizeImage`（Callable Function）を
呼ぶ際、`SmilesStudioApplication`（Issue #35）が起動時に非同期で開始する匿名認証が
未完了のまま呼び出されるレースコンディションへの対処を検討した。

## Decision

`Coordinator`は`FirebaseAuth`の完了を待つ処理を追加せず、そのまま`recognizeImage`を
呼び出す。匿名認証はユーザーが画像ソース選択・撮影/選択という複数ステップを経る間に
ほぼ確実に完了するため、実質的にレースは起きない。万一`unauthenticated`エラーが起きても
既存の`Failed(reason)`経路でエラーメッセージを表示すれば、ユーザーの再タップ時には認証が
完了しているため再試行で解決する。

## Alternatives

- `FirebaseAuth`の`currentUser`を`AuthStateListener`等で待ってから呼び出す:
  レースを完全に排除できるが、ほぼ起きないレースのために新しい非同期待機の実装・テストが
  必要になり、CLAUDE.mdの「起こり得ないシナリオのためのエラーハンドリングを追加しない」
  方針に反するため不採用とした。

## Consequences

- タイミングレース由来の`unauthenticated`エラーは再試行で解決するが、これとは別に
  `SmilesStudioApplication`の`signInAnonymously()`に`addOnFailureListener`が
  未実装であるため、匿名認証自体が永続的に失敗するケースは今回のリトライでは解決しない
  （この抜けはIssue #35の実装漏れとして別Issueで対応する）。

## Related

- [0130-anonymous-auth-at-app-launch](./0130-anonymous-auth-at-app-launch.md)
- [0134-reactive-free-tier-exhaustion-detection](./0134-reactive-free-tier-exhaustion-detection.md)
