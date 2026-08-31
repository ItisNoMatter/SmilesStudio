# 0012: SmilesParser本体実装は直鎖+分岐のみを最初のスコープとする

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるグリリングセッションで、`SmilesParser.parse()`の実際の構文解析
ロジック（現状は`ParseResult.Failure("SMILES parsing is not implemented yet")`を返す
ダミー実装）に着手するにあたり、最初のTDDイテレーションでどこまでの文法スコープを
対象にするかが議題になった。CLAUDE.mdの複数案提示ルールに従い、Claude Codeが3案を
提示し、ユーザーが選択した。

## Decision
最初のTDDイテレーションでは「直鎖+分岐」（例: `CCO`（エタノール）、`CC(=O)O`（酢酸））
のみをスコープとする。結合記号（`=`、`#`など）と括弧による分岐は扱うが、環閉包記法
（例: `C1CCCCC1`）と芳香族小文字表記（例: `c1ccccc1`）は次段階に送り、今回は実装しない。

## Alternatives
- **アプローチA: 単一原子のみ**（`C`、`[NH4+]`など） — 最小実装でRed→Greenを回せ、
  既存の`HydrogenCount`推定ロジックに集中できる点はメリットだが、`CCO`のような
  非環式分子すら一つも描けず、進捗確認上もユーザー体験上も手応えが薄いため不採用。
- **アプローチC: フルグラマー一式**（分岐+環+芳香族+電荷/同位体などの角括弧拡張を
  一度に実装） — 後からの手戻りが少ない利点はあるが、CLAUDE.mdが指定する
  「息を吸うようにTDD」（小さなRed-Greenサイクル）の精神に反する粒度の大きさであり、
  かつ0006でRing検出を「必要になった時点で着手」とすでに方針決定していることと
  直接矛盾するため不採用。

## Consequences
非環式分子であれば実用上十分な範囲をパースできるようになる一方、環閉包数字・芳香族
小文字表記に遭遇した際に`SmilesParser`がどう振る舞うか（`ParseResult.Failure`として
拒否するのか、単に未対応として別扱いにするのか）は本決定ではまだ扱っておらず、
別途決める必要がある。また「分岐だけは先に許す」というスコープ境界は今回の判断であり、
次のイテレーションで環閉包・芳香族表記に対応する際にあらためて設計を拡張する。

## Related
- [0006-ring-as-derived-domain-term](./0006-ring-as-derived-domain-term.md)
- [0008-smiles-parser-result-type](./0008-smiles-parser-result-type.md)
- [0009-defer-canonical-smiles-writer](./0009-defer-canonical-smiles-writer.md)
