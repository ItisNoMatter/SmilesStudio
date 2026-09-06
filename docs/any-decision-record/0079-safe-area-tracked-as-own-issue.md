# 0079: Safe Area（システムバー・ディスプレイカットアウト）対応を独立Issue化する

- Date: 2026-09-06
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

Issue #11のクローズ判断・実機検証（AnyAR 0026）の過程で、`android-app`の`MainActivity.kt`・
`ui-compose`の`MoleculeEditor`にはシステムバー（ステータスバー・ナビゲーションバー）や
ディスプレイカットアウトを考慮した`WindowInsets`対応が一切ないことが分かっていた。その場では
「ActionBarがCompose UIの先頭を覆い隠す」という顕在化していたバグのみを`NoActionBar`テーマの
追加で対処し、根本的なインセット対応は保留していた。

プロジェクトの`compileSdk`/`targetSdk`はすでに37（Android 16相当）で、API 35以降は
edge-to-edge表示がプラットフォーム側で強制されるため、この保留は放置できない実質的なリスクで
あると判断し、[Android公式ガイド「Layout basics」](https://developer.android.com/design/ui/mobile/guides/layout-and-content/layout-basics)
と[「Edge-to-edge design」ガイド](https://developer.android.com/design/ui/mobile/guides/layout-and-content/edge-to-edge)
を参照した上で、対応方針を検討した。

## Decision

Safe Area対応を、今すぐ手を入れるのではなく、独立したGitHub Issue（[#24](https://github.com/ItisNoMatter/SmilesStudio/issues/24)
「android-app: Safe Area（システムバー・ディスプレイカットアウト）対応」）として切り出し、
Issue #11のsub-issueとして紐づける。スコープは、`MainActivity`のCompose contentへの
`WindowInsets.safeDrawing`等の適用、`MoleculeEditor`のTextField（画面上端）がステータスバーと
重ならないようにすること、ソフトウェアキーボード表示時にフォーカスした入力欄が隠れないことの
確認、ジェスチャー/3ボタン両方のナビゲーションでの見た目確認、の4点とした。

## Alternatives

- その場（ActionBar修正と同じセッション）で即座にインセット対応まで実装する: 発見した瞬間に
  直したくなる誘惑はあったが、当時のタスクはあくまで「Issue #11をcloseできる状態か」の棚卸しと
  署名設定であり、スコープが際限なく広がってしまう。プロジェクトの既存の運用（Wayfinder方式、
  タスクは必ずIssue化してから着手する）にも反するため不採用。
- 対応不要と判断し記録もしない: targetSdk 37でedge-to-edgeが強制される以上、実機・実際のテスター
  環境（Issue #18の非公開テスト）で表示崩れとして表面化するリスクが具体的にあり、放置は
  不適切と判断し不採用。

## Consequences

- Issue #24が解決されるまで、`MoleculeEditor`のTextField等がステータスバー・カットアウトと
  重なる可能性が残る。Issue #18の非公開テスト運用と並行して着手できるが、実機バリエーション
  （ノッチ・パンチホール・ジェスチャーナビゲーション）でのUI崩れは現状未検証のまま。
- `ui-compose`の`MoleculeEditor`は共有Composableのため、インセット対応の実装次第では
  `desktop-app`側への影響（そもそもデスクトップにはシステムバー概念がないため通常は無関係だが、
  Modifier構成を共有している部分に注意）を確認する必要がある。

## Related

- [0027-android-app-module-for-shipaton-2026](./0027-android-app-module-for-shipaton-2026.md)
- [0071-molecule-canvas-responsive-scaling-only](./0071-molecule-canvas-responsive-scaling-only.md)
