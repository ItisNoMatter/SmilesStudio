# 0088: プライバシーポリシーはGitHub Pagesでホストする

- Date: 2026-09-06
- Status: Accepted
- Category: tooling
- Deciders: the user, Claude Code

## Context

[AnyDR 0087](./0087-store-listing-assets-own-issue.md)でストア掲載素材を独立Issueとして
切り出した後、Google Playのストア掲載情報に必須のプライバシーポリシーURLを、どこにホスト
するかを決める必要があった。

## Decision

プライバシーポリシーのページはGitHub Pagesでホストする。

## Alternatives

- 別のホスティングサービス（Notion公開ページ等）: 既に使っているツールがあればそちらで
  公開する選択肢もあったが、リポジトリと同一の場所で完結し追加コスト・別サービスへの
  登録が不要なGitHub Pagesの方がシンプルであり、他に有力な既存ツールが挙がらなかった
  ため不採用。

## Consequences

- リポジトリでGitHub Pagesを有効化する必要がある（`docs/`ディレクトリまたは専用ブランチを
  公開元として設定）。
- プライバシーポリシーの文面自体は別途作成が必要（次のフロンティア）。

## Related

- [0087-store-listing-assets-own-issue](./0087-store-listing-assets-own-issue.md)
