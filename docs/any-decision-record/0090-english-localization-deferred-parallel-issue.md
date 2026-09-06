# 0090: 英語対応は今回の提出に含めず、非公開テスト運用と並行して別Issueで進める

- Date: 2026-09-06
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

Issue #18を`/grill-with-docs`で進める中で、ユーザーから「有料テスターサービスに使って
もらう上で英語対応は不要か（Shipatonに向けていずれ必要になる）」という指摘があった。
現状`android-app`の全UI文字列はKotlinコード内に日本語でハードコードされており
（`strings.xml`未使用）、英語対応には`stringResource`化＋英訳という一定規模の作業が
必要になる。Phase 1期限（2026-09-08）が迫る中、これを今回の提出に含めるかどうかを
検討した。

調査の結果、Google Playの非公開テスト14日間カウントは、テスト期間中にアプリを更新
しても**リセットされない**（リセットされるのはテスター数が12人を下回った場合のみ）
ことが確認できた。

## Decision

英語対応は今回のPlay Console初回提出には含めない。[AnyDR 0062]
(./0062-single-production-submission-parallel-closed-testing.md)で既に決めていた
「14日間の非公開テストと機能実装（#14〜#17）を並行させる」という方針と同様に、英語対応も
非公開テスト運用と並行する別Issue（#11のsub-issue）として進める。

## Alternatives

- 英語対応してからPlay Consoleに提出する: テスターがUIを理解して使えるためエンゲージ
  メントの質が上がるという利点、Shipatonで将来必要になる作業を前倒しできるという利点が
  あったが、Phase 1期限に間に合わない可能性が高く、締切直前に新たなスコープを追加する
  リスクが大きいため不採用。

## Consequences

- 非公開テスト期間の一部、テスターは日本語UIのまま使うことになり、フィードバックの質が
  やや落ちる可能性がある。
- 英語対応は非公開テスト期間中に安全にアップデートとして追加できる（14日カウントに
  影響しないことを確認済み）。
- 英語対応を独立Issueとして起票し、Issue #11のsub-issueとして紐づける必要がある。

## Related

- [0062-single-production-submission-parallel-closed-testing](./0062-single-production-submission-parallel-closed-testing.md)
- [0087-store-listing-assets-own-issue](./0087-store-listing-assets-own-issue.md)
