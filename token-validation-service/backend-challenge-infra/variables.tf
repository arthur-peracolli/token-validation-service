variable "region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Project short name"
  type        = string
  default     = "backend-challenge"
}

variable "environment" {
  description = "Deployment environment"
  type        = string
  default     = "develop"
}

variable "app_version" {
  description = "Application version used to name artifact"
  type        = string
  default     = "1.0.0"
}

variable "s3_key" {
  description = "S3 key for the lambda jar. If empty, lambda and API resources are not created (used for bootstrap)."
  type        = string
  default     = ""
}

variable "jwt_secret" {
  description = "JWT secret for application (optional)"
  type        = string
  default     = ""
}

variable "memory_size" {
  type    = number
  default = 512
}

variable "timeout" {
  type    = number
  default = 60
}

variable "lambda_handler" {
  description = "Lambda handler (default uses Spring Cloud Function adapter)"
  type        = string
  default     = "com.challenge.token_validation_service.infrastructure.lambda.TokenValidationHandler::handleRequest"
}

variable "runtime" {
  description = "Lambda runtime"
  type        = string
  default     = "java17"
}

variable "image_uri" {
  description = "(Optional) ECR image URI to deploy as Lambda image. When provided, the image-based Lambda will be created."
  type        = string
  default     = ""
}

