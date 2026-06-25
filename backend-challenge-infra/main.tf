module "network" {
  source = "./modules/network"
}

module "security" {
  source = "./modules/security"

  vpc_id = module.network.vpc_id
}

module "ecr" {

  source = "./modules/ecr"

  repository_name = "token-validation-service"

}

module "iam" {

  source = "./modules/iam"

  execution_role_name = "ecsTaskExecutionRole"

}

module "alb" {

  source = "./modules/alb"

  project_name = var.project_name

  vpc_id = module.network.vpc_id

  security_group_id = module.security.security_group_id

  alb_subnet_ids = [
    "subnet-08b20fc1f21ecb326",
    "subnet-0c214a953bcdc8b76",
    "subnet-0fad06eb0440fd0b1"
  ]

}

module "ecs" {

  source = "./modules/ecs"

  project_name = var.project_name

  aws_region = var.aws_region

  execution_role_arn = module.iam.execution_role_arn

  repository_url = module.ecr.repository_url

  security_group_id = module.security.security_group_id

  subnet_ids = module.network.public_subnets

  target_group_arn = module.alb.target_group_arn

  jwt_secret = var.jwt_secret

}