variable "aws_region" {

  description = "AWS Region"

  type = string

}

variable "project_name" {
  description = "Project name"
  type        = string
}

variable "jwt_secret" {
  description = "JWT Secret"
  type        = string
  sensitive   = true
}