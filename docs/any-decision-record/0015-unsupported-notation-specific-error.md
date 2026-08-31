# 0015: 未対応記法（環閉包・芳香族）には専用の未対応理由を返す

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるグリリングセッションで、0012が保留した論点
（環閉包数字や芳香族小文字表記のような、今回未対応の記法に遭遇した場合の
`SmilesParser`の振る舞い）が議題になった。CLAUDE.mdの複数案提示ルールに従い、
Claude Codeが2案を提示し、ユーザーが選択した。

## Decision
環閉包数字（例: `1`）や芳香族小文字表記（例: `c`）のような、構文としては
既知だが今回のスコープでは未実装の記法に遭遇した場合、`ParseResult.Failure`は
汎用的な「予期しない文字」エラーではなく、「この記法は未対応」であることが
わかる専用の理由文字列（例: "ring closure notation is not yet supported"）を返す。

## Alternatives
- **アプローチA: 汎用エラーとして扱う** — 「有機サブセットの原子でも既知の
  結合/括弧記号でもない」という一つの判定だけで済み実装がシンプルだが、
  エディタ利用者から見て「未実装の機能」なのか「本当に不正なSMILES」なのかを
  区別できず、0012・0014で一貫して取ってきた「スコープ境界を明示的にする」
  方針と整合しないため不採用。

## Consequences
Tokenizer/パーサーに「構文的には既知だが未対応」の文字（環閉包数字、芳香族小文字）
を検出する専用ロジックが必要になる。将来環閉包・芳香族を実装する際は、この
専用エラー分岐を実際のパースロジックに置き換える形で拡張する。

## Related
- [0012-smiles-parser-initial-scope-chain-and-branches](./0012-smiles-parser-initial-scope-chain-and-branches.md)
- [0014-defer-bracket-atom-notation](./0014-defer-bracket-atom-notation.md)
- [0008-smiles-parser-result-type](./0008-smiles-parser-result-type.md)
