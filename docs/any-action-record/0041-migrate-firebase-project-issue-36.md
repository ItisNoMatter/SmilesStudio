# 0041: Firebaseプロジェクトを新規作成して移行した

- Date: 2026-09-14
- Related Issue: #36

## Objective
Issue #36の非BYOKクラウド経路の実機検証を進めるため、変更したCloud Functionsを
`smilestudio-116a8`（旧プロジェクト）に再デプロイしようとしたところ、
`firebase deploy --only functions`が一貫して失敗する権限問題に遭遇した。原因を特定し、
デプロイできる状態に戻す。

## Action
- IAMコンソール上は`702sunnylife@gmail.com`に「オーナー」ロールが正しく表示・保存されて
  いるにもかかわらず、`testIamPermissions`APIが空の結果を返す（＝実効権限がゼロ）という
  食い違いを、直接Google CloudのAPIを叩いて確認した。
- Blazeプランへのアップグレード、Firebase Management API・Cloud Resource Manager APIの
  有効化、一晩の反映待ち、IAM Policy Troubleshooterでの調査など、考えられる原因を一つずつ
  潰したが、いずれも解消しなかった。
- プロジェクト自体に何らかの内部的な不整合が起きていると判断し、新規Firebaseプロジェクト
  `smilesstudio-309a5`を作成して移行することにした。新規プロジェクトでは、プロジェクト
  作成者に確実にオーナー権限が付与されるため、この種の不整合を回避できる見込みだった。
- 新プロジェクトでFirebase Authentication（匿名認証）・Firestore（`asia-northeast1`、
  ロックモード）・Androidアプリ登録（`google-services.json`再取得）・Blazeプラン・
  Secret Manager（`GEMINI_API_KEY`・`REVENUECAT_WEBHOOK_SECRET`）を再設定。
  `.firebaserc`のプロジェクトIDを更新。
- デプロイの過程で、ローカルのFirebase CLI（v13.7.0）が古く、`firebase-functions`
  npmパッケージが出力するビルド仕様の新しいキーを解釈できないエラーに遭遇。CLIを
  最新版（v15.30.0）に更新したところ、今度はNode.js 18ではCLI自体が動かないという
  制約に当たり、ローカルのNode.jsをwinget経由で24系にアップグレードして解決した。
  加えて、Cloud Functionsのランタイム自体もNode.js 18が廃止済みだったため、
  `functions/package.json`の`engines.node`を20に更新した。

## Result
新プロジェクト`smilesstudio-309a5`へのIAM周りの不整合は再発せず、
`firebase deploy --only functions`が成功し、`recognizeImage`・`revenueCatWebhook`の
両方が`us-central1`にデプロイされた。コンテナイメージのクリーンアップポリシーも設定済み。
RevenueCat側のGoogle Play連携・Play ConsoleのReal-time developer notificationsの
向け直しはPlay Console決済プロファイル未完了（Issue #33）で引き続きブロック中のため
後回しにし、Androidアプリ側のクラウド経路の実機検証に進む。

## Reflections
IAMコンソールの表示（オーナーロールが確かに付与・保存されている）と、実際の認可バックエンドの
挙動（`testIamPermissions`が空を返す）が一致しない、という事象は今回初めて遭遇した。
Blazeプランのアップグレード・複数のAPI有効化・一晩の待機のいずれでも解消しなかったことから、
これは設定ミスではなくGoogle Cloud側の何らかの内部的な不整合だったと考えられる。最終的には
「原因を特定して直す」より「新しいプロジェクトを作って移行する」方が早く確実だった。
原因不明の永続的な不整合に対しては、深追いするコストと作り直すコストを早めに比較する
判断も選択肢として持っておくとよい、という学びだった。

また、副次的にFirebase CLI・Node.jsのバージョンが両方とも相当古いまま放置されていた
ことが分かった（Node 18は既にCloud Functionsランタイムとして廃止済み）。開発環境の
ツールチェーンも、プロジェクトのコード同様に定期的な更新が必要だと再認識した。
