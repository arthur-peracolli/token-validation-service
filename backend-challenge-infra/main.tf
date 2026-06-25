module "network" {
  source = "./modules/network"
}

module "security" {
  source = "./modules/security"
  vpc_id       = module.network.vpc_id
}

module "ecr" {

  source = "./modules/ecr"

  repository_name = "token-validation-service"

}