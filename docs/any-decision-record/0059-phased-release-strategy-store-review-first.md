# 0059: リリース戦略を段階的リリースに転換し、ストア審査を最優先事項とする

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

ユーザーから「MVPでストアの審査を通すことを最優先事項とし、そのあとに課金の導線などをつくっていく形にしたい」という作業計画見直しの依頼があった。`/grill-with-docs`によるグリリングセッションの最初の論点として、CLAUDE.mdやAnyDR 0028で「MVP」が手描き構造式認識機能を指す用語として使われている一方、CONTEXT.mdには正式な用語定義がなく、「ストア審査を最優先」の意味が「最小ビルドを先に出す」のか「完成形のまま順序だけ早める」のか曖昧だったため、Pros/Cons形式で選択肢を提示した。

## Decision

リリース戦略を「段階的リリース」に転換する。Shipaton 2026対応（[Issue #11](https://github.com/ItisNoMatter/SmilesStudio/issues/11)）の完成形（手描き構造式認識・課金機能を含む）を一括でPlay Storeに提出するのではなく、まず手描き認識（[Issue #14](https://github.com/ItisNoMatter/SmilesStudio/issues/14)・[Issue #15](https://github.com/ItisNoMatter/SmilesStudio/issues/15)）・課金（[Issue #16](https://github.com/ItisNoMatter/SmilesStudio/issues/16)・[Issue #17](https://github.com/ItisNoMatter/SmilesStudio/issues/17)）を含まない最小限のAndroidビルドをできるだけ早くPlay Storeの審査に提出し、審査待ち時間を先に消化する。審査待ち中・審査通過後に手描き認識・課金を並行実装し、Shipaton締切（2026-09-30）前に機能追加版をアップデートとして再提出する。

## Alternatives

- 一括リリース（現行計画のまま、完成形の1ビルドのみ提出する）: 提出・審査は1回で済み運用がシンプルで、「ストア審査通過」＝「Shipaton要件充足」が常に一致するという利点があったが、Play審査という外部律速要因が実装完了までまったく消化されず、今回明示された「ストア審査を最優先事項にしたい」というユーザーの要望と合わないため不採用。

## Consequences

- 提出作業が最低2回（最小ビルド→機能追加版）発生し運用の手間が増え、アップデート版にも再審査の待ち時間が発生する。
- Shipatonの参加要件（[AnyDR 0027](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0027-android-app-module-for-shipaton-2026.md): RevenueCat SDK導入・課金実装）を満たさないビルドで最初の審査を通すことになり、「ストア審査通過」と「Shipaton要件充足」の間にタイムラグが生じる。
- 最小ビルドの具体的なスコープ（手描き認識を除いた範囲で何を含めるか）は別途グリリングで決める。
- [Issue #18](https://github.com/ItisNoMatter/SmilesStudio/issues/18)（Google Play Console申請準備）の依存関係グラフ（現状#15/#17/#22にブロックされている）を今回の方針に合わせて再設計する必要がある。

## Related

- [0027-android-app-module-for-shipaton-2026](./0027-android-app-module-for-shipaton-2026.md)
- [0034-prioritize-shipaton-map-over-desktop-v1](./0034-prioritize-shipaton-map-over-desktop-v1.md)
- [0035-revisit-priority-for-rendering-pipeline](./0035-revisit-priority-for-rendering-pipeline.md)
- [0036-plan-b-c-monetization-supersedes-byok-hybrid](./0036-plan-b-c-monetization-supersedes-byok-hybrid.md)
