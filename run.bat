@echo off

echo Starting servers...

start cmd /k "java -cp out lb.SimpleServer 1 3001"
start cmd /k "java -cp out lb.SimpleServer 2 3002"
start cmd /k "java -cp out lb.SimpleServer 3 3003"

timeout /t 2

echo Starting load balancer...
java -cp out lb.LoadBalancer