<#
.SYNOPSIS
    docs/any-decision-record/0138 で定義した DI フレームワーク（Hilt/Koin 等）再検討条件のうち、
    機械的に測れる指標だけを android-app のソースからカウントする補助スクリプト。

.DESCRIPTION
    AnyDR 0138 の3条件は次の通り（いずれか1つでも該当したら再検討）:
      1. Activityをまたいで同じインスタンスを共有する必要が出た
      2. remember配線が3階層以上ネストした
      3. Application-scopedなシングルトンが2個以上必要になった

    このスクリプトは上記の代理指標（Activity数、remember呼び出し数、object宣言数）を
    カウントするだけで、実際に条件へ該当するかどうかの判定は行わない。
    3条件はいずれも意味的な判断を伴うため、出力を見て人間が最終判断すること。
#>

$ErrorActionPreference = "Stop"

$repoRoot = (git rev-parse --show-toplevel).Trim()
$androidAppSrc = Join-Path $repoRoot "android-app\src\main\kotlin"

if (-not (Test-Path $androidAppSrc)) {
    Write-Error "android-app source directory not found: $androidAppSrc"
    exit 1
}

$ktFiles = Get-ChildItem -Path $androidAppSrc -Recurse -Filter "*.kt"

Write-Output "=== AnyDR 0138 DI threshold check (代理指標のみ。最終判断は人間が行うこと) ==="
Write-Output ""

# --- 条件1の代理指標: Activityクラスの数 ---
$activityMatches = $ktFiles | Select-String -Pattern ":\s*ComponentActivity" -List
$activityCount = $activityMatches.Count
Write-Output "[条件1の代理指標] Activityクラスの数: $activityCount"
if ($activityCount -ge 2) {
    Write-Output "  -> 2つ以上あります。Activityをまたいだインスタンス共有が必要になっていないか確認してください。"
}
$activityMatches | ForEach-Object { Write-Output "    - $($_.Path):$($_.LineNumber)" }
Write-Output ""

# --- 条件2の代理指標: ファイルごとのremember系呼び出し回数（ネスト深度の簡易近似） ---
Write-Output "[条件2の代理指標] ファイルごとの remember 系呼び出し回数（3階層以上ネストの可能性がある目安）:"
$anyFlagged = $false
foreach ($file in $ktFiles) {
    $count = (Select-String -Path $file.FullName -Pattern "\bremember(Coroutine\w*|Saveable\w*)?\s*[{(]" -AllMatches).Matches.Count
    if ($count -ge 3) {
        $anyFlagged = $true
        Write-Output "  - $($file.Name): $count 回 -> 目視でネスト構造を確認してください"
    }
}
if (-not $anyFlagged) {
    Write-Output "  該当ファイルなし（3回以上のファイルはありません）"
}
Write-Output ""

# --- 条件3の代理指標: ファイル直下（インデントなし）のobject宣言の数（シングルトン候補） ---
# sealed classの中にネストしたobject（enumのケース等）はインデントされるため除外できるが、
# ステートレスな関数ホルダーobject（例: AndroidImageResizer）とApplication-scopedな
# 状態保持シングルトン（例: SubscriptionStatus）の区別まではしていない。
# ヒットしたファイルの中身を見て、可変状態(var等)を持つものだけを条件3の対象として数えること。
$objectMatches = $ktFiles | Select-String -Pattern "^object\s+\w+" -List
$objectCount = $objectMatches.Count
Write-Output "[条件3の代理指標] ファイル直下の object 宣言の数: $objectCount"
if ($objectCount -ge 2) {
    Write-Output "  -> 2つ以上あります。うち可変状態を持つものがApplication-scopedなシングルトンとして"
    Write-Output "     扱われていないか確認してください（ステートレスな関数ホルダーは対象外）。"
}
$objectMatches | ForEach-Object { Write-Output "    - $($_.Path):$($_.LineNumber)" }
Write-Output ""

Write-Output "詳細な条件定義: docs/any-decision-record/0138-di-framework-reconsideration-triggers.md"
