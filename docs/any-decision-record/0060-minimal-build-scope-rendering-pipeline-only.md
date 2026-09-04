# 0060: 最小ビルドのスコープをレンダリングパイプライン＋モバイル調整のみとする

- Date: 2026-09-04
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[AnyDR 0059](./0059-phased-release-strategy-store-review-first.md)で段階的リリース戦略（最小ビルドを先にストア審査へ提出）への転換を決めた後、その「最小ビルド」に具体的に何を含めるかを決める必要があった。`/grill-with-docs`セッションの中で、Pros/Cons形式で選択肢を提示した。

## Decision

初回提出ビルドは、共有レンダリングパイプライン（[Issue #6](https://github.com/ItisNoMatter/SmilesStudio/issues/6) Kekulize変換、[Issue #7](https://github.com/ItisNoMatter/SmilesStudio/issues/7) MoleculeCanvas描画実装）＋Androidモバイル向け調整（[Issue #13](https://github.com/ItisNoMatter/SmilesStudio/issues/13)）のみを含む「テキストでSMILESを入力すると構造式が描画される」アプリとする。手描き構造式認識（[Issue #14](https://github.com/ItisNoMatter/SmilesStudio/issues/14)・[Issue #15](https://github.com/ItisNoMatter/SmilesStudio/issues/15)）・課金（[Issue #16](https://github.com/ItisNoMatter/SmilesStudio/issues/16)・[Issue #17](https://github.com/ItisNoMatter/SmilesStudio/issues/17)）はどちらも含めない。

## Alternatives

- 最小ビルド＝手描き認識まで含む（課金のみ後回し）: Shipatonの本丸機能を含んだ状態でストアデビューでき初回リリース時点でアプリの価値提案が完成しているという利点、また「無料アプリを後から有料化する」更新はGoogle Play・RevenueCat双方の運用上一般的という利点があったが、実装の不確実性が最も高いKoog連携（外部Vision LLM API呼び出し、Issue #14）が依然としてクリティカルパスに残り、審査提出までの時間最小化という主目的に対して縮小効果が薄いため不採用。

## Consequences

- Koog SDK・Vision LLM連携という、このプロジェクトで初めて触る外部依存が初回審査提出のクリティカルパスから外れる。
- テキスト入力のみのアプリとしてストアデビューすることになり、初回リリースの体験・ストア説明文がShipatonの本来の価値提案（手描き構造式認識）と一致しない状態が一時的に生じる。
- [Issue #18](https://github.com/ItisNoMatter/SmilesStudio/issues/18)（Google Play Console申請準備）の依存関係を、#6・#7・#13の完了のみをブロッカーとする形に再設計する必要がある。

## Related

- [0059-phased-release-strategy-store-review-first](./0059-phased-release-strategy-store-review-first.md)
- [0035-revisit-priority-for-rendering-pipeline](./0035-revisit-priority-for-rendering-pipeline.md)
