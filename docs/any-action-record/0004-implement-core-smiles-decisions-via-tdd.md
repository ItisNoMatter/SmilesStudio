# 0004: core-smilesドメイン決定をTDDで実装しテスト命名規則を追加確定

- Date: 2026-08-31
- Related AnyDR: 0010（新規決定）、実装対象は0003, 0004, 0005, 0008

## Objective

AnyDR 0003（HydrogenCountのsealed interface化）・0004（芳香族性の導出）・0005（lazyキャッシュ）
・0008（ParseResult型）の4つの決定を実際のコードに反映する。

## Action

TDDサイクル（Red→Green→Refactor）で`HydrogenCount.kt`・`ParseResult.kt`等を新規実装し、
`Atom`/`Molecule`/`SmilesParser`を更新した。`MoleculeTest.kt`に新しいテストケースを英語の
バッククォート記法で追記した際、ユーザーから「テストメソッド名は日本語で統一したい」という
明示的な指示があり、その場でAnyDR 0010として記録した。

## Result

4件のAnyDR決定がコードに反映され、`./gradlew allTests`がグリーンになった。テストメソッド名の
日本語統一という新しい命名規則が確定し、以降のテストコードに適用される運用ルールとなった。

## Reflections

実装作業の途中で出た一言の指示（テスト名を日本語に）でも、その場でAnyDRとして記録したことで、
後続のセッションで同じ指示を繰り返さずに済んだ。設計討議だけでなく実装作業中に出た方針も
AnyDR化する価値があることを示す一例になった。
