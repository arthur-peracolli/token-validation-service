output "vpc_id" {

  value = data.aws_vpc.default.id

}

output "public_subnets" {

  value = data.aws_subnets.default.ids

}

output "availability_zones" {

  value = [
    for subnet in data.aws_subnet.selected :
    subnet.availability_zone
  ]

}