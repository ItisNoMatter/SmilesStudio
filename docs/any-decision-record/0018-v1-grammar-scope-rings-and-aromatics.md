# 0018: v1のSMILES文法スコープに環閉包と芳香族小文字表記を含める

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0017]]でv1のコア体験を「テキスト編集＋読み取り専用の構造式描画」に確定した後、次に描画実装へ進む前提としてパーサーが対応すべきSMILES文法の範囲（グラマースコープ）を決める必要があった。現状の`SmilesParser`/`Tokenizer`は直鎖＋分岐のみに対応しており（[[0012]]）、環閉包記法（`C1CCCCC1`）と芳香族小文字表記（`c1ccccc1`）は[[0015]]に従い専用の未対応エラーを返している。ユーザーは今回のゴールを「最小構成でユーザーにリリースできるところまで」と明言しており、スコープの広さとリリース可能性のバランスが論点だった。

## Decision

v1のSMILES文法スコープに、環閉包記法（`C1CCCCC1`）と芳香族小文字表記（`c1ccccc1`）の両方を含める。ベンゼン・ピリジンなど、化学徒が最も日常的に触れる芳香族分子までv1で描画可能にする。

## Alternatives

- 現状維持（直鎖＋分岐のみ）: 実装コストは最小で既存の`Tokenizer`/`SmilesParser`に変更不要だが、環状分子を一切描画できず、有機化学で最頻出の構造がまるごと抜け落ちる。ユーザーがベンゼン等の代表的な分子を試した瞬間に「非対応」エラーに当たり、ファーストインプレッションが悪いため不採用。
- 環閉包記法のみ対応（芳香族小文字表記は対象外）: 脂肪族環状分子（シクロヘキサン等）まで扱えるようになり、芳香族の非局在化表現の描画方針（Kekulé構造 vs 円/破線表現）という重い意思決定を先送りできる点が魅力だったが、それでもベンゼンなど芳香環という化学徒が最も試したくなる分子の一角が抜け落ちたままになるため不採用。

## Consequences

- パーサー拡張として、環閉包ラベルの解決（開いた環閉包の対応付け、複数環対応）と芳香族小文字の原子・結合パースの両方が必要になる。
- `Ring`（[[0006]]）・Aromatic Atom/Bond（`CONTEXT.md`）はドメインモデル側（`Molecule.isAromatic()`, `BondType.AROMATIC`）で既に定義・実装済みであり、パーサー側を追いつかせる形になる。
- 芳香族結合をどう描画するか（Kekulé構造として描くか、円/破線による非局在化表現にするか）という描画方針の意思決定が新たに必要になる。これは本AnyDRのスコープ外であり、別途グリリングで扱う。
- 「最小構成」というゴールに対してスコープが最大の選択肢を採ったため、着手までのリードタイムは他の案より長くなる。

## Related

- [0006-ring-as-derived-domain-term](./0006-ring-as-derived-domain-term.md)
- [0012-smiles-parser-initial-scope-chain-and-branches](./0012-smiles-parser-initial-scope-chain-and-branches.md)
- [0015-unsupported-notation-specific-error](./0015-unsupported-notation-specific-error.md)
- [0017-v1-text-input-readonly-rendering-scope](./0017-v1-text-input-readonly-rendering-scope.md)
