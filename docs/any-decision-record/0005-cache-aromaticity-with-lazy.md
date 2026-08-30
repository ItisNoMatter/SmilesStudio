# 0005: 芳香族性の導出計算をMoleculeの`by lazy`プロパティでキャッシュする

- Date: 2026-08-29
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
0004で`Atom.isAromatic`フィールドを廃止し、芳香族性（Aromatic Atom）を隣接する`Bond`から
導出する計算プロパティに変更したことに伴い、判定のたびにグラフ走査が発生しうるため
パフォーマンス上のキャッシュが必要になった。CLAUDE.mdの複数案提示ルールに従い、
Claude Codeが3案を提示し、ユーザーが選択した。

## Decision
`Molecule`に`by lazy`を使った遅延計算プロパティ（例: `private val aromaticAtomIds: Set<AtomId> by lazy { ... }`）
を持たせ、`molecule.isAromatic(atomId)`のようなシンプルなAPIの内部でキャッシュする。

## Alternatives
- **アプローチB: 生成時にeagerに計算してプロパティに持たせる** — 遅延評価がなくシンプルだが、
  `Molecule`を生成するたびに実際に問い合わせがあるかに関わらず必ず計算コストを先払いする
  ことになる。`SmilesParser`がパース過程で中間`Molecule`を大量生成する可能性があり、
  無駄な計算が走るリスクがあるため不採用。
- **アプローチC: `Molecule`自身はキャッシュを持たず、呼び出し側（`ui-compose`など）に
  メモ化を委ねる** — `core-smiles`のドメインモデルを完全にキャッシュレスに保てるが、
  「化学ドメインロジックは`core-smiles`が持つ」というアーキテクチャ方針に反し、
  芳香族性というドメイン知識のキャッシュ責務を呼び出し側に漏らしてしまう。将来の
  消費者（Koog連携など）ごとにキャッシュ機構を重複実装する羽目になるため不採用。

## Consequences
`Molecule`は不変（0001の決定）であるため、`by lazy`による一度きりの計算がそのまま
正しさの保証になる。Kotlinの`data class`は主コンストラクタのプロパティのみを
`equals`/`hashCode`/`copy`の対象にするため、`by lazy`の派生プロパティ（キャッシュ）は
比較対象に含まれない（今回は意図した挙動）。デフォルトの`by lazy`はスレッドセーフモード
（内部でロックを取る）のため、マルチスレッドから頻繁に呼ばれる場合は僅かなオーバーヘッドが
ある。

## Related
- [0001-core-smiles-id-based-domain-model](./0001-core-smiles-id-based-domain-model.md)
- [0004-derive-aromaticity-from-bonds](./0004-derive-aromaticity-from-bonds.md)
