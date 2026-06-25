# Backend Challenge Infrastructure

Infraestrutura como código do projeto **Token Validation Service**, desenvolvida com **OpenTofu** para provisionamento e gerenciamento dos recursos da AWS.

## Objetivo

Este projeto tem como objetivo automatizar a criação e gerenciamento da infraestrutura necessária para execução da aplicação em ambiente AWS, utilizando uma arquitetura modular baseada em OpenTofu.

## Arquitetura

A infraestrutura é composta pelos seguintes serviços:

* Amazon VPC (VPC padrão)
* Security Groups
* Amazon Elastic Container Registry (ECR)
* Amazon ECS Fargate
* Application Load Balancer (ALB)
* AWS IAM
* Amazon CloudWatch Logs

A aplicação é executada em containers Docker hospedados no Amazon ECS Fargate, utilizando imagens armazenadas no Amazon ECR e expostas publicamente através de um Application Load Balancer.

## Estrutura do Projeto

```text
backend-challenge-infra/
│
├── modules
│   ├── network
│   ├── security
│   ├── iam
│   ├── ecr
│   ├── alb
│   ├── ecs
│   └── cloudwatch
│
├── imports.tf
├── provider.tf
├── versions.tf
├── variables.tf
├── outputs.tf
├── terraform.tfvars.example
├── main.tf
└── README.md
```

## Módulos

### Network

Responsável por consumir a infraestrutura de rede existente da AWS, incluindo:

* Default VPC
* Subnets públicas

### Security

Gerenciamento dos Security Groups utilizados pelo ALB e ECS.

### IAM

Gerenciamento da Role utilizada pelas Tasks do ECS.

### ECR

Gerenciamento do repositório Docker utilizado pela aplicação.

### ALB

Gerenciamento do:

* Application Load Balancer
* Target Group
* Listener HTTP

### ECS

Provisionamento do ambiente de execução da aplicação utilizando:

* ECS Cluster
* Task Definition
* ECS Service

### CloudWatch

Gerenciamento dos grupos de logs utilizados pela aplicação.

## Pré-requisitos

* OpenTofu
* AWS CLI
* Docker
* Credenciais AWS configuradas

Verifique a autenticação:

```bash
aws sts get-caller-identity
```

## Inicialização

```bash
tofu init
```

## Formatação

```bash
tofu fmt -recursive
```

## Validação

```bash
tofu validate
```

## Planejamento

```bash
tofu plan
```

## Provisionamento

```bash
tofu apply
```

## Importação de Recursos

Os recursos previamente existentes na AWS são importados através do arquivo `imports.tf`, permitindo que o OpenTofu passe a gerenciá-los sem necessidade de recriação.

## Variáveis

As principais variáveis utilizadas são:

| Variável     | Descrição                          |
| ------------ | ---------------------------------- |
| aws_region   | Região AWS                         |
| project_name | Nome do projeto                    |
| jwt_secret   | Chave JWT utilizada pela aplicação |

## Tecnologias

* OpenTofu
* AWS ECS Fargate
* AWS ECR
* AWS IAM
* AWS Application Load Balancer
* AWS CloudWatch
* Docker

## Fluxo de Deploy

```text
OpenTofu
        │
        ▼
Provisionamento AWS
        │
        ▼
Amazon ECR
        │
        ▼
Amazon ECS Fargate
        │
        ▼
Application Load Balancer
        │
        ▼
Internet
```

## Autor

Arthur Peracolli

Backend Challenge – Token Validation Service
