terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.0"
    }
  }
}

provider "aws" {
  region = var.region
}

resource "random_id" "bucket_suffix" {
  byte_length = 4
}

resource "aws_s3_bucket" "artifact" {
  bucket = "${var.project_name}-${var.environment}-${random_id.bucket_suffix.hex}"
  acl    = "private"
}

# ECR repository for container image deployments
resource "aws_ecr_repository" "app" {
  name = "${var.project_name}-${var.environment}"
}

# IAM role for lambda
resource "aws_iam_role" "lambda_role" {
  name = "${var.project_name}-${var.environment}-lambda-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = { Service = "lambda.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_basic" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

# Lambda function created only when s3_key is provided (workflow will do a two-step apply)
resource "aws_lambda_function" "app" {
  # Zip/S3 deployment (existing flow)
  count       = var.s3_key != "" ? 1 : 0
  s3_bucket   = aws_s3_bucket.artifact.id
  s3_key      = var.s3_key
  function_name = "${var.project_name}-${var.environment}-api"
  role        = aws_iam_role.lambda_role.arn
  handler     = var.lambda_handler
  runtime     = var.runtime
  timeout     = var.timeout
  memory_size = var.memory_size

  environment {
    variables = {
      JWT_SECRET  = var.jwt_secret
      ENVIRONMENT = var.environment
    }
  }

  tags = {
    Name        = "${var.project_name}-${var.environment}-lambda"
    Environment = var.environment
  }

  lifecycle {
    create_before_destroy = true
  }
}

# Image-based Lambda (created when image_uri is provided)
resource "aws_lambda_function" "image" {
  count        = var.image_uri != "" ? 1 : 0
  package_type = "Image"
  image_uri    = var.image_uri
  function_name = "${var.project_name}-${var.environment}-api"
  role         = aws_iam_role.lambda_role.arn
  timeout      = var.timeout
  memory_size  = var.memory_size

  environment {
    variables = {
      JWT_SECRET  = var.jwt_secret
      ENVIRONMENT = var.environment
    }
  }

  tags = {
    Name        = "${var.project_name}-${var.environment}-lambda"
    Environment = var.environment
  }

  lifecycle {
    create_before_destroy = true
  }
}

# API Gateway resources created only when lambda exists
resource "aws_api_gateway_rest_api" "api" {
  count       = (var.s3_key != "" || var.image_uri != "") ? 1 : 0
  name        = "${var.project_name}-${var.environment}-api"
  description = "API Gateway for ${var.project_name} (${var.environment})"
}

resource "aws_api_gateway_resource" "validate" {
  count      = (var.s3_key != "" || var.image_uri != "") ? 1 : 0
  rest_api_id = aws_api_gateway_rest_api.api[0].id
  parent_id   = aws_api_gateway_rest_api.api[0].root_resource_id
  path_part   = "api"
}

resource "aws_api_gateway_resource" "validate_method" {
  count      = (var.s3_key != "" || var.image_uri != "") ? 1 : 0
  rest_api_id = aws_api_gateway_rest_api.api[0].id
  parent_id   = aws_api_gateway_resource.validate[0].id
  path_part   = "validate"
}

resource "aws_api_gateway_method" "validate_get" {
  count      = (var.s3_key != "" || var.image_uri != "") ? 1 : 0
  rest_api_id = aws_api_gateway_rest_api.api[0].id
  resource_id = aws_api_gateway_resource.validate_method[0].id
  http_method = "GET"
  authorization = "NONE"

  request_parameters = {
    "method.request.querystring.token" = true
  }
}

resource "aws_api_gateway_integration" "validate_integration" {
  count      = (var.s3_key != "" || var.image_uri != "") ? 1 : 0
  rest_api_id = aws_api_gateway_rest_api.api[0].id
  resource_id = aws_api_gateway_resource.validate_method[0].id
  http_method = aws_api_gateway_method.validate_get[0].http_method
  integration_http_method = "POST"
  type = "AWS_PROXY"
  # try will pick the first available invoke_arn without failing when one resource has count = 0
  uri  = try(aws_lambda_function.app[0].invoke_arn, aws_lambda_function.image[0].invoke_arn)
}

resource "aws_lambda_permission" "api_gateway" {
  count = (var.s3_key != "" || var.image_uri != "") ? 1 : 0
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = try(aws_lambda_function.app[0].function_name, aws_lambda_function.image[0].function_name)
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.api[0].execution_arn}/*/*"
}

resource "aws_api_gateway_deployment" "deploy" {
  count = var.s3_key != "" ? 1 : 0
  depends_on = [
    aws_api_gateway_integration.validate_integration
  ]

  rest_api_id = aws_api_gateway_rest_api.api[0].id
  stage_name  = var.environment

  triggers = {
    redeployment = sha1(jsonencode([
      aws_api_gateway_resource.validate_method[0].id,
      aws_api_gateway_method.validate_get[0].id,
      aws_api_gateway_integration.validate_integration[0].id,
    ]))
  }

  lifecycle {
    create_before_destroy = true
  }
}

