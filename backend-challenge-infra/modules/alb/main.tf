resource "aws_lb" "alb" {

  name               = "token-validation-alb"
  internal           = false
  load_balancer_type = "application"

  security_groups = [
    var.security_group_id
  ]

  subnets = var.alb_subnet_ids

  idle_timeout               = 60
  enable_deletion_protection = false

  tags = {}

}

resource "aws_lb_target_group" "tg" {

  name        = "token-validation-tg-8080"
  port        = 8080
  protocol    = "HTTP"
  target_type = "ip"

  vpc_id = var.vpc_id

  health_check {

    enabled = true

    path = "/actuator/health"

    protocol = "HTTP"

    matcher = "200"

    interval = 30

    timeout = 5

    healthy_threshold = 5

    unhealthy_threshold = 2

  }

  tags = {}

}

resource "aws_lb_listener" "http" {

  load_balancer_arn = aws_lb.alb.arn

  port = 80

  protocol = "HTTP"

  default_action {

    type = "forward"

    forward {

      target_group {
        arn    = aws_lb_target_group.tg.arn
        weight = 1
      }

      stickiness {
        enabled  = false
        duration = 3600
      }

    }

  }

}