# 0052: AnyARは`docs/any-action-record/`内で独自の連番を持つ

- Date: 2026-09-03
- Status: Accepted
- Category: naming
- Deciders: the user, Claude Code

## Context

[[0050]]・[[0051]]で`any-action-record`（AnyAR）の主目的・自動発火方式を確定した後、採番方式を検討した。AnyDR（`docs/any-decision-record/`）は既に0001から始まる連番を持っており、AnyARがこれと同じ採番空間を共有するか、独自の連番を持つかが論点だった。

## Decision

AnyARは`docs/any-action-record/`内で0001から始まる独自の連番を持つ。AnyDRの採番空間とは完全に分離する。

## Alternatives

- AnyDRと同じ採番空間を共有する（次のAnyARを既存AnyDRの続き番号から開始）: プロジェクト全体で番号が常に一意になり曖昧さがないという利点があったが、`any-action-record`Skillが次番号を決める際に`docs/any-decision-record/`も毎回スキャンする必要が生じ、別々に設計・配布される2つのグローバルSkill（`any-decision-record`・`any-action-record`）が結合してしまうため不採用。

## Consequences

- `any-action-record`Skillは`docs/any-action-record/`ディレクトリのみをスキャンして次番号を決めればよく、`any-decision-record`のロジックに一切依存しない。
- AnyDR番号とAnyAR番号は独立しており、同じ番号（例: AnyDR 0012とAnyAR 0012）が別内容を指しうる。参照時は常に「AnyDR」「AnyAR」と種別を明記する運用でこの曖昧さをカバーする。

## Related

- [0050-any-action-record-purpose](./0050-any-action-record-purpose.md)
- [0051-hybrid-hook-plus-judgment-trigger](./0051-hybrid-hook-plus-judgment-trigger.md)
