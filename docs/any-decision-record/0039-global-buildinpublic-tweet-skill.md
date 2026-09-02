# 0039: BuildInPublicツイート作成Skillはグローバルスコープに配置する

- Date: 2026-09-03
- Status: Accepted
- Category: tooling
- Deciders: the user, Claude Code

## Context

Shipaton 2026のBuildInPublicAward獲得を狙い、公開済みのAnyDRへのリンクとその要約をツイートする運用を検討した（`/grill-with-docs`によるグリリングセッション）。この運用を支援するツイート作成Skillを新規に作る必要があり、既存のカスタムSkill配置を確認したところ、`any-decision-record`・`grill-with-docs`はグローバル（`~/.claude/skills/`）、`update-wip`はプロジェクトスコープ（`SmileStudio/.claude/skills/`）という2パターンが既に存在していた。

## Decision

BuildInPublicツイート作成Skillはグローバルスコープ（`~/.claude/skills/`）に配置する。`any-decision-record`と同様に「現在のプロジェクト」を動的に対象とする設計とする。Shipaton固有の文脈（ハッシュタグ規約、ツイートのトーン等）はスキル本体にはハードコードせず、プロジェクト側（CLAUDE.md等）から動的に読み込む形にする。

## Alternatives

- プロジェクトスコープ（`SmileStudio/.claude/skills/`に配置）: `update-wip`と同じ扱いになり、このプロジェクト固有のワークフローであることが明確になる、リポジトリに同梱されコントリビューターに意図が伝わりやすいという利点があったが、将来他プロジェクトでも同様の運用をしたくなった場合にコピー・再設定が必要になることと、ユーザーが個人ワークフロー改善においてこれまでグローバルスコープの個人フォークを好む傾向があった（`grill-with-docs`の個人フォーク採用等）ことから不採用。

## Consequences

- スキル本体は他プロジェクトでも再利用可能な汎用設計にする必要がある。SmileStudio固有の設定（ハッシュタグ、AnyDRの英訳ディレクトリ規約等）はSmileStudio側のCLAUDE.mdやCONTEXT.mdに書き、スキルはそれを動的に参照する。
- リポジトリに同梱されないため、OSSコントリビューターから見た運用の透明性は、CLAUDE.md側の記述で補う必要がある。

## Related

- [0037-three-layer-defense-oss-strategy](./0037-three-layer-defense-oss-strategy.md)
