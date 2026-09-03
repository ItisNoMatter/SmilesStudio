# 0058: buildinpublic-tweet SkillをAnyDR/AnyAR両対応に拡張する

- Date: 2026-09-03
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

`buildinpublic-tweet`Skill（グローバル、`~/.claude/skills/buildinpublic-tweet/`）は当初AnyDR
（`docs/any-decision-record/`）専用として設計された（AnyDR 0039〜0044）。その後`any-action-record`
Skillが新設され、AnyAR（`docs/any-action-record/`、Objective/Action/Result/Reflectionsの記録）が
追加されたことで、既存のAnyDR専用実装ではAnyARをツイート下書きの材料にできない状態になった。
ユーザーから「/buildinpublic-tweetを、AnyAR対応にしてほしい」という依頼があり、CLAUDE.mdの複数案
提示ルールに従い実装前に選択肢を提示した。

## Decision

既存の`buildinpublic-tweet`Skillを拡張し、AnyDR・AnyARの両方を対象にできるようにする
（アプローチA）。呼び出し時に種別（AnyDR/AnyAR）を明示させるか、番号がどちらか一方のディレクトリ
にしか存在しない場合のみ自動解決する（両方または両方に存在しない場合はユーザーに確認する）。
英訳（`en/`サブディレクトリ）の生成規約・文字数カウント・クリップボードコピー等のロジックは
種別に依らず共有し、ツイート本文合成時に参照するフィールドのみ種別で分岐させる
（AnyDR: Decision/Consequences/Alternatives、AnyAR: Reflectionsを主軸にObjective/Action/Result
で最小限の文脈づけ）。

## Alternatives

- `docs/any-action-record/`専用の新しい兄弟Skill（例: `buildinpublic-actionlog-tweet`）を新設する:
  AnyDR 0056が`buildinpublic-poll`で採った前例（入力形・目的が異なるものは別Skillに分離）との
  一貫性があり、各Skillファイルが単一の入力形だけを扱いシンプルに保てる、既存`buildinpublic-tweet`
  のI/F・出力を一切変更せずに済むという利点があったが、ハッシュタグ規約読み込み・文字数カウント・
  クリップボードコピー・owner/repo取得など大部分のロジックが3つ目のSkillに重複することになり、
  AnyDR 0056が既に「同種のuser-invokedスキルが3つ以上に増えたらrouter skill化を検討する余地が
  ある」と明記していたまさにその閾値に達してしまうため不採用。AnyDR-tweetとAnyAR-tweetは
  「確定済みの記録を要約してリンクする」という同じ目的・同じツイート形状であり、pollが分離された
  理由（要約 vs 未確定の問いかけという本質的に異なる目的）には該当しないと判断した。
- `~/.claude/skills/`配下に共有の参照ファイルを切り出し、tweet/poll/新Skillの3つがそれぞれ参照する
  形にする: AnyDR 0056が示唆していた将来的な選択肢を先取りできる、各Skillの入力形は分離されたまま
  という利点があったが、今回のスコープ（AnyAR対応を1つ追加したいだけ）に対して明らかにオーバー
  エンジニアリングで、3スキル同時のリファクタリングコストが発生し「最小構成」の一貫方針にも反する
  ため不採用。

## Consequences

- 今後`/buildinpublic-tweet AnyDR <番号>`または`/buildinpublic-tweet AnyAR <番号>`の形で呼び出す
  想定になる（バレ番号のみの場合は曖昧性解決ロジックが働く）。
- AnyARの英訳も`docs/any-action-record/en/`という、AnyDRの`en/`規約（0040）を踏襲したサブ
  ディレクトリに生成されるようになる。
- 将来同種のuser-invokedスキルが3つ以上に増えた場合は、AnyDR 0056が示唆した共有参照ファイル
  切り出し（不採用としたアプローチ）を再検討する余地が残る。

## Related

- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0040-english-anydr-subdirectory](./0040-english-anydr-subdirectory.md)
- [0050-any-action-record-purpose](./0050-any-action-record-purpose.md)
- [0053-anyar-template-fields](./0053-anyar-template-fields.md)
- [0056-buildinpublic-poll-skill](./0056-buildinpublic-poll-skill.md)
