# 0018: 芳香族結合のKekulize変換（Issue #6）をTDDで実装した

- Date: 2026-09-04
- Related AnyDR: 0065
- Related Issue: #6

## Objective

直近の作業計画見直し（AnyDR 0059〜0063）で最小ビルドのスコープに含まれると確定した、Issue #6
（core-smiles: 芳香族結合のKekulize変換）を完了させる。AnyDR 0020（芳香族結合の描画はKekulé
構造）に基づく実装。

## Action

実装前にCLAUDE.mdの複数案提示ルールに従い、Kekulize変換の結果表現について2案（独立関数で
`List<Bond>`を返す vs `Map<Bond, BondType>`を返す）を提示し、AnyDR 0045の`computeLayout`と
同じ独立関数パターンを選択した（AnyDR 0065）。検討から除外した第3の案（`Molecule`を直接
書き換える）も、AnyDR 0020の「Kekulé表現は描画上の近似であり化学的な芳香族性（AnyDR 0004の
SSOT）とは別物」という理由とあわせてAnyDR 0065に明記した。

TDDで`Kekulize.kt`を実装した: `fun kekulize(molecule: Molecule): List<Bond>`。ベンゼン・
ピリジンの交互パターン、隣接する2つの結合が両方二重結合にならないこと、環に結合した非芳香族
置換基が変化しないこと等5件のテストを先に書き、Red確認後に実装しGreenにした。

## Result

`./gradlew allTests`がグリーン。Issue #6完了。コミット`49522cb`・push完了。

## Reflections

特に大きな驚きはなく、設計フォークの提示→選択→TDD実装という、このプロジェクトで確立された
通常運転の一サイクルだった。ただし「検討から除外した案」もAnyDRに含めてほしいというユーザー
からの明示的な指示があり、通常のPros/Cons（採用案＋比較検討した代替案）だけでなく、比較の
土俵にすら乗せなかった案とその理由も記録する価値があるという運用上の学びがあった。
