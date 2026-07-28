# Java Distributed Load Balancer

A custom distributed load balancer implemented in Java and deployed using Docker Compose.

The project demonstrates how a load balancer can act as an entry point for a microservice architecture by distributing client traffic, monitoring backend availability, and forwarding requests to healthy service instances.

The load balancer is implemented from scratch using Java networking APIs without external frameworks.

---

## Architecture

The system consists of:

- Java Load Balancer
- API Gateway
- User Service
- Product Service
- PostgreSQL databases

Architecture overview:

    Client
       |
       v
    Load Balancer :8080
       |
       v
    API Gateway :9000
       |
       +----------------+
       |                |
       v                v
 User Service      Product Service
    :9001              :9002
       |                |
       v                v
 Users Database   Products Database


The Load Balancer serves as the single entry point for client requests and forwards traffic to the API Gateway.

---

## Technologies

- Java 17
- Java HTTP Client
- Java Networking API
- Java Concurrency Utilities
- Docker
- Docker Compose

---

## Project Structure

    LoadBalancer/
    |
    ├── src/
    │   └── lb/
    │       ├── LoadBalancer.java
    │       ├── SimpleServer.java
    │       |
    │       ├── core/
    │       │   └── BalancerState.java
    │       |
    │       ├── handler/
    │       │   ├── HealthChecker.java
    │       │   ├── MetricsHandler.java
    │       │   └── ProxyHandler.java
    │       |
    │       └── strategy/
    │           ├── BalancerStrategy.java
    │           └── WeightedLeastConnectionsStrategy.java
    |
    ├── Dockerfile
    ├── docker-compose.yml
    ├── .dockerignore
    ├── run.bat
    └── README.md

---

## How It Works

1. Client sends an HTTP request to the Load Balancer on port 8080.

2. The Load Balancer checks available backend services.

3. A routing strategy selects the best available service.

4. The request is forwarded to the API Gateway.

5. The API Gateway routes the request to the correct microservice.

6. The response is returned back to the client through the Load Balancer.

---

## Load Balancing Strategy

The project implements a Weighted Least Connections strategy.

The strategy considers:

- Active connections
- Server availability
- Response latency

The goal is to forward requests to the service instance with the lowest current load.

---

## Health Checking

The Load Balancer continuously monitors backend availability.

Health checks are performed periodically by sending:

    GET /health

to registered services.

Unavailable services are automatically marked as inactive and removed from request routing.

---

## Metrics

The Load Balancer provides monitoring information through:

    GET /metrics

Available information includes:

- Active connections
- Request count
- Response latency
- Service availability

---

## Running the Project

Make sure Docker is installed.

Clone the repository:

    git clone https://github.com/jkovvv/load-balancer.git

Start the application:

    docker compose up --build

All services will start automatically.

---

## Accessing the Application

Load Balancer:

    http://localhost:8080


Example request:

    curl localhost:8080/users


Request flow:

    Client
      |
      v
    Load Balancer
      |
      v
    API Gateway
      |
      v
    Microservice

---

## Docker Services

| Service | Port | Description |
|---|---|---|
| Load Balancer | 8080 | Entry point for client requests |
| API Gateway | 9000 | Routes requests to microservices |
| User Service | 9001 | User operations |
| Product Service | 9002 | Product operations |
| PostgreSQL | 5432/5433 | Application databases |

---

## Useful Commands

Start services:

    docker compose up


Build images:

    docker compose build


Rebuild and start:

    docker compose up --build


Stop services:

    docker compose down


Check running containers:

    docker ps


View logs:

    docker compose logs

---

## Features

- Custom Java implementation without frameworks
- Reverse proxy functionality
- API Gateway integration
- Health monitoring
- Backend availability detection
- Weighted Least Connections routing strategy
- Request forwarding
- Latency tracking
- Metrics endpoint
- Docker Compose deployment
- Microservice architecture support

---

## Future Improvements

- Support for multiple API Gateway instances
- Dynamic service discovery
- Advanced routing algorithms
- Rate limiting
- Circuit breaker pattern
- Kubernetes deployment
- Distributed monitoring

---

## Related Projects

This project is part of a larger microservice ecosystem:

API Gateway:
https://github.com/jkovvv/api-gateway

Microservices System:
https://github.com/jkovvv/microservices-system

User Service:
https://github.com/jkovvv/user-service

Product Service:
https://github.com/jkovvv/product-service
