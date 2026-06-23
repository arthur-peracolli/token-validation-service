#!/bin/bash

# Deploy Script para Token Validation Service Lambda (Linux/WSL)
# Este script automatiza o processo de build, copy e deployment

set -e  # Exit on error

SKIP_TERRAFORM=false
ONLY_BUILD=false

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --skip-terraform)
            SKIP_TERRAFORM=true
            shift
            ;;
        --only-build)
            ONLY_BUILD=true
            shift
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done

PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$PROJECT_ROOT/token-validation-service"
TARGET_DIR="$PROJECT_DIR/target"
TERRAFORM_DIR="$PROJECT_DIR/terraform"
LAMBDA_DIR="$TERRAFORM_DIR/lambda"

echo -e "\033[1;32m🚀 Iniciando deploy da Token Validation Service Lambda\033[0m"

# Step 1: Build
echo -e "\n\033[1;36m📦 Step 1: Compilando o projeto...\033[0m"
cd "$PROJECT_DIR"
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo -e "\033[1;31m❌ Erro na compilação!\033[0m"
    exit 1
fi
echo -e "\033[1;32m✅ Compilação concluída com sucesso!\033[0m"

# Step 2: Copy JAR
echo -e "\n\033[1;36m📁 Step 2: Copiando JAR para o diretório do Terraform...\033[0m"
JAR_SOURCE="$TARGET_DIR/token-validation-service-1.0.0.jar"
JAR_DEST="$LAMBDA_DIR/token-validation-service-1.0.0.jar"

if [ ! -f "$JAR_SOURCE" ]; then
    echo -e "\033[1;31m❌ JAR não encontrado em $JAR_SOURCE!\033[0m"
    exit 1
fi

mkdir -p "$LAMBDA_DIR"
cp "$JAR_SOURCE" "$JAR_DEST"
echo -e "\033[1;32m✅ JAR copiado para $JAR_DEST\033[0m"

if [ "$ONLY_BUILD" = true ]; then
    echo -e "\n\033[1;32m✅ Build e copy completados com sucesso!\033[0m"
    exit 0
fi

# Step 3: Terraform
if [ "$SKIP_TERRAFORM" = false ]; then
    echo -e "\n\033[1;36m🏗️  Step 3: Executando Terraform...\033[0m"
    cd "$TERRAFORM_DIR"

    echo -e "\n\033[1;36m📋 Terraform Plan:\033[0m"
    terraform plan

    echo -e "\n\033[1;33m❓ Deseja continuar com o apply? (S/N)\033[0m"
    read -r response

    if [[ "$response" == "S" || "$response" == "s" || "$response" == "Y" || "$response" == "y" ]]; then
        terraform apply -auto-approve
        if [ $? -eq 0 ]; then
            echo -e "\n\033[1;32m✅ Terraform apply concluído com sucesso!\033[0m"
        else
            echo -e "\n\033[1;31m❌ Erro ao executar Terraform apply!\033[0m"
            exit 1
        fi
    else
        echo -e "\n\033[1;33m⏭️  Terraform apply cancelado.\033[0m"
        echo -e "\033[1;33mExecute 'terraform apply' manualmente quando estiver pronto.\033[0m"
    fi
else
    echo -e "\n\033[1;33m⏭️  Terraform apply pulado (remova --skip-terraform para executar)\033[0m"
fi

echo -e "\n\033[1;32m✅ Deploy completado com sucesso!\033[0m"
echo -e "\n\033[1;36m💡 Próximas etapas:\033[0m"
echo "1. Aguarde 1-2 minutos para a Lambda inicializar o Spring Context"
echo "2. Verifique os logs no CloudWatch: /aws/lambda/token-validation-service-develop-api"
echo "3. Teste a API com um curl request"
echo -e "\n\033[1;36m📚 Para mais informações, leia LAMBDA_TROUBLESHOOTING.md\033[0m"

