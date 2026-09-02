# 0042: ツイートは英語のみとし、日本語版は作成しない

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[[0039]]〜[[0041]]でBuildInPublicツイート作成Skillの配置場所・英語版AnyDRの格納場所・呼び出し方を決めた後、ツイート自体の言語を検討した。

## Decision

ツイートは英語のみで作成する。日本語版のツイートは作成しない。

## Alternatives

- 英語＋日本語（2ツイート、またはスレッド形式）: 審査員・国際コントリビューター向け（英語）と化学徒ユーザー向け（日本語）の両方にリーチできるという利点があったが、ツイート文を2パターン作成する必要がありSkillの複雑さ・実行コストが増すこと、1つの決定に対し2投稿になりフィード上で冗長に見える可能性があることから、BuildInPublicAwardという目標に照らしシンプルさを優先し不採用。

## Consequences

- ツイート作成Skillはツイート文を1パターンのみ生成すればよく、実装がシンプルになる。
- 化学徒ユーザー（日本語話者）向けのリーチは今回のツイート運用では狙わない。将来必要になれば日本語版の追加を再検討する。

## Related

- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0041-explicit-invocation-only-tweet-skill](./0041-explicit-invocation-only-tweet-skill.md)
