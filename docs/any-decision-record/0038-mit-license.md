# 0038: OSSライセンスはMITを採用する

- Date: 2026-09-03
- Status: Accepted
- Category: naming
- Deciders: the user

## Context

[[0031]]の記録時点では、OSSライセンス（MIT/Apache 2.0等）は「今のうちに検討しておく」という言及のみで実際には未決定だったため、AnyDRには残していなかった。更新版のShipaton 2026対応方針で「ライセンスはMITを軸に検討」という記述があり、本セッションで確認したところ、MITライセンスに正式決定として記録してよいことが確認された。

## Decision

SmilesStudioのOSSライセンスはMITライセンスを採用する。

## Alternatives

- Apache 2.0: 特許条項等を含むより保護的なライセンスとして一般的な選択肢だが、シンプルさが「気軽にコントリビュートできる」という[[0037]]の三層防御構想の思想と合うという理由でMITが選ばれ、Apache 2.0は不採用。

## Consequences

- リポジトリルートに`LICENSE`ファイル（MITライセンス全文）を追加する必要がある。
- README等でライセンスを明記する。

## Related

- [0031-type-system-as-oss-contribution-safety-net](./0031-type-system-as-oss-contribution-safety-net.md)
- [0037-three-layer-defense-oss-strategy](./0037-three-layer-defense-oss-strategy.md)
