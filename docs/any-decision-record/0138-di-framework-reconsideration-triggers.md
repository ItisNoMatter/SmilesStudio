# 0138: DIフレームワーク導入を再検討する条件を明文化する

- Date: 2026-09-16
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context
[[0137-extract-smilesstudioapp-state-holder]]のリファクタリングに関連して、`android-app`がHilt/Koin/DaggerのようなDIフレームワークを導入せず手動DI（コンストラクタインジェクション＋`remember`での手動配線）に留めている理由を説明した際、「依存グラフ・画面数が増えて配線コストがスコープ管理の手間を上回ったタイミングで再検討する」という暗黙の閾値に頼っていることが分かった。ユーザーからこの閾値を明確にしておきたいという要望があった。

## Decision
以下の3条件のうち**いずれか1つでも該当したら**、DIフレームワーク（Hilt/Koin等）の導入を再検討する。

1. Activityをまたいで同じインスタンスを共有する必要が出た
2. `remember`による手動配線が3階層以上ネストした
3. Application-scopedなシングルトンが2個以上必要になった

これらに該当しない限りは、現状の手動DI（`rememberSmilesStudioAppState()`のような`remember`ベースの構築）を継続する。

## Alternatives
- **依存グラフの複雑さを数値化する案**: `remember { XxxClass(...) }`で手動構築しているオブジェクトの個数、またはコンストラクタ引数が5個を超えるクラスの数を数値の閾値にする。grep等で機械的にカウントでき客観性は高いが、「数」より「スコープ管理の複雑さ」の方が本質的な痛みになるケースを見逃す懸念があり不採用。
- **画面（Activity/Navigationルート）の数を閾値にする案**: トップレベル画面が2つ以上になったら再検討する。Navigation Component導入のタイミングと自然に一致するが、1画面内で依存グラフ自体が深くなるケースを検知できないため不採用。
- 採用した「具体的な技術的痛みの発生」案は、DIコンテナが実際に解決してくれる痛み（インスタンス共有・スコープ管理・シングルトン管理）そのものを条件にしており、将来導入する際に「なぜ今か」を説明しやすい点を決め手として選んだ。

## Consequences
- 今後の実装で上記3条件のいずれかに該当した場合は、Hilt/Koin等の導入をあらためて複数案比較のうえ検討する。
- 該当しない限りは、`android-app`内の依存構築は`remember`ベースの手動DIを標準として継続する。
- 3条件はいずれも定性的な判断を伴うため、該当するかどうかの判定自体にある程度の主観が残る。

## Related
- [0137-extract-smilesstudioapp-state-holder](./0137-extract-smilesstudioapp-state-holder.md)
