# 0019: 2Dレイアウトは固定角度配置アルゴリズムを自前実装する

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0018]]でv1のSMILES文法スコープに環閉包・芳香族小文字表記を含めることを決めた後、`MoleculeCanvas`（現状は空のCanvas）で構造式を描画するために、`Molecule`の原子・結合グラフから2D座標を計算するレイアウトアルゴリズムが必要になった。単純な直鎖だけでなく環構造も対象になるため、レイアウト方式の選定が実装スコープに直結する論点だった。

## Decision

2Dレイアウトは、環を正多角形として配置し、鎖・分岐は固定結合角（120度など）で伸ばすルールベースの固定角度配置アルゴリズムを自前実装する。力学モデル（force-directed layout）や外部レイアウトライブラリは採用しない。

## Alternatives

- 既存の2Dレイアウトライブラリを利用: 衝突回避や縮合環対応など質の高いレイアウトが得られる可能性がある一方、KMPの`commonMain`から使えるピュアKotlin製の分子レイアウトライブラリはおそらく存在しない（CDK等はJVM専用で、`core-smiles`の「ピュアKotlin・UI/Android非依存」という制約と整合しにくい）。依存の調査・選定自体に時間がかかり「最小構成」から外れるため不採用。
- 力学モデル（force-directed layout）を自前実装: 衝突回避が自然に組み込まれ複雑な構造でも破綻しにくい一方、実装・チューニングコストが最大で「最小構成」に反する。収束の不安定さ（実行のたびにレイアウトが変わりうる）があり決定論的なテストがしにくいため不採用。

## Consequences

- v1の文法スコープ（[[0018]]）は「単環・単純な分岐」を主な対象としており、縮合環のような複雑な構造は現時点で想定していない。固定角度配置はこの範囲であれば十分に読める図を描ける。
- 座標計算は純粋なロジックとして`core-smiles`側に置くことができ、TDDサイクルに乗せやすい。
- 縮合環など、より複雑な分子構造への対応が必要になった場合は、原子の重なり（オーバーラップ）が発生しうる。その際は本AnyDRを再訪し、衝突回避ロジックや別方式の採用を検討する。

## Related

- [0002-kmp-module-structure-core-smiles-ui-compose](./0002-kmp-module-structure-core-smiles-ui-compose.md)
- [0006-ring-as-derived-domain-term](./0006-ring-as-derived-domain-term.md)
- [0018-v1-grammar-scope-rings-and-aromatics](./0018-v1-grammar-scope-rings-and-aromatics.md)
