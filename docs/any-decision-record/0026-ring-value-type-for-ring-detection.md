# 0026: Ring検出結果は専用の値型`Ring(atoms: List<AtomId>)`で表現する

- Date: 2026-09-02
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[GitHub Issue #4](https://github.com/ItisNoMatter/SmilesStudio/issues/4)（core-smiles: Ring検出アルゴリズムの実装）の実装に着手するにあたり、CLAUDE.mdの方針に従い実装前に結果表現の選択肢を提示した。`Ring`は[[0006]]で「`Molecule`の保存フィールドとしては持たず、クエリ時に導出する」ドメイン概念と定義済みであり、`CONTEXT.md`にも用語として登録されている。検出アルゴリズム自体は、v1スコープ（[[0018]]・[[0019]]により単環・非縮合環に限定）を踏まえ、DFSで背後辺（back edge）を検出する方式を採用する（縮合環にも対応する本格的なSSSR系アルゴリズムは過剰と判断）。論点は、検出結果をどう型で表現するかだった。

## Decision

Ring検出結果は、専用の値型`data class Ring(val atoms: List<AtomId>)`（環を一周する順序付きのAtomIdリスト）で表現する。`Molecule`に`rings: List<Ring>`（`by lazy`キャッシュ）を追加する。

## Alternatives

- 生の`List<List<AtomId>>`をクエリ関数から直接返す: 新しい型を追加せずに済みコード量が最小という利点があったが、内側の`List<AtomId>`が何を表すか（順序に意味があるのか、環を一周するパスなのか）が型からは読み取れず、`AtomId`・`HydrogenCount`など既存のドメイン概念にはすべて専用の型を与えてきた本プロジェクトの一貫した設計スタイルから外れるため不採用。

## Consequences

- `Molecule.rings`は`bondsByAtom`・`aromaticAtomIds`と同様、`by lazy`でキャッシュされた派生プロパティとして実装する。
- `Ring.atoms`の順序は、Issue #5（2Dレイアウト計算）が環を正多角形として配置する際にそのまま利用でき、Issue #6（芳香族結合のKekulize変換）が環を構成する`Bond`を隣接ペアから導出する際にも利用できる。
- v1スコープでは縮合環・橋かけ環を検出対象としない。将来これらに対応する必要が生じた場合、DFS背後辺方式では正しい最小環集合（SSSR）を保証できないため、アルゴリズムを再訪する必要がある。

## Related

- [0006-ring-as-derived-domain-term](./0006-ring-as-derived-domain-term.md)
- [0018-v1-grammar-scope-rings-and-aromatics](./0018-v1-grammar-scope-rings-and-aromatics.md)
- [0019-fixed-angle-2d-layout-algorithm](./0019-fixed-angle-2d-layout-algorithm.md)
