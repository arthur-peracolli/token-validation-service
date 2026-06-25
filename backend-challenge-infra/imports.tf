import {
  to = module.security.aws_security_group.default
  id = "sg-0055f21dcd1f66588"
}

import {
  to = module.iam.aws_iam_role.execution
  id = "ecsTaskExecutionRole"
}

import {
  to = module.ecr.aws_ecr_repository.repository
  id = "token-validation-service"
}

import {
  to = module.alb.aws_lb.alb
  id = "arn:aws:elasticloadbalancing:us-east-1:581009775825:loadbalancer/app/token-validation-alb/dc4539f39e916a9b"
}

import {
  to = module.alb.aws_lb_target_group.tg
  id = "arn:aws:elasticloadbalancing:us-east-1:581009775825:targetgroup/token-validation-tg-8080/8880f436c1b6fe26"
}

import {
  to = module.alb.aws_lb_listener.http
  id = "arn:aws:elasticloadbalancing:us-east-1:581009775825:listener/app/token-validation-alb/dc4539f39e916a9b/8c8cdd10c661ef51"
}