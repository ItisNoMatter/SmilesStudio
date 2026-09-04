# 0064: buildinpublic-tweet Skillが生成した英訳ファイルを自動でコミット・pushする

- Date: 2026-09-04
- Status: Accepted
- Category: tooling
- Deciders: the user

## Context

`buildinpublic-tweet`Skillは、AnyDR/AnyARの英訳が存在しない場合その場で生成するが、生成しただけではリンクが404のままで、これまでは毎回ユーザーが手動で「pushして」と依頼する必要があった。ユーザーから「英訳ファイルをコミットpushするところまでtweetスキルに含めてほしい」という明示的な依頼があった。

## Decision

`buildinpublic-tweet`Skillが新規に英訳ファイルを生成した場合、そのファイル1つだけをスコープにした`git add`・commit・pushを即座に自動で行う（確認は求めない）。これは「コミット前は必ず確認する」という一般原則に対する、この1ケース（生成した英訳ファイル1つのみ）に限定した標準の例外とする。他の保留中の変更を巻き込むことは禁止。push失敗時はエラーを報告し、リンクが解決しない旨をユーザーに伝える。

## Alternatives

本文中に明示的な代替案の比較記述はなし。ユーザーが直接の方針として依頼し、そのまま実装した（単純な機能追加であり、複数アプローチを比較検討するような設計フォークではないと判断）。

## Consequences

- `buildinpublic-tweet`Skill（グローバル、`~/.claude/skills/buildinpublic-tweet/SKILL.md`）のStep 3を更新し、翻訳ファイル作成直後にコミット・pushする処理を追加した。Step 4のリンク解決に関する注記も、pushが前提になったことを反映して更新した。
- 今後このSkillを使う際、英訳が新規生成された場合は自動でpushまで完了し、ユーザーが別途pushを依頼する手間がなくなる。
- 「What NOT to do」に、この自動コミットが翻訳ファイル1つに厳密にスコープされ、他の変更に拡大解釈されないことを明記した。

## Related

- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0044-clipboard-copy-best-effort](./0044-clipboard-copy-best-effort.md)
- [0058-extend-buildinpublic-tweet-for-anyar](./0058-extend-buildinpublic-tweet-for-anyar.md)
