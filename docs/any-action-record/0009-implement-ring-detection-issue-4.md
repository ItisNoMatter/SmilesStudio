# 0009: Ring検出アルゴリズム（Issue #4）をDFS背後辺方式でTDD実装

- Date: 2026-09-02
- Related AnyDR: 0026
- Related Issue: #4

## Objective

Issue #4（core-smiles: Ring検出アルゴリズムの実装）を完了させる。

## Action

DFSで背後辺（back edge）を検出する方式（縮合環対応のSSSR系アルゴリズムはv1スコープには過剰と
判断）を採用し、検出結果を専用の値型`Ring(atoms: List<AtomId>)`で表現する方針を確定した
（AnyDR 0026）。`Molecule.rings`を`by lazy`キャッシュの派生プロパティとしてTDDで実装した。

## Result

単環・非縮合環のRing検出が動作するようになった。`f7bc955`で実装完了、Issue #4クローズ。

## Reflections

「生の`List<List<AtomId>>`を返す」という最小実装ではなく、既存の`AtomId`・`HydrogenCount`と
同じ「専用の値型を与える」設計スタイルを一貫させた。プロジェクト全体で型の一貫性を優先する
判断が積み重なっている。
