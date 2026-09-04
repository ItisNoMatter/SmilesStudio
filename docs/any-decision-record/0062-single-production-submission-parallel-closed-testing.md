# 0062: Play Store本番提出を1回に統合し、非公開テスト14日間を機能開発と並行させる

- Date: 2026-09-04
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[AnyDR 0059](./0059-phased-release-strategy-store-review-first.md)〜[AnyDR 0061](./0061-split-store-submission-issue-and-rewire-dependencies.md)では、「初回（最小ビルド）申請」→「機能追加版申請」という2回の本番提出を想定していた。しかし`/grill-with-docs`セッション中の事実調査で、Google Playの新規個人開発者アカウント（2023-11-13以降作成）には、本番公開の前に「12人以上のテスターによる14日間連続の非公開テスト」が別途必須であることが判明した。ユーザーに確認したところ、SmilesStudio用のGoogle Play Console開発者アカウントは新規作成中であり、この要件が適用される。この14日間はビルドの完成度と無関係に開始・経過させられ、機能開発と完全に並行できるため、当初の「早く審査を通す」という目的の実体は、この非公開テスト期間の早期開始にあることが分かった。これを踏まえ、2回本番提出する設計を見直した。

## Decision

最小ビルド（[Issue #6](https://github.com/ItisNoMatter/SmilesStudio/issues/6)・[Issue #7](https://github.com/ItisNoMatter/SmilesStudio/issues/7)・[Issue #13](https://github.com/ItisNoMatter/SmilesStudio/issues/13)）が完成し次第、Google Play非公開テストトラックを開始し12人以上のテスターで14日間のクロックを回す。テスト期間中に[Issue #14](https://github.com/ItisNoMatter/SmilesStudio/issues/14)〜[Issue #17](https://github.com/ItisNoMatter/SmilesStudio/issues/17)（手描き認識・課金）を並行実装する。本番（Production）への提出は、非公開テスト完了（本番アクセス許可）と機能実装の両方が揃った時点で、1回のみ行う。

[AnyDR 0061](./0061-split-store-submission-issue-and-rewire-dependencies.md)で決めたIssue分割自体は維持するが、意味合いを以下のように改める:
- [Issue #18](https://github.com/ItisNoMatter/SmilesStudio/issues/18): 「初回（最小ビルド）本番申請」から「Google Play非公開テストの開始・運用（テスター募集、14日間の実施）」に再定義する。ブロッカーは#6・#7・#13のまま。
- 新設Issue「本番申請（機能追加版）」: #18（非公開テスト完了・本番アクセス許可）と#14〜#17（機能実装完了）の両方にブロックされる、単一の本番提出Issueとする。

## Alternatives

- AnyDR 0061のまま、最小ビルドを単独で一度本番提出してから機能追加版を再提出する（2回本番提出）: 万一#14〜#17の実装が遅延しても最小ビルドは既に本番公開済みという安心材料が得られる利点があったが、本番提出が2回になり2回目のアップデート提出にも審査待ち時間が発生すること、テキスト入力のみの最初のバージョンがShipatonの価値提案（手描き認識）と一致しない状態でストアに公開される期間が生じることから、非公開テスト期間の並行化で同じ目的（早期に審査クロックを開始する）を達成できるアプローチAを優先し不採用。

## Consequences

- Issue #18の名称・説明文を「非公開テストの開始・運用」に更新し、新設Issueを「本番申請（機能追加版）」として#18と#14〜#17の両方にブロックされる形で作成する。
- テスターの確保（12人以上、14日間の継続的な関与）が新たな実務上のリスクとして残る。ユーザーが対応を引き受けた。
- 14日間で#14〜#17の実装が終わらなかった場合、非公開テスト自体はやり直し不要だが、本番提出のタイミングが計画よりずれ込む。

## Related

- [0059-phased-release-strategy-store-review-first](./0059-phased-release-strategy-store-review-first.md)
- [0060-minimal-build-scope-rendering-pipeline-only](./0060-minimal-build-scope-rendering-pipeline-only.md)
- [0061-split-store-submission-issue-and-rewire-dependencies](./0061-split-store-submission-issue-and-rewire-dependencies.md)
