# 0001: core-smilesのドメインモデルをIDベース設計で採用

- Date: 2026-08-27
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
SmilesStudioの`core-smiles`モジュールにおいて、SMILES構造式を表現する化学ドメインモデル
（原子・結合・分子）のデータ構造を設計する必要があった。CLAUDE.mdの複数案提示ルールに従い、
Claude Codeが複数のアプローチを提示し、ユーザーが選択する形で決定した。

## Decision
`AtomId`という`@JvmInline value class(Int)`を導入し、`Atom`は自身の`id: AtomId`を持つ。
`Molecule`は`atoms: Map<AtomId, Atom>`で原子を保持し、`Bond`は`atom1`/`atom2`を`AtomId`の
参照で表現する（IDベース設計、いわゆる「アプローチB」）。`BondType`はenum
(SINGLE, DOUBLE, TRIPLE, AROMATIC等)を想定する。

```kotlin
@JvmInline
value class AtomId(val value: Int)

data class Atom(
    val id: AtomId,
    val element: Element,
    val charge: Int = 0,
    val isotope: Int? = null,
    val isAromatic: Boolean = false,
    val hydrogenCount: Int? = null
)

data class Bond(
    val atom1: AtomId,
    val atom2: AtomId,
    val type: BondType
)

data class Molecule(
    val atoms: Map<AtomId, Atom>,
    val bonds: List<Bond>
)
```

`Element`の具体形（enumで有機サブセット中心に定義する想定）は、複数案提示ルールの対象外の
軽微な実装判断として別途進める。

## Alternatives
- **アプローチA: オブジェクト参照ベース** — `Atom`が隣接する`Bond`/`Atom`への参照を直接持つ
  グラフ表現。隣接原子の走査は直感的だが、`Atom`と`Bond`が互いを参照する循環参照になり、
  data classデフォルトの`equals`/`hashCode`/`toString`がスタックオーバーフローする危険がある。
  イミュータブルに保ちづらく、1原子の変更が分子全体の再帰的な再構築を招きやすい。将来の
  Koog連携（JSON等へのシリアライズ）でも循環参照が障害になるため不採用。
- **アプローチC: インデックス（配列添字）ベース** — `AtomId`のような専用型を作らず、
  `List<Atom>`の添字（生の`Int`）で原子を参照する方式。実装がシンプルでランダムアクセスは
  O(1)だが、生の`Int`は型安全でなく他のInt値と取り違えやすい。さらに原子の削除・並べ替えで
  既存`Bond`が指すインデックスが壊れる。SMILESエディタは原子の追加・削除が頻発するUIになる
  想定のため、このリスクが実利的に大きく不採用。

## Consequences
IDベース設計はA/Cの弱点（循環参照によるイミュータビリティの崩壊、削除時のインデックス崩壊）
を回避できる一方、隣接原子の探索には`Map`ルックアップ相当のコストがかかり、`AtomId`の採番・
再利用ロジックを自前で管理する必要がある。`Element`・`BondType`の具体的な列挙値は本決定とは
別に、実装時の軽微な判断として定める。
