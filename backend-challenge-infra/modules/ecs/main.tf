resource "aws_ecs_cluster" "cluster" {

  name = var.project_name

  setting {
    name  = "containerInsights"
    value = "disabled"
  }

  configuration {
    execute_command_configuration {
      logging = "DEFAULT"
    }
  }

  tags = {}

}

resource "aws_ecs_task_definition" "task" {

  family = "token-validation-service-td"

  cpu = "512"

  memory = "1024"

  network_mode = "awsvpc"

  requires_compatibilities = [
    "FARGATE"
  ]

  execution_role_arn = var.execution_role_arn

  track_latest = false

  container_definitions = jsonencode([
    {
      name      = "token-validation-service-c1"
      image     = "${var.repository_url}:latest"
      essential = true

      environment = [
        {
          name  = "JWT_SECRET"
          value = var.jwt_secret
        }
      ]

      environmentFiles = []

      portMappings = [
        {
          appProtocol   = "http"
          containerPort = 8080
          hostPort      = 8080
          protocol      = "tcp"
          name          = "token-validation-service-c1-80-tcp"
        }
      ]

      mountPoints = []

      systemControls = []

      ulimits = []

      volumesFrom = []

      logConfiguration = {

        logDriver = "awslogs"

        options = {

          awslogs-group = "/ecs/"

          awslogs-region = var.aws_region

          awslogs-stream-prefix = "ecs"

          awslogs-create-group = "true"

        }

        secretOptions = []

      }

    }

  ])

  runtime_platform {

    cpu_architecture = "X86_64"

    operating_system_family = "LINUX"

  }

  tags = {}

}

resource "aws_ecs_service" "service" {

  name = "token-validation-service"

  cluster = aws_ecs_cluster.cluster.id

  task_definition = aws_ecs_task_definition.task.arn

  desired_count = 1

  scheduling_strategy = "REPLICA"

  platform_version = "1.4.0"

  enable_execute_command = false

  enable_ecs_managed_tags = true

  propagate_tags = "NONE"

  wait_for_steady_state = false

  force_new_deployment = true

  deployment_maximum_percent = 200

  deployment_minimum_healthy_percent = 100

  deployment_controller {

    type = "ECS"

  }

  deployment_circuit_breaker {

    enable = true

    rollback = true

  }

  capacity_provider_strategy {

    capacity_provider = "FARGATE"

    weight = 1

    base = 0

  }

  network_configuration {

    assign_public_ip = true

    subnets = [
      "subnet-08b20fc1f21ecb326",
      "subnet-0c214a953bcdc8b76",
      "subnet-0fad06eb0440fd0b1"
    ]

    security_groups = [
      var.security_group_id
    ]

  }

  load_balancer {

    target_group_arn = var.target_group_arn

    container_name = "token-validation-service-c1"

    container_port = 8080

  }

  depends_on = [
    aws_ecs_task_definition.task
  ]

  tags = {}

}