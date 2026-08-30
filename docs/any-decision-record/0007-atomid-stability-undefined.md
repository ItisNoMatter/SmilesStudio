# 0007: AtomIdの編集をまたいだ安定性は未定義とし、CONTEXT.mdに明記する

- Date: 2026-08-29
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるcore-smilesドメインモデルのグリリングセッション（Q4）で、
`AtomId`が「SMILES文字列中の出現順インデックス」なのか「（将来のインタラクティブ
エディタで）編集をまたいで安定する識別子」なのか、意味が未確定であることが議題になった。
`ui-compose`が将来`AtomId`に選択状態などを紐付ける可能性があり、この意味の違いは重要
だった。CLAUDE.mdの複数案提示ルールに従い、Claude Codeが3案を提示し、ユーザーが選択した。

## Decision
`AtomId`は現時点では「パース結果内で一意な識別子」であることのみを保証し、編集をまたいだ
安定性は保証しない。ただし「安定性は未定義である」という事実そのものを`CONTEXT.md`に
明記する（グリリングQ4のアプローチC）。実装自体は変更せず、既存の`AtomId(value: Int)`を
維持する。

## Alternatives
- **アプローチA: 現状維持（実装もドキュメントも変更しない）** — 実装コストは最も低いが、
  「安定性が未定義である」という事実が暗黙のままになり、将来`ui-compose`が編集機能を
  実装する際に無自覚に`AtomId`の安定性を仮定してしまう事故が起きやすいため不採用。
- **アプローチB: 今のうちに編集をまたいで安定する識別子として設計する**
  （モノトニックカウンタで採番し、削除後もID再利用しない・並べ替えでも不変、という契約を
  今から約束する） — `core-smiles`にはまだ原子の追加・削除・並べ替えAPI自体が存在せず、
  実際のAPI形状が見えないまま契約を確定させる推測に基づく設計になり、YAGNI違反のリスクが
  あるため不採用。

## Consequences
実装は変更しないため`core-smiles`側の追加コストはない。`CONTEXT.md`に「編集をまたいだ
安定性は未定義」と明記したことで、将来`ui-compose`が編集機能（選択状態やハイライトなど）を
`AtomId`に紐付けようとした際、この前提を無自覚に仮定するリスクを低コストで防げる。実際に
編集APIが必要になった時点で、`core-smiles`がその要件を受けて`AtomId`の安定性契約を
あらためて設計し直す必要がある。

## Related
- [0001-core-smiles-id-based-domain-model](./0001-core-smiles-id-based-domain-model.md)
