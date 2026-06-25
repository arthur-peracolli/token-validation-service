output "vpc_id" {
  description = "Default VPC ID"
  value       = module.network.vpc_id
}

output "public_subnets" {
  description = "Default public subnets"
  value       = module.network.public_subnets
}

output "availability_zones" {
  description = "Availability Zones"
  value       = module.network.availability_zones
}