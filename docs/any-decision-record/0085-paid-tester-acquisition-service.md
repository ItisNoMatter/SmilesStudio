# 0085: 非公開テストの12人のテスターは有料サービスで確保する

- Date: 2026-09-06
- Status: Accepted
- Category: process
- Deciders: the user

## Context

Issue #18（Google Play非公開テストの開始・運用）を`/grill-with-docs`で進める中で、Google Play
Consoleの新規個人開発者アカウントに必須の「12人以上のテスターによる14日間連続の非公開テスト」
を満たすため、テスターの確保方法を決める必要があった。

## Decision

12人以上のテスターは、知人・Shipaton参加者コミュニティ等の自分のネットワーク経由ではなく、
有料のテスター獲得サービスを利用して確保する。

## Alternatives

- 知人・コミュニティ（Shipaton参加者のDiscord等）経由で確保する: 費用がかからず
  BuildInPublic運用（AnyDR 0049）とも相性が良いという利点があったが、14日間の継続的な
  参加を知人に頼み続けるのは心理的なコストが高く、確実性・速さでも劣る。Phase 1期限
  （2026-09-08）が迫っており、確実に12人を集められる手段を優先すべきと判断し不採用。

## Consequences

- 具体的にどの有料サービスを使うかは別途検討が必要（次のフロンティア）。
- BuildInPublic運用（知人・コミュニティとの互恵的関与）とは別軸の対応になるため、
  テスター確保の過程自体はBuildInPublicのネタにはなりにくい。ただし「なぜ有料サービスを
  選んだか」という判断自体はAnyDRとして記録されているため、ネタにする場合はこの決定を
  参照できる。

## Related

- [0084-play-console-upload-scope-includes-24-25](./0084-play-console-upload-scope-includes-24-25.md)
- [0062-single-production-submission-parallel-closed-testing](./0062-single-production-submission-parallel-closed-testing.md)
