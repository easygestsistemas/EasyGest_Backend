param(
    [string]$Uri,
    [string]$Database = "EasyGest",
    [switch]$Drop
)

$mongoimport = Get-Command mongoimport -ErrorAction SilentlyContinue
if (-not $mongoimport) {
    throw "mongoimport não encontrado. Instale o MongoDB Database Tools e reinicie o PowerShell."
}

if (-not $Uri) {
    $envFile = Join-Path $PSScriptRoot "..\..\.env.local"
    if (Test-Path $envFile) {
        foreach ($line in Get-Content $envFile) {
            if ($line -match '^SPRING_MONGODB_URI=(.+)$') {
                $Uri = $matches[1].Trim()
                break
            }
        }
    }
}

if (-not $Uri) {
    $Uri = $env:SPRING_MONGODB_URI
}

if (-not $Uri) {
    throw "Informe -Uri, defina SPRING_MONGODB_URI ou configure .env.local na raiz."
}

$collections = @(
    "fornecedores",
    "clientes",
    "funcionarios",
    "prestadores",
    "produtos"
)

foreach ($collection in $collections) {
    $file = Join-Path $PSScriptRoot "$collection.json"
    if (-not (Test-Path $file)) {
        throw "Arquivo não encontrado: $file"
    }

    $arguments = @(
        "--uri", $Uri,
        "--db", $Database,
        "--collection", $collection,
        "--jsonArray",
        "--file", $file
    )

    if ($Drop) {
        $arguments += "--drop"
    }

    Write-Host "Importando $collection.json..." -ForegroundColor Cyan
    & mongoimport @arguments
    if ($LASTEXITCODE -ne 0) {
        throw "Falha ao importar a coleção $collection. Código: $LASTEXITCODE"
    }
}

Write-Host "Importação concluída no banco '$Database'." -ForegroundColor Green
