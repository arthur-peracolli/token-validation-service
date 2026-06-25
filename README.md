# Token Validation Service

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED)
![AWS](https://img.shields.io/badge/AWS-ECS%20Fargate-FF9900)
![OpenTofu](https://img.shields.io/badge/OpenTofu-IaC-6F42C1)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-2088FF)
![License](https://img.shields.io/badge/License-MIT-blue)

## 📖 Sobre o projeto

O **Token Validation Service** é uma API REST desenvolvida em **Java 21** e **Spring Boot** responsável por validar tokens JWT conforme as regras de negócio definidas pelo desafio.

Além da implementação da API, este projeto contempla uma arquitetura completa de entrega contínua utilizando **Docker**, **GitHub Actions**, **AWS ECS Fargate**, **Application Load Balancer** e **Infraestrutura como Código (OpenTofu)**.

O objetivo foi simular um ambiente corporativo de desenvolvimento, aplicando boas práticas de engenharia de software, automação de infraestrutura e DevOps.

---

# ✨ Características

- API REST desenvolvida com Java 21 e Spring Boot
- Arquitetura em camadas inspirada em Domain Driven Design
- Validação de JWT baseada em regras de negócio
- Documentação automática via Swagger/OpenAPI
- Containerização com Docker
- Infraestrutura como Código utilizando OpenTofu
- Deploy automatizado na AWS utilizando GitHub Actions
- Observabilidade com Spring Boot Actuator e Micrometer
- Métricas compatíveis com Prometheus
- Logging estruturado utilizando SLF4J e MDC

---

# Sumário

* Visão Geral
* Arquitetura da Solução
* Tecnologias Utilizadas
* Estrutura do Projeto
* Como Executar
* Docker
* Docker Compose
* Endpoints
* Arquitetura da Aplicação
* Design Patterns
* Infraestrutura AWS
* CI/CD
* Testes
* Segurança
* Decisões Técnicas
* Melhorias Futuras
* Coleção Insomnia
* Referências

---

# Visão Geral

O serviço recebe um token JWT e executa uma sequência de validações estruturais e regras de negócio para determinar sua validade.

Entre as validações implementadas estão:

* Decodificação do Payload JWT
* Contagem das Claims
* Validação do campo Name
* Validação do Role
* Validação do Seed
* Validação de números primos
* Tratamento de exceções
* Logging estruturado

A aplicação foi desenvolvida utilizando uma arquitetura em camadas inspirada em DDD, priorizando separação de responsabilidades, extensibilidade e facilidade de testes.

---

# Arquitetura da Solução

```text
                        GitHub
                           │
             Push / Pull Request / Merge
                           │
                           ▼
                 GitHub Actions CI/CD
      ┌────────────────────┴────────────────────┐
      │                                         │
      ▼                                         ▼
 Build + Test + Docker                 OpenTofu Validate/Plan/Apply
      │                                         │
      ▼                                         ▼
 Amazon ECR                               AWS Infrastructure
      │                          (VPC, SG, ALB, ECS, IAM)
      │                                         │
      └────────────────────┬────────────────────┘
                           ▼
                 Amazon ECS Fargate
                           │
                           ▼
            Spring Boot Token Validation API
                           │
                           ▼
           Application Load Balancer (ALB)
                           │
                           ▼
                 Endpoint Público HTTP
```

---

# Tecnologias Utilizadas

## Backend

* Java 21
* Spring Boot
* Spring Web
* Spring Validation
* Jackson
* Maven

## Testes

* JUnit 5
* Mockito
* Spring Boot Test

## DevOps

* Docker
* Docker Compose
* GitHub Actions
* OpenTofu

## Cloud

* Amazon ECS Fargate
* Amazon ECR
* Application Load Balancer
* CloudWatch Logs
* IAM
* VPC
* Security Groups

## Stack de Observabilidade

A solução utiliza:

* Spring Boot Actuator
* Micrometer
* Prometheus

As métricas podem ser consumidas por ferramentas como Grafana para construção de dashboards e monitoramento em tempo real.

---

# Estrutura do Projeto

```text
.
├── .github
│   └── workflows
│       ├── backend.yml
│       ├── deploy.yml
│       └── infrastructure.yml
│
├── backend-challenge-infra
│   ├── modules
│   │   ├── alb
│   │   ├── ecs
│   │   ├── ecr
│   │   ├── iam
│   │   ├── network
│   │   └── security
│   │
│   ├── main.tf
│   ├── provider.tf
│   ├── variables.tf
│   ├── outputs.tf
│   └── README.md
│
├── scripts
├── src
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

# Como Executar

## Pré-requisitos

* Java 21
* Maven 3.9+
* Docker
* Docker Compose

---

## Executando localmente

```bash
mvn clean package
```

Depois execute:

```bash
java -jar target/token-validation-service-1.0.0.jar
```

Ou execute diretamente pelo Maven:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

```
http://localhost:8080
```

---

# Docker

Construir a imagem:

```bash
docker build -t token-validation-service .
```

Executar:

```bash
docker run \
-p 8080:8080 \
-e JWT_SECRET=your_secret \
token-validation-service
```

---

# Docker Compose

```bash
docker compose up --build
```

---

# Executando os testes

Todos os testes:

```bash
mvn test
```

Empacotar a aplicação:

```bash
mvn clean package
```

---

# Endpoints

## Validar Token

```
GET /api/validate
```

Parâmetros

| Nome  | Obrigatório | Descrição          |
| ----- | ----------- | ------------------ |
| token | Sim         | JWT a ser validado |

Exemplo

```bash
curl -G \
"http://localhost:8080/api/validate" \
--data-urlencode "token=eyJ..."
```

Resposta

```json
{
    "valid": true
}
```

ou

```json
{
    "valid": false
}
```
---

# Arquitetura da Aplicação

A aplicação foi organizada seguindo uma arquitetura inspirada em **Domain Driven Design (DDD)**, separando claramente responsabilidades entre domínio, aplicação e infraestrutura.

```text
src
├── application
│   ├── controllers
│   └── dto
│
├── domain
│   ├── model
│   ├── rules
│   ├── services
│   ├── validators
│   └── interfaces
│
├── infrastructure
│   ├── jwt
│   ├── configuration
│   └── logging
│
└── shared
    ├── constants
    ├── exceptions
    └── utils
```

Cada camada possui uma responsabilidade específica.

| Camada         | Responsabilidade                                          |
| -------------- | --------------------------------------------------------- |
| Application    | Recebe as requisições HTTP e delega para o domínio        |
| Domain         | Contém toda a regra de negócio                            |
| Infrastructure | Comunicação com recursos externos e implementação técnica |
| Shared         | Componentes reutilizáveis                                 |

Essa separação reduz o acoplamento entre as camadas e facilita a manutenção da aplicação.

---

# Fluxo de Validação

O processo de validação ocorre na seguinte ordem:

```text
Requisição HTTP

↓

ValidationController

↓

TokenValidationService

↓

JwtPayloadExtractor

↓

ClaimData

↓

ValidationRule #1

↓

ValidationRule #2

↓

ValidationRule #3

↓

ValidationRule #N

↓

Resposta da API
```

O serviço interrompe imediatamente o processamento caso qualquer regra retorne inválida.

Essa estratégia reduz processamento desnecessário e melhora a performance.

---

# Principais Componentes

## ValidationController

Responsável por expor o endpoint REST.

Funções:

* Receber o token
* Delegar a validação
* Retornar a resposta HTTP

---

## TokenValidationService

É o orquestrador da aplicação.

Responsabilidades:

* Extrair o payload
* Construir o ClaimData
* Executar todas as ValidationRules
* Retornar o resultado final

---

## JwtPayloadExtractor

Responsável por:

* Decodificar Base64URL
* Converter JSON
* Construir o objeto ClaimData

Nesta implementação a assinatura criptográfica do JWT não é validada, pois o desafio concentra-se apenas na validação estrutural e nas regras de negócio.

---

## ValidationRule

Interface implementada por todas as regras.

Cada regra possui dois métodos:

```java
ValidationResult validate(ClaimData claimData);

int getOrder();
```

O método `getOrder()` define a sequência de execução.

---

# Regras Implementadas

## ClaimCountRule

Valida que existam exatamente três claims.

---

## NameRule

Valida:

* Obrigatório
* Máximo de 256 caracteres
* Não possuir números

---

## RoleRule

Valida se o Role pertence aos valores permitidos.

Exemplo:

* Admin
* Member
* External

---

## SeedRule

Valida:

* Seed informado
* Conversão numérica
* Número primo

A verificação de primalidade é realizada pelo componente:

```text
PrimeNumberValidator
```

---

# Design Patterns

Durante o desenvolvimento foram utilizados alguns padrões clássicos de projeto.

## Chain of Responsibility

As regras de validação são executadas sequencialmente.

```text
Rule 1

↓

Rule 2

↓

Rule 3

↓

Rule N
```

Cada regra possui apenas uma responsabilidade e pode interromper o fluxo.

Benefícios:

* Baixo acoplamento
* Fácil manutenção
* Fácil extensão

---

## Strategy

Cada ValidationRule representa uma estratégia diferente de validação.

Novas regras podem ser adicionadas sem alterar o serviço principal.

---

## Dependency Injection

Toda a aplicação utiliza injeção de dependência do Spring Boot.

Benefícios:

* Baixo acoplamento
* Testabilidade
* Reutilização

---

# Infraestrutura AWS

Toda a infraestrutura foi provisionada utilizando **OpenTofu**.

O projeto utiliza uma arquitetura modular.

```text
backend-challenge-infra
│
├── network
├── security
├── iam
├── ecr
├── alb
└── ecs
```

Cada módulo possui responsabilidade única.

---

## Recursos Provisionados

A solução cria automaticamente:

* Amazon ECS Cluster
* Amazon ECS Service
* Amazon ECS Task Definition
* Amazon ECR
* Application Load Balancer
* Target Group
* Listener HTTP
* Security Group
* IAM Roles
* CloudWatch Logs

Toda a infraestrutura pode ser recriada apenas executando os comandos do OpenTofu.

---

# Arquitetura na AWS

```text
Internet

↓

Application Load Balancer

↓

Amazon ECS Service

↓

Amazon ECS Task

↓

Spring Boot API

↓

CloudWatch Logs
```

As imagens Docker são armazenadas no Amazon ECR.

O deploy é realizado automaticamente pelo GitHub Actions.

---

# Infraestrutura como Código

A infraestrutura está localizada em:

```text
backend-challenge-infra/
```

Principais arquivos:

```text
main.tf

provider.tf

variables.tf

outputs.tf

terraform.tfvars
```

Toda a infraestrutura é modularizada.

---

# CI/CD

O projeto utiliza GitHub Actions para automação.

Existem três pipelines independentes.

## Backend

Responsável por:

* Build
* Testes
* Empacotamento

---

## Infrastructure

Responsável por:

* OpenTofu Init
* Validate
* Plan
* Apply

---

## Deploy

Responsável por:

* Build Docker
* Push para Amazon ECR
* Atualização do ECS Service

---

# Fluxo do CI/CD

```text
Developer

↓

GitHub

↓

GitHub Actions

↓

Build

↓

Tests

↓

Docker Build

↓

Amazon ECR

↓

Amazon ECS

↓

Application Load Balancer

↓

Usuário Final
```

Todo o processo ocorre automaticamente após um merge na branch **main**.

---

# Deploy Automatizado

Após um push na branch principal, o pipeline executa:

1. Build da aplicação

2. Execução dos testes

3. Build da imagem Docker

4. Push para o Amazon ECR

5. Atualização do Amazon ECS

6. Novo deployment da Task Definition

Todo o processo é realizado sem intervenção manual.

# Engenharia de Prompt

Durante o desenvolvimento deste projeto foram utilizadas ferramentas de Inteligência Artificial como apoio à engenharia de software.

A utilização ocorreu como ferramenta auxiliar, principalmente para:

* Revisão de código
* Refinamento de arquitetura
* Estruturação da documentação
* Sugestões para pipelines CI/CD
* Modelagem da infraestrutura OpenTofu
* Revisão de boas práticas
* Geração inicial da especificação OpenAPI

Todas as implementações foram revisadas, adaptadas e validadas manualmente antes da utilização.

# 📷 Evidências

Durante o desenvolvimento foram registradas evidências da implementação e validação da solução, incluindo:

- Execução dos pipelines CI/CD
- Provisionamento da infraestrutura com OpenTofu
- Deploy na AWS
- Exposição da API através do Application Load Balancer
- Swagger/OpenAPI
- Métricas com Spring Boot Actuator e Micrometer
- Testes da API
- Evolução das entregas

As evidências podem ser consultadas na seção **Issues** deste repositório, onde cada etapa foi documentada de forma incremental para simular um ambiente real de desenvolvimento e acompanhamento de atividades.

# 🔗 Links Úteis

## API

http://token-validation-alb-1555674803.us-east-1.elb.amazonaws.com

## Swagger UI

http://token-validation-alb-1555674803.us-east-1.elb.amazonaws.com/swagger-ui/index.html

## OpenAPI

http://token-validation-alb-1555674803.us-east-1.elb.amazonaws.com/v3/api-docs

## Health

http://token-validation-alb-1555674803.us-east-1.elb.amazonaws.com/actuator/health

## Métricas Prometheus

http://token-validation-alb-1555674803.us-east-1.elb.amazonaws.com/actuator/prometheus

# Observações

A arquitetura principal da solução utiliza Amazon ECS Fargate para execução da aplicação.

Embora o desafio também mencione Helm Chart, optou-se por utilizar ECS devido à sua simplicidade operacional e integração nativa com a AWS.

Um Helm Chart poderá ser disponibilizado futuramente para implantação em ambientes Kubernetes como Amazon EKS.