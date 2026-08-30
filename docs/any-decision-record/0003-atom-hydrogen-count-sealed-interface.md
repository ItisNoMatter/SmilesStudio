# 0003: Atom.hydrogenCountをsealed interfaceで表現

- Date: 2026-08-29
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`/grill-with-docs`によるcore-smilesドメインモデルのグリリングセッション中、`Atom.hydrogenCount: Int?`
（0001で導入）の意味を確定させる必要が生じた。`null`は「SMILES上で明示されておらず原子価規則から
暗黙的に補完される」ことを意図していたが、その意図が型からは読み取れず、コードを読んだだけでは
「未計算」なのか「暗黙的に補完される」のか区別できないという課題があった。CLAUDE.mdの複数案提示
ルールに従い、Claude Codeが3案を提示し、ユーザーが選択した。

## Decision
`Atom.hydrogenCount`の型を`Int?`から専用の`sealed interface`に変更する。

```kotlin
sealed interface HydrogenCount {
    data object Implicit : HydrogenCount
    data class Explicit(val count: Int) : HydrogenCount
}
```

`Atom.hydrogenCount: HydrogenCount`（デフォルト`Implicit`）とし、`Implicit`はSMILES上で水素数が
明示されておらず原子価規則から補完されることを、`Explicit(count)`はSMILESの角括弧記法
（例: `[CH3]`）で明示された水素数（0を含む）を表す。

## Alternatives
- **アプローチB: 値クラス＋センチネル値** — `@JvmInline value class HydrogenCount`の内部に`-1`を
  「暗黙」を表すセンチネル値として持たせる方式。JVM上ほぼゼロコストだが、`-1`という魔法の値が
  「`null`を`0`と誤解する」のと同種の誤用リスクを別の形で持ち込み、`when`による網羅性チェックも
  効かないため不採用。
- **アプローチC: `Int?`のまま維持し命名とアクセサ関数で意図を明確化** — フィールド名を
  `explicitHydrogenCount`にし、`Atom.impliedHydrogenCount()`のような関数で暗黙値を解決する方式。
  変更が最小限で済むが、「コードを読んだだけで分かる」というユーザーの要件を型システムでは
  保証できず、`hydrogenCount ?: 0`のような誤用をコンパイラが防げないため不採用。

## Consequences
呼び出し側（将来の`ui-compose`を含む）は`?:`による簡便なnull合体ではなく、`when`による
網羅的なパターンマッチが必須になる。`Implicit`時に実際の水素数を原子価規則から算出するロジック
自体はこの型に含まれないため、`Atom`/`Element`側に別途実装が必要。既存の
`Atom.hydrogenCount: Int? = null`フィールドおよび`MoleculeTest`の該当テスト
（"atom defaults reflect a neutral non-aromatic atom with unspecified hydrogen count"）は
型変更に伴い修正が必要。また、`CONTEXT.md`には**Implicit Hydrogen Count**と
**Explicit Hydrogen Count**という対概念を用語として記録する。

## Related
- [0001-core-smiles-id-based-domain-model](./0001-core-smiles-id-based-domain-model.md)
