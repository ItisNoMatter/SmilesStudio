# 0013: SmilesParserはTokenizer分離（二段階）で実装する

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるグリリングセッションで、0012により最初のスコープが
「直鎖+分岐」に決まったことを受け、`SmilesParser`の内部実装アーキテクチャを
どうするかが議題になった。CLAUDE.mdの複数案提示ルールに従い、Claude Codeが3案を
提示し、ユーザーが選択した。

## Decision
`SmilesParser`は「文字列→トークン列（Tokenizer）→再帰下降パース」という二段階の
アーキテクチャで実装する。中間表現としてTokenの型を用意し、パースロジックとは
別にトークナイズの段階を設ける。

## Alternatives
- **アプローチB: 一体型再帰下降（Claude Codeの推奨案）** — 中間表現を作らず文字列に
  直接再帰下降でパースする案。直鎖+分岐という今の文法規模に対しては実装量が少なく
  小刻みTDDサイクルと相性が良いという理由で推奨したが、ユーザーはアプローチAの
  Pros（各段階を単位テストで分離検証できる、Cl/Brのような複数文字元素記号の曖昧性
  解決をTokenizer側に閉じ込められる、将来の環閉包・芳香族拡張時にトークン種別の
  追加で対応しやすい）を優先し不採用とした。
- **アプローチC: スタックベースの状態機械（非再帰）** — 括弧のネストをスタックで
  明示管理し再帰呼び出しを避ける案。SMILES文字列は実際には短くスタック
  オーバーフローの懸念が現実的でない一方、対応関係の明示管理によりコードの
  可読性が再帰下降より劣ることが多いため不採用。

## Consequences
Token型（元素記号、結合記号、開き/閉じ括弧などを表す）を`core-smiles`に新規定義する
必要がある。トークナイズ段階とパース段階を別々にTDDで進められるため、テストは
Tokenizer単体のテストとパーサー単体のテストに分けて書いていく。将来の環閉包・
芳香族表記への拡張は、新しいトークン種別を追加する形で対応する想定。

## Related
- [0012-smiles-parser-initial-scope-chain-and-branches](./0012-smiles-parser-initial-scope-chain-and-branches.md)
- [0008-smiles-parser-result-type](./0008-smiles-parser-result-type.md)
