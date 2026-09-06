# 0084: Play Console初回アップロードは#24・#25を除外せずmainブランチそのままとする

- Date: 2026-09-06
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

Issue #18（Google Play非公開テストの開始・運用）を`/grill-with-docs`で進める中で、最初に
Play Consoleへアップロードするビルドのスコープを確認する必要があった。[AnyDR 0060]
(./0060-minimal-build-scope-rendering-pipeline-only.md)は初回提出ビルドを「#6・#7・#13の
みを含む最小ビルド」と定義していたが、その後のセッションでIssue #24（Safe Area対応）・
Issue #25（Material 3 Expressiveデザイン実装）を実装済みで、いずれもmainブランチに
マージ済み、かつGitHubマイルストーンには未割り当て（Phase 1に正式には含まれていない）
という状態だった。

## Decision

Play Console初回アップロードは、#24・#25を除外せず、現在のmainブランチをそのまま使う。

## Alternatives

- #24・#25を除外したAnyDR 0060原典のスコープでビルドし直す: AnyDR 0060の決定を厳密に
  守れるという利点はあったが、#24・#25はどちらも新機能ではなく「#6/#7/#13が実現する
  最小体験（テキスト入力→構造式描画）」自体の品質向上（システムバー対応・デザイン刷新）
  であり、AnyDR 0060が本来避けたかった「実装不確実性の高い新機能（手描き認識等）を
  クリティカルパスに乗せない」という趣旨には抵触しない。すでにmainにマージ済みで別
  ブランチも存在しないため、除外するには一時的なrevertコミットを作成してビルドし、
  直後にrevertを取り消すという余分な作業が必要になり、得られる利益に見合わないと
  判断し不採用。

## Consequences

- AnyDR 0060の「最小ビルド」という表現は、今後「機能面のスコープ」を指すものであり、
  同じ機能スコープ内の品質改善（UI/UXポリッシュ）まで凍結する趣旨ではない、という
  解釈が本AnyDRにより明文化された。
- Play Console非公開テストのテスターは、Material 3 Expressiveデザイン・Safe Area対応
  込みの状態を最初から見ることになる。

## Related

- [0060-minimal-build-scope-rendering-pipeline-only](./0060-minimal-build-scope-rendering-pipeline-only.md)
- [0059-phased-release-strategy-store-review-first](./0059-phased-release-strategy-store-review-first.md)
