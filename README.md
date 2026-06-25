# Token Validation Service

API REST desenvolvida em **Java 21** e **Spring Boot** para geração e validação de tokens JWT, projetada com foco em boas práticas de arquitetura, infraestrutura como código e automação de deploy.

A solução foi construída utilizando serviços da AWS, OpenTofu e GitHub Actions, simulando um fluxo de desenvolvimento utilizado em equipes de engenharia de software.

## Planejamento do Projeto

Durante o desenvolvimento foi criado um conjunto de **cards** organizados em um **GitHub Project**, simulando um ambiente real de desenvolvimento ágil.

O objetivo dessa organização foi demonstrar a capacidade de:

* Planejar entregas incrementais.
* Definir critérios de aceite para cada funcionalidade.
* Organizar e priorizar atividades.
* Acompanhar a evolução do projeto por meio de métricas.
* Trabalhar seguindo práticas inspiradas em metodologias ágeis.

Os cards representam apenas o planejamento e acompanhamento da execução do desafio, não sendo um requisito funcional da aplicação.

O planejamento completo pode ser consultado em:

* GitHub Projects
* GitHub Issues

---

## Funcionalidades

* Validação estrutural de tokens JWT.
* Aplicação das regras de negócio propostas no desafio.
* Logs estruturados para observabilidade.
* Containerização utilizando Docker.
* Deploy automatizado utilizando GitHub Actions.
* Infraestrutura como código utilizando OpenTofu.
* Execução da aplicação em AWS ECS Fargate.
* Exposição da API através de Application Load Balancer.
* Monitoramento utilizando Amazon CloudWatch.

---

## Tecnologias

### Backend

* Java 21
* Spring Boot
* Spring Security
* JWT
* Maven

### Infraestrutura

* OpenTofu
* AWS ECS Fargate
* AWS ECR
* AWS Application Load Balancer
* AWS IAM
* Amazon CloudWatch

### DevOps

* Docker
* GitHub Actions
* CI/CD
* GitHub Projects
