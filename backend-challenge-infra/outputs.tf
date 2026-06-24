output "artifact_bucket" {
  description = "S3 bucket where artifacts are uploaded"
  value       = aws_s3_bucket.artifact.bucket
}

output "api_url" {
  description = "API URL (when lambda and API are created)"
  value = (var.s3_key != "" || var.image_uri != "") ? "https://${aws_api_gateway_rest_api.api[0].id}.execute-api.${var.region}.amazonaws.com/${var.environment}/api/validate" : ""
}

output "ecr_repository_url" {
  description = "ECR repository URL (if created)"
  value       = aws_ecr_repository.app.repository_url
}

