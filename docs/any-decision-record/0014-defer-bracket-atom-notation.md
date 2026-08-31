# 0014: 角括弧原子表記（水素数指定・電荷・同位体）は今回のスコープに含めない

- Date: 2026-09-01
- Status: Accepted
- Category: rejected
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるグリリングセッションで、0012により最初のスコープが
「直鎖+分岐」に決まったが、角括弧原子表記（例: `[CH3]`、`[NH4+]`、`[13C]`）を
このスコープに含めるかどうかは明記されておらず、`HydrogenCount.Explicit`（0003で
定義済み、CONTEXT.mdにも記載）のパスがこのままでは今回全く検証されないことが
議題になった。CLAUDE.mdの複数案提示ルールに従い、Claude Codeが3案を提示し、
ユーザーが選択した。

## Decision
角括弧原子表記（水素数指定・電荷・同位体のいずれも）は今回の「直鎖+分岐」
イテレーションのスコープに含めない。有機サブセット文字（C, N, O, F, P, S, Cl,
Br, I）のみを対象とし、水素数は常に`HydrogenCount.Implicit`として扱う。

## Alternatives
- **アプローチB: 水素数指定のみ含める** — `[CH3]`のような水素数指定のみ対応し、
  電荷・同位体（`[NH4+]`、`[13C]`）は除く案。`HydrogenCount.Explicit`/`Implicit`
  両方を今回検証でき0003の型の意義を早期に実証できる利点はあったが、Tokenizerに
  角括弧のネスト解析・数字パースが増えて複雑化する上、「電荷・同位体だけ除く」
  という中途半端な境界線の説明が必要になるため不採用。
- **アプローチC: 角括弧表記をフルサポート** — 水素数+電荷+同位体を一度に実装する案。
  一回で完成させられる利点はあるが、0012で確定した「小刻みTDD」の精神に反し
  スコープが再び肥大化するため不採用。

## Consequences
`HydrogenCount.Explicit`の検証は次のイテレーション（角括弧原子表記への対応）まで
持ち越される。Tokenizer（0013）が今回扱う元素トークンは有機サブセット文字のみで
よく、角括弧の開始・終了、水素数・電荷・同位体を表すトークン種別の追加は
将来の拡張作業として残る。

## Related
- [0012-smiles-parser-initial-scope-chain-and-branches](./0012-smiles-parser-initial-scope-chain-and-branches.md)
- [0013-smiles-parser-tokenizer-separation](./0013-smiles-parser-tokenizer-separation.md)
- [0003-atom-hydrogen-count-sealed-interface](./0003-atom-hydrogen-count-sealed-interface.md)
