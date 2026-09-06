# 0087: ストア掲載素材（アイコン・プライバシーポリシー等）を独立Issueとして切り出す

- Date: 2026-09-06
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

Issue #18（Google Play非公開テストの開始・運用）を`/grill-with-docs`で進める中で、
`android-app`にアプリアイコン（`mipmap/ic_launcher`等、`AndroidManifest.xml`の
`android:icon`属性も含め）が一切存在せず、プライバシーポリシー・ストア用スクリーンショットも
用意されていないことが判明した。Google Playは非公開テスト以前の「ストア掲載情報の作成」
段階で、アプリアイコン・プライバシーポリシーURLを必須としており、これはIssue #18を含む
どのIssueにも明示的にスコープとして含まれていなかった。

## Decision

アイコン・プライバシーポリシー・スクリーンショット等のストア掲載素材の準備を、独立した
新規GitHub Issueとして切り出す（Issue #18のスコープには含めない）。

## Alternatives

- Issue #18のスコープ内でこのまま対応する: Issue分割の手間がないという利点があったが、
  Issue #18本文の「やること」チェックリストにない作業が紛れ込みスコープが曖昧になる。
  アイコンデザイン・プライバシーポリシー文面作成という、非公開テスト運用そのものとは
  性質の異なる作業であり、このプロジェクトのWayfinder運用（作業は必ずIssue化してから
  着手する）とも一貫しないため不採用。

## Consequences

- Issue #18の実行前に、新規Issue（ストア掲載素材の準備）の完了が事実上の前提条件になる。
  Issue間の`blocked_by`関係として明示する必要がある。
- Phase 1期限（2026-09-08）に対し、素材準備という新たな未実装作業が判明したため、
  スケジュールへの影響を再検討する必要がある。

## Related

- [0084-play-console-upload-scope-includes-24-25](./0084-play-console-upload-scope-includes-24-25.md)
