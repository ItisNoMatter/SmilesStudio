# 0002: プロジェクトの初期スキャフォールドとcore-smilesの最初のアーキテクチャ決定

- Date: 2026-08-27
- Related AnyDR: 0001, 0002

## Objective

SmilesStudioのKMPプロジェクトの土台を作り、SMILES構造式を表現するドメインモデル（原子・結合・
分子）の最初の設計判断を下す。

## Action

CLAUDE.mdの複数案提示ルールに従い、`Molecule`のデータ構造についてオブジェクト参照ベース・ID
ベース・インデックスベースの3案を提示し、ユーザーがIDベース設計（`AtomId` value class +
`Map<AtomId, Atom>`）を選択した（AnyDR 0001）。合わせて`core-smiles`/`ui-compose`を真のKotlin
Multiplatformモジュールとして構成するかどうかも複数案（JVM統一/真のKMP/折衷）で比較し、真のKMP
構成を採用した（AnyDR 0002）。この2つの決定を反映した状態で、`core-smiles`/`ui-compose`/
`desktop-app`の3モジュール構成、Atom/AtomId/Bond/BondType/Element/Moleculeの初期実装、
SmilesParserのダミー実装、MoleculeTestを一括でコミットした。

## Result

`48f64e4`でプロジェクトの初期スキャフォールドが完成した。以降すべての開発がこの土台の上で進む。

## Reflections

プロジェクトの一番最初のコミットの時点で、後から変更コストの高いアーキテクチャ判断（ドメイン
モデルの表現方式、KMPモジュール構成）を先に複数案比較で決め切ったことで、後続の作業（Android
対応追加など）で構成の作り直しが発生しなかった。
