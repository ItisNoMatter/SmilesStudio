# 0128: Firestoreは`users/{uid}`の単一ドキュメントで購読状態・無料枠カウンターを管理する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #34のCloud Functionsが参照するFirestoreのデータモデル（購読状態・無料枠カウンターの
持ち方）を検討した。

## Decision

`users/{uid}`の単一ドキュメントに`isSubscribed`, `freeTierCount`, `freeTierMonth`等の
フィールドをまとめて持つ。

## Alternatives

- `users/{uid}/subscription/current`と`users/{uid}/usage/{YYYY-MM}`のようにサブコレクション
  で分ける: 月ごとの利用履歴が残り将来の分析に使える利点はあるが、現状の要件（今の購読状態と
  今月のカウンターが分かればよい）に対してオーバースペックで、複数ドキュメントにまたがる
  トランザクション設計の複雑さも見合わないため不採用とした。

## Consequences

- 1回の読み取り/書き込みで完結し、Firestoreトランザクションでのカウンター消費の原子性を
  保ちやすい。
- 月をまたぐたびにフィールドを上書きするため過去の利用履歴は残らないが、現状の要件では
  問題ない。

## Related

- [0124-free-tier-counter-moves-server-side](./0124-free-tier-counter-moves-server-side.md)
- [0126-cloud-function-as-callable](./0126-cloud-function-as-callable.md)
