# 0021: 配布形態はjpackageベースのネイティブインストーラをGitHub Releasesで配布し、まずWindows向けのみとする

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

「プロジェクト全体の進め方の構想を整理してissue化したい。最小構成でユーザーにリリースできるところまで」というゴールに向けたグリリングセッションの中で、[[0017]]〜[[0020]]によりコア体験・文法スコープ・レイアウト・描画方針が確定した。残る大きな論点として、「ユーザーにリリース」を実際にどう届けるか（配布形態・対象プラットフォーム）を決める必要があった。

## Decision

配布形態は、Compose for Desktopの`compose.desktop`Gradleプラグインが標準提供するjpackageベースの配布タスク（`packageDistributionForCurrentOS`等）でネイティブインストーラ/実行可能ファイルを生成し、GitHub Releasesで配布する。対象プラットフォームはv1ではWindowsのみとする。

## Alternatives

- ソースからの起動のみ（`./gradlew :desktop-app:run`）: 実装コストゼロで今すぐ「リリース可能」と言えるが、Gradle/JDKのセットアップができない一般ユーザー（非開発者の化学徒）には配布できず、実質「開発者向け」の配布形態にとどまる。「ユーザーにリリース」という当初のゴールの達成度としては弱いため不採用。
- 単一runnable jar（fat/shadow jar）で配布: OSごとのインストーラよりビルドがシンプルだが、実行にユーザー側のJDKインストールと`java -jar`実行が必須で「ダブルクリックで起動」という体験にはならず、非開発者には依然ハードルが高い。Compose for DesktopのようなGUIアプリではあまり一般的な配布形態でもないため不採用。

## Consequences

- OSごとに異なる形式でビルド・動作確認が必要になるが、v1はWindowsのみに絞ることでこの手間を最小化する。
- GitHub Releasesへのアップロード運用（バージョニング、リリースノート作成）という新たな運用プロセスが必要になる。
- macOS/Linux向け配布は、需要が出た時点でのv1.1以降の拡張として温存する。

## Related

- [0017-v1-text-input-readonly-rendering-scope](./0017-v1-text-input-readonly-rendering-scope.md)
- [0018-v1-grammar-scope-rings-and-aromatics](./0018-v1-grammar-scope-rings-and-aromatics.md)
