param(
    [string]$DbHost = "127.0.0.1",
    [int]$DbPort = 3306,
    [string]$DbUser = "root",
    [string]$DbPassword = "",
    [string]$Database = "tech_blog"
)

$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Resolve-Path (Join-Path $scriptDir "..\..")
$sqlFile = Join-Path $repoRoot "backend\src\main\resources\db\migration\V1__full_system_schema.sql"

if (-not (Get-Command mysql -ErrorAction SilentlyContinue)) {
    throw "mysql CLI was not found in PATH."
}

$mysqlArgs = @("--host=$DbHost", "--port=$DbPort", "--user=$DbUser")
if (-not [string]::IsNullOrEmpty($DbPassword)) {
    $mysqlArgs += "--password=$DbPassword"
}

$createDbSql = "CREATE DATABASE IF NOT EXISTS $Database CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

Write-Host "Creating database '$Database' if needed..."
& mysql @mysqlArgs -e $createDbSql

$checkSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '$Database' AND table_name = 'ai_settings';"
$alreadyInitialized = (& mysql @mysqlArgs -N -s -e $checkSql).Trim()

if ($alreadyInitialized -eq "1") {
    Write-Host "Schema already initialized. Skipping import."
    exit 0
}

Write-Host "Applying schema from $sqlFile"
Get-Content -Raw $sqlFile | & mysql @mysqlArgs $Database

Write-Host "Done. Database '$Database' is ready."
