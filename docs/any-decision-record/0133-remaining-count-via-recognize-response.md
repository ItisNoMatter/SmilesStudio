# 0133: 残り無料枠回数はrecognizeImageのレスポンスに含める

- Date: 2026-09-14
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #36（`ImageRecognitionCoordinator`の非BYOKパス対応）で、認識成功時に残り回数を
Snackbar表示する（AnyDR 0120）ための実装方法を検討した。残り回数はサーバー側
（Firestore、AnyDR 0124）が管理しているため、クライアントがどうやってこの値を取得するかが
論点だった。

## Decision

Issue #34で実装済みの`recognizeImage`（Callable Function）のレスポンスを拡張し、
残り無料枠回数を含めて返す。サーバー側の`decideEntitlement`が算出した
`nextState.freeTierCount`から計算する。

## Alternatives

- クライアントがFirestoreの`users/{uid}`ドキュメントを直接読んで残り回数を算出する:
  レスポンス型を変更せずに済み、残り回数取得を認識フローと分離できる利点はあるが、
  AnyDR 0121〜0129で一貫してきた「クライアントは直接Firestoreを触らず、Cloud Functions
  経由のみ」という設計原則から外れるセキュリティルール追加が必要になり、また認識成功と
  Firestore読み取りのタイミングがずれるレースコンディションの検討も余計に必要になるため、
  不採用とした。

## Consequences

- Issue #34で実装・デプロイ済みの`recognizeImage`のレスポンス型（`RecognizeImageResponse`）
  と`entitlementLogic.ts`を変更する必要がある。
- 認識と残り回数取得が1リクエストで完結し、実装がシンプルになる。

## Related

- [0120-remaining-count-via-snackbar](./0120-remaining-count-via-snackbar.md)
- [0124-free-tier-counter-moves-server-side](./0124-free-tier-counter-moves-server-side.md)
- [0126-cloud-function-as-callable](./0126-cloud-function-as-callable.md)
