# 0056: buildinpublic-pollを独立したグローバルスキルとして新設する

- Date: 2026-09-03
- Status: Accepted
- Category: tooling
- Deciders: the user, Claude Code

## Context

[[0055]]で定めたフィードバック実証型の運用（Xでの問いかけ→返信の理由付けを判断材料にする）を実行するにあたり、既存の`buildinpublic-tweet`スキル（確定済みAnyDRの要約専用、[[0039]]〜0044で設計）では「まだ決まっていない問いを投げる」投稿を作成できないことが判明した。この種の投稿は[[0055]]の課金パラメータに限らず今後も繰り返し発生しうるため、都度手動でドラフトするのではなく汎用的なスキルとして仕組み化する方針になった。

## Decision

新しいスキル`buildinpublic-poll`を、既存`buildinpublic-tweet`を拡張する形ではなく独立した兄弟スキルとして、`buildinpublic-tweet`と同じグローバル配置（`~/.claude/skills/`）に作成する。ドラフトのみ・投稿はしない、というsibling skillと同じ非目標を維持する。投票の集計結果ではなく返信の理由付けが実質的な材料になるという教訓を、スキル自体の設計原則として明記した。

設計はwriting-for-agentsスキルの参照ドキュメントに基づき、明示呼び出し専用の担保を`disable-model-invocation: true`というfrontmatterのフラグで機械的に強制する形にした。この過程で、既存`buildinpublic-tweet`にはこのフラグが欠落しており、[[0041]]「明示呼び出しのみ」という設計意図が文章による指示だけで担保されていたことが判明したため、あわせて追加修正した（`disable-model-invocation: true`を追加し、descriptionから冗長になった「never triggered proactively」等の文言を整理）。

## Alternatives

- 既存`buildinpublic-tweet`を拡張して2モードにする: ハッシュタグ/トーン読み込みやクリップボードコピーのロジックを共有できる利点はあったが、「確定済みAnyDRの要約」と「未確定の問いかけ」という本質的に異なる入力形・目的を1つのスキルに混在させることになり、0039〜0044で丁寧に設計した既存スキルの見通しを損なうと判断し不採用。

## Consequences

- 今後`buildinpublic-tweet`・`buildinpublic-poll`の2つのグローバルスキルが並立する。同種のuser-invokedスキルが3つ以上に増えた場合は、router skill化（1つのuser-invokedスキルが他を紹介する）を検討する余地がある。現時点では2つなので不要と判断。
- 文字数カウントの厳密化・クリップボードコピー手順など、細部の指示は両スキルにわずかに重複がある。将来スキルが増えた場合は、`~/.claude/skills/`配下に共有の参照ファイルを切り出す選択肢がある（user-invokedスキル同士は互いを呼び出せないため、共有参照は素のファイルに逃がすのが定石）。

## Related

- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0041-explicit-invocation-only-tweet-skill](./0041-explicit-invocation-only-tweet-skill.md)
- [0055-feedback-driven-monetization-poll-process](./0055-feedback-driven-monetization-poll-process.md)
