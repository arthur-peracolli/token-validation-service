<#
Local helper script to build the JAR and Docker image and push to a registry.
Usage:
  .\build-and-push.ps1 -RegistryServer "ghcr.io" -ImageName "owner/repo" -Tag "latest"

Environment:
  - Requires Docker installed and logged in, or provide credentials via Docker login beforehand.
#>

param(
  [string]$RegistryServer = "ghcr.io",
  [string]$ImageName = "my-image",
  [string]$Tag = "latest",
  [switch]$SkipTests
)

Write-Host "Building project JAR..."
& .\mvnw.cmd package $(if ($SkipTests) {"-DskipTests"} else {""})

if ($LASTEXITCODE -ne 0) { throw "Maven build failed" }

$fullTag = "$RegistryServer/$ImageName:$Tag"
Write-Host "Building Docker image $fullTag"
docker build -t $fullTag -f Dockerfile .
if ($LASTEXITCODE -ne 0) { throw "Docker build failed" }

Write-Host "Pushing Docker image $fullTag"
docker push $fullTag
if ($LASTEXITCODE -ne 0) { throw "Docker push failed" }

Write-Host "Image pushed: $fullTag"

