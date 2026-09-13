# 0134: 無料枠の使い切り検知はリアクティブ方式とする

- Date: 2026-09-14
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #36で、無料枠を使い切っている場合に「認識を試みずにペイウォールを表示する」
（Issue #36本文、AnyDR 0115）をどう実現するかを検討した。残り回数はAnyDR 0133により
`recognizeImage`のレスポンスに含まれるようになるため、これをクライアント側でキャッシュして
次回以降サーバー呼び出し自体を省略できるかが論点だった。

## Decision

クライアント側でのキャッシュは行わず、毎回`recognizeImage`を呼び出し、サーバーが
`resource-exhausted`エラーを返した場合にペイウォール表示にマッピングするリアクティブ方式と
する。「認識を試みずに」は「Gemini呼び出しは試みない」という意味だと解釈する
（Firestoreトランザクションによる否認はGemini呼び出しより手前で起きるため、この方式でも
満たされる）。

## Alternatives

- クライアント側で直近のレスポンスから残り回数をキャッシュし、0になったら次回はサーバーを
  呼ばずにペイウォールを表示するプロアクティブ方式: サーバー往復そのものを省略できる利点は
  あるが、購読開始・月替わりのタイミングでキャッシュを正しくクリアする追加ロジックが必要に
  なり、AnyDR 0124（ローカル保存のカウンターはズレうるためサーバー側に寄せた）の判断理由と
  趣旨が逆行するため不採用とした。

## Consequences

- 使い切った後も毎回Firestoreトランザクションへのネットワーク往復は発生するが、Gemini呼び出し
  はスキップされるためコスト・レイテンシともに無視できる。
- `Coordinator`に無料枠状態のキャッシュを持たせる必要がなく、実装がシンプルになる。

## Related

- [0115-show-remaining-count-transparently](./0115-show-remaining-count-transparently.md)
- [0124-free-tier-counter-moves-server-side](./0124-free-tier-counter-moves-server-side.md)
- [0133-remaining-count-via-recognize-response](./0133-remaining-count-via-recognize-response.md)
