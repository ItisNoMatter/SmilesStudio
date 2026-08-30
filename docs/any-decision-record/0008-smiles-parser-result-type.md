# 0008: SmilesParserの失敗をsealed classのParseResultで表現する

- Date: 2026-08-29
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるcore-smilesドメインモデルのグリリングセッション（Q5）で、
`SmilesParser.parse(smiles: String): Molecule`が`TODO()`のままで、不正なSMILESを
パースした際の失敗の契約が未定義であることが議題になった。CLAUDE.mdの複数案提示
ルールに従い、Claude Codeが3案を提示し、ユーザーが選択した。

## Decision
`SmilesParser.parse`の戻り値を`sealed class ParseResult { Success(Molecule); Failure(reason) }`
のような型にする。失敗を例外や`null`ではなく、失敗理由を構造化して持てる正常系の一つとして
型で表現する。

## Alternatives
- **アプローチA: 例外を投げる（`SmilesParseException`など）** — Kotlin標準の
  `try/catch`に乗せられるが、Kotlinにはchecked exceptionが無いため呼び出し側の
  catch忘れを型チェックで検出できない。またSMILES入力欄からのリアルタイム
  バリデーションのような、失敗が頻繁に起こりうるユースケースには不向きなため不採用。
- **アプローチC: `Molecule?`（null許容）で返す** — 実装が最も単純だが、失敗理由
  （どこで・なぜ失敗したか）を一切表現できずUIのエラー表示に使えない。さらに0003で
  `hydrogenCount`の`null`が曖昧で誤用しやすいという理由から`sealed interface`に
  変更した経緯と矛盾する設計になるため不採用。

## Consequences
`SmilesParser.parse`のシグネチャが`Molecule`から`ParseResult`に変わるため、現在の
`TODO()`実装および将来書かれるテストは`ParseResult`の`Success`/`Failure`を扱う形で
書く必要がある。呼び出し側（`ui-compose`を含む）は`when`による網羅的な分岐が必要になり、
`try/catch`や`?:`のような簡便な糖衣構文は使えなくなる。

## Related
- [0003-atom-hydrogen-count-sealed-interface](./0003-atom-hydrogen-count-sealed-interface.md)
