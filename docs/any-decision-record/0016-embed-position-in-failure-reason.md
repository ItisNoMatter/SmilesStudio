# 0016: ParseResult.Failureのreason文字列に位置情報を埋め込む

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるグリリングセッションで、`SmilesParser.parse`が失敗した際に
`ParseResult.Failure`（0008で決定済み）がエラー箇所の位置情報を伝えるべきかが
議題になった。SMILES入力欄でのリアルタイムバリデーションが将来のユースケースとして
WIPメモに想定されており、エラー箇所をUIでハイライトする際に位置情報が要る可能性が
ある。CLAUDE.mdの複数案提示ルールに従い、Claude Codeが3案を提示し、ユーザーが
選択した。

## Decision
`ParseResult.Failure(reason: String)`という0008で確定した型は変更せず、`reason`の
文字列の中に不正な文字の位置情報を埋め込む（例: "位置5: 不明な文字 'x'"）。

## Alternatives
- **アプローチA: 位置情報を含めない** — 実装は最も簡単だが、将来のリアルタイム
  バリデーションUIでエラー箇所をハイライトする機能が作りにくくなるため不採用。
- **アプローチC: `Failure`の型自体を拡張し位置を構造化する**（例:
  `Failure(reason: String, position: Int?)`） — 位置情報をUI側が文字列パースせずに
  扱える利点はあるが、0008で確定した型を変更することになり、0008の決定を見直す
  形の議論が別途必要になる。UI側の具体的なニーズがまだない現時点では過剰と判断し
  不採用。

## Consequences
Tokenizer/パーサーは、エラー発生時に文字列中のインデックスを把握できる必要があり、
`reason`メッセージの組み立て時にそのインデックスを埋め込む。UI側が将来ハイライト
機能を実装する場合、当面は`reason`文字列のフォーマット（例: "位置N: ..."）を
パースして位置を取り出す必要がある。この文字列フォーマットへの暗黙的な依存が
問題になった場合は、あらためて0008を見直しFailureの型を構造化する案（アプローチC）
を再検討する。

## Related
- [0008-smiles-parser-result-type](./0008-smiles-parser-result-type.md)
- [0015-unsupported-notation-specific-error](./0015-unsupported-notation-specific-error.md)
