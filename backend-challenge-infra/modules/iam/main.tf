resource "aws_iam_role" "execution" {

  name = var.execution_role_name

  assume_role_policy = jsonencode({
    Version = "2008-10-17"

    Statement = [
      {
        Sid = ""

        Effect = "Allow"

        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }

        Action = "sts:AssumeRole"
      }
    ]
  })

  managed_policy_arns = [
    "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  ]

  max_session_duration = 3600

  path = "/"
}