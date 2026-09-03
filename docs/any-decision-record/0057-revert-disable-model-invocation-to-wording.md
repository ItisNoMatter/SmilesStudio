# 0057: disable-model-invocationではなくdescription文言でSkillの明示呼び出しを担保する

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[[0056]]でbuildinpublic-pollスキルを新設した際、あわせてbuildinpublic-tweetの明示呼び出し担保（[[0041]]）がfrontmatterのフラグで機械的に強制されていない点を修正する目的で、両スキルに`disable-model-invocation: true`を設定した。writing-for-agentsスキルの参照ドキュメントに基づく判断だったが、実際に運用したところ、`disable-model-invocation: true`は「人間がスラッシュコマンドを文字通り入力した場合以外は一切起動できない」という制約であり、Skillツール経由でユーザーの明示的な依頼（「はい、ドラフトして」等の自然言語での依頼）に応じて起動しようとした際にもエラーでブロックされることが判明した。[[0041]]が本来意図していたのは「AnyDR記録直後などにプロアクティブに自発提案しない」ことであり、「ユーザーの明示的な依頼にすら応じない」ことではなかった。

## Decision

両スキル（buildinpublic-tweet、buildinpublic-poll）から`disable-model-invocation: true`を削除し、model-invokedのまま維持する。かわりにdescription欄と本文（「Explicit invocation only」セクション）の文言で、「ユーザーが明示的に頼んだ時のみ起動し、プロアクティブに提案しない」という制約を表現する。このセッション内でのClaude Codeの実際の挙動（一度も自発的にツイート/ポールを提案しなかった）を踏まえ、文言のみの制約でも実用上のリスクは低いと判断した。

## Alternatives

- `disable-model-invocation: true`を維持する: 誤発火が構造的にゼロという最強の保証があったが、ユーザーの明示的な自然言語での依頼にも応じられなくなるという実害が生じたため不採用。

## Consequences

- [[0056]]の「`disable-model-invocation: true`で機械的に強制する」という設計判断は本AnyDRにより上書きされる（[[0056]]自体は歴史的記録としてそのまま残す）。
- 今後、同種の「明示呼び出しのみ」を担保したいSkillを作る際は、`disable-model-invocation: true`ではなく、description・本文の文言で表現する方針を踏襲する。

## Related

- [0041-explicit-invocation-only-tweet-skill](./0041-explicit-invocation-only-tweet-skill.md)
- [0056-buildinpublic-poll-skill](./0056-buildinpublic-poll-skill.md)
