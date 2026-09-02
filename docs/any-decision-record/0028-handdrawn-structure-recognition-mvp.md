# 0028: 手描き構造式パースMVPをKoog経由のVision LLMで実装する

- Date: 2026-09-02
- Status: Accepted
- Category: architecture
- Deciders: the user

## Context

Shipaton 2026（[[0027]]）向けの新機能として、手描きの化学構造式画像をJetBrains Koog経由でVision対応LLMに投げてSMILES文字列に変換し、既存の`SmilesParser`/`Molecule`/描画パイプラインで再度構造式として表示する機能を計画した。CLAUDE.mdはこれまでKoog連携を「将来的に導入予定」という遠い将来の構想として記載していたが、本AnyDRによりハッカソンのMVPスコープへ格上げする。

## Decision

MVPスコープは「画像1枚 → SMILES候補1つ → 構造式再描画」に限定する。複数候補の提示や信頼度スコアの表示は今回のスコープ外とする。LLMが返したSMILES文字列は、既存のSMILESテキスト入力欄（[[0017]]）にそのまま反映し、ユーザーが確認・修正できるようにする（新しいグラフィカル編集UIは追加しない）。認識が失敗した場合、または誤認識だった場合のフォールバックとして、SMILES文字列を直接手入力する経路を必ず残す。

## Alternatives

- 複数候補提示・信頼度スコア表示: 認識精度をユーザーに提示する上では有用だが、今回はMVPスコープとして明示的に後回しとした。

## Consequences

- この設計は[[0017]]で確定した「テキスト編集＋読み取り専用の構造式描画」という一方向パイプラインをそのまま再利用する形になっており、そこで不採用となったアプローチB（グラフィカルな構造エディタ）には影響しない。画像認識結果はテキストフィールドを経由するだけであり、Canvas上の直接編集機能は依然として導入しない。
- [[0009]]（正規SMILESライター実装の先送り）にも影響しない。本機能はSMILESのパース（テキスト→Molecule）のみを必要とし、逆方向（Molecule→SMILES）のシリアライズは不要なため。
- UIの実体は主に`android-app`/`ui-compose`側に実装されるが、パース・描画のコアロジックは`core-smiles`側でプラットフォーム非依存のまま変更不要。

## Related

- [0009-defer-canonical-smiles-writer](./0009-defer-canonical-smiles-writer.md)
- [0017-v1-text-input-readonly-rendering-scope](./0017-v1-text-input-readonly-rendering-scope.md)
- [0027-android-app-module-for-shipaton-2026](./0027-android-app-module-for-shipaton-2026.md)
