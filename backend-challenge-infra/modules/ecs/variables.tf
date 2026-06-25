variable "project_name" {
  type = string
}

variable "aws_region" {
  type = string
}

variable "execution_role_arn" {
  type = string
}

variable "repository_url" {
  type = string
}

variable "security_group_id" {
  type = string
}

variable "subnet_ids" {
  type = list(string)
}

variable "target_group_arn" {
  type = string
}

variable "jwt_secret" {
  type      = string
  sensitive = true
}