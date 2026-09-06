# 0086: テスター確保サービスはTesters Communityを利用する

- Date: 2026-09-06
- Status: Accepted
- Category: tooling
- Deciders: the user, Claude Code

## Context

[AnyDR 0085](./0085-paid-tester-acquisition-service.md)で「12人のテスターは有料サービスで
確保する」と決めた後、具体的にどのサービスを使うかを調査・比較した。調査の結果、
候補として[Testers Community](https://www.testerscommunity.com/)と[onTest](https://ontest.app/)
の2つを比較した。あわせて、Fiverr等の格安ギグ（$10〜25で「Gmailアドレス12件を納品」を謳う
もの）は、実際のオプトイン・アプリ起動を伴わないことが多く、Googleの本番アクセス審査
（実際の利用実態を確認する）で却下されるリスクが高いことも分かったため、この種の格安ギグは
最初から選択肢から除外した。

## Decision

テスター確保サービスとして[Testers Community](https://www.testerscommunity.com/)を利用する。

## Alternatives

- onTest（$19.99、12人、15日間、実機でのデイリースクリーンショット証跡付き、3日間返金保証）:
  実機証跡という透明性の高さは魅力的だったが、Testers Communityと比べるとテスター数が
  少なく（12人ちょうど、離脱の余地が少ない）、価格も同程度かやや高め。
- Fiverr等の格安ギグ（$10〜25）: 最安だが、実際のオプトイン・アプリ起動を伴わないことが
  多く、Googleの本番アクセス審査で却下されるリスクが高いため、比較検討の対象からも除外した。

## Consequences

- Testers CommunityのStarterプラン（¥1,999、15人、本番アクセス許可保証付き）を利用する
  想定。実際の申し込み・支払いはユーザー自身が行う。
- 「本番アクセス許可されなければ返金」という保証の実効性は、実際に14日間運用してみるまで
  検証できない。

## Related

- [0085-paid-tester-acquisition-service](./0085-paid-tester-acquisition-service.md)
