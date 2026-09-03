# 0003: /grill-with-docsでcore-smilesドメインモデルを深掘りしSkill運用基盤を整備

- Date: 2026-08-31
- Related AnyDR: 0003, 0004, 0005, 0006, 0007, 0008, 0009

## Objective

core-smilesの初期スキャフォールド後、Atomの水素数表現・芳香族性の扱い・Ring概念・AtomIdの
安定性・SmilesParserの結果型・逆変換（canonical writer）の要否など、ドメインモデル上の未確定
点を`/grill-with-docs`で詰める。

## Action

`/grill-with-docs`によるグリリングセッションを複数ラウンド実施した（Q3: Ring概念の要否、Q4:
AtomId安定性、Q6: canonical writerの要否 等）。domain-modeling Skillの流儀に沿って`CONTEXT.md`
を新設し、用語が確定するたびにその場で反映した。issue-tracker運用ドキュメント
（`docs/agents/domain.md`, `docs/agents/issue-tracker.md`）も合わせて整備した。確定した決定を
AnyDR 0003〜0009として記録した。

## Result

HydrogenCountのsealed interface化、芳香族性をBondから導出しlazyキャッシュする方針、Ringを
導出概念として定義、AtomId安定性は未定義のままCONTEXT.mdに明記、SmilesParserの結果型
（ParseResult）、canonical writerは当面スコープ外、という7件の決定が確定した。`9f92ff0`で
一括反映した。

## Reflections

「Ring」や「AtomId安定性」のような、まだ実装が存在しない概念について先に用語だけをCONTEXT.md
に定義しておく（0006/0007のパターン）ことで、後から機能が必要になった際に無自覚な前提を持ち込む
リスクを低コストで防げるとわかった。実装を急がず「未定義であることを明記する」という選択肢自体
が有効な決定になりうる。
