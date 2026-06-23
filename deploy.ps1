# Deploy Script para Token Validation Service Lambda
# Este script automatiza o processo de build, copy e deployment

param(
    [switch]$SkipTerraform = $false,
    [switch]$OnlyBuild = $false
)

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectDir = Join-Path $projectRoot "token-validation-service"
$targetDir = Join-Path $projectDir "target"
$terraformDir = Join-Path $projectDir "terraform"
$lambdaDir = Join-Path $terraformDir "lambda"

Write-Host "Iniciando deploy da Token Validation Service Lambda" -ForegroundColor Green

# Step 1: Build
Write-Host "`nStep 1: Compilando o projeto..." -ForegroundColor Cyan
Set-Location $projectDir
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "Erro na compilacao!" -ForegroundColor Red
    exit 1
}
Write-Host "Compilacao concluida com sucesso!" -ForegroundColor Green

# Step 2: Copy JAR
Write-Host "`nStep 2: Copiando JAR para o diretorio do Terraform..." -ForegroundColor Cyan
$jarSource = Join-Path $targetDir "token-validation-service-1.0.0.jar"
$jarDest = Join-Path $lambdaDir "token-validation-service-1.0.0.jar"

if (-not (Test-Path $jarSource)) {
    Write-Host "JAR nao encontrado em $jarSource!" -ForegroundColor Red
    exit 1
}

Copy-Item $jarSource $jarDest -Force
Write-Host "JAR copiado para $jarDest" -ForegroundColor Green

if ($OnlyBuild) {
    Write-Host "`nBuild e copy completados com sucesso!" -ForegroundColor Green
    exit 0
}

# Step 3: Terraform
if (-not $SkipTerraform) {
    Write-Host "`nStep 3: Executando Terraform..." -ForegroundColor Cyan
    Set-Location $terraformDir

    Write-Host "`nTerraform Plan:" -ForegroundColor Cyan
    terraform plan

    Write-Host "`nDeseja continuar com o apply? (S/N)" -ForegroundColor Yellow
    $response = Read-Host

    if ($response -eq "S" -or $response -eq "s" -or $response -eq "Y" -or $response -eq "y") {
        terraform apply -auto-approve
        if ($LASTEXITCODE -eq 0) {
            Write-Host "`nTerraform apply concluido com sucesso!" -ForegroundColor Green
        } else {
            Write-Host "`nErro ao executar Terraform apply!" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "`nTerraform apply cancelado." -ForegroundColor Yellow
        Write-Host "Execute 'terraform apply' manualmente quando estiver pronto." -ForegroundColor Yellow
    }
} else {
    Write-Host "`nTerraform apply pulado (use sem -SkipTerraform para executar)" -ForegroundColor Yellow
}

Write-Host "`nDeploy completado com sucesso!" -ForegroundColor Green
Write-Host "`nProximas etapas:" -ForegroundColor Cyan
Write-Host "1. Aguarde 1-2 minutos para a Lambda inicializar o Spring Context"
Write-Host "2. Verifique os logs no CloudWatch: /aws/lambda/token-validation-service-develop-api"
Write-Host "3. Teste a API com um curl request" -ForegroundColor Cyan
Write-Host "`nPara mais informacoes, leia LAMBDA_TROUBLESHOOTING.md" -ForegroundColor Cyan

