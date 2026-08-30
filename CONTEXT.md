# SmilesStudio

化学徒向けSMILES記法エディタが扱う、分子構造（原子・結合）とSMILES記法のパースに関するドメイン。

## Language

**Implicit Hydrogen Count**:
原子に結合する水素の数のうち、SMILES文字列上で明示されておらず、元素の原子価規則から自動的に
補完される状態。`Atom.hydrogenCount`が`HydrogenCount.Implicit`であることに対応する。
_Avoid_: unspecified hydrogen count, null hydrogen count

**Explicit Hydrogen Count**:
SMILESの角括弧記法（例: `[CH3]`）で明示的に指定された水素の数（0を含む）。
`Atom.hydrogenCount`が`HydrogenCount.Explicit(count)`であることに対応する。
_Avoid_: hydrogen count override

**Aromatic Atom**:
環内で非局在化したπ電子系に属する原子。この状態は原子自身が保持するのではなく、
隣接するすべての`Bond`がAromatic Bondであることから導出される（Single Source of Truth）。
_Avoid_: isAromaticフラグ, 芳香族フラグ

**Aromatic Bond**:
環内で非局在化したπ電子系を構成する結合。SMILES上では小文字表記の原子間結合
（例: `c1ccccc1`）に対応する。`BondType.AROMATIC`がこれを表す。
_Avoid_: 芳香結合

**Ring**:
分子グラフ中の閉路（同じ原子に戻ってくる結合の経路）。SMILESの環閉包記法
（例: `C1CCCCC1`）に対応する。`Molecule`の保存フィールドとしては持たず、原子と結合の
グラフから必要に応じて導出するクエリ時の概念とする。
_Avoid_: 環構造, cycle

**AtomId**:
`Molecule`内で原子を一意に参照するための識別子。現時点ではパース結果内で一意であることのみを
保証し、（将来のインタラクティブエディタ機能を想定した）編集をまたいだ安定性は未定義・
未保証である。
_Avoid_: 原子インデックス, atom index
