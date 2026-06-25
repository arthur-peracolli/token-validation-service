output "alb_arn" {
  value = aws_lb.alb.arn
}

output "alb_dns" {
  value = aws_lb.alb.dns_name
}

output "target_group_arn" {
  value = aws_lb_target_group.tg.arn
}

output "listener_arn" {
  value = aws_lb_listener.http.arn
}