# 0095: BYOK設定画面への導線は既存の「その他メニュー」に追加する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #16（BYOK設定画面）を実装するにあたり、画面のどこからアクセスできるようにするかを
検討した。`SmilesStudioApp.kt`には既にTopAppBarの`more_vert`アイコンから開く「その他メニュー」
（「入力をクリア」「このアプリについて」）が実装済みだった。

## Decision

BYOK設定画面への導線は、既存の「その他メニュー」に「APIキー設定」項目を追加する形にする。
NavigationBarに新しい「設定」タブを追加する案は採用しない。

## Alternatives

- NavigationBarに「設定」タブを追加する（ホーム/使い方/設定の3タブ構成）: 設定項目が将来
  増えた場合の拡張性は高いが、現時点で設定に相当する項目はAPIキーのみでタブを新設するほどの
  ボリュームがなく、Issue #25で実装したMaterial 3 Expressiveデザインの2タブ構成・
  `AnimatedContent`切り替えの実装にも手を入れる必要が生じるため不採用とした。

## Consequences

- 既存の「その他メニュー」に項目を追加するだけで済み、ナビゲーション構造の変更が不要。
- 将来設定項目が増えてメニューが肥大化した場合は、改めて専用タブ化を検討する余地がある。

## Related

- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
