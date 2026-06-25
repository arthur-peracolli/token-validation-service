variable "project_name" {
  description = "Project name"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "alb_subnet_ids" {
  description = "ALB subnet IDs"
  type        = list(string)
}

variable "security_group_id" {
  description = "ALB Security Group"
  type        = string
}