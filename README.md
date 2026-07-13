# Java Docker Load Balancer

A simple distributed load balancer implemented in Java and deployed using Docker Compose.

The project demonstrates how a load balancer distributes client requests between multiple backend server instances running in isolated Docker containers.

## Architecture

The system consists of:

- Load Balancer
- Three backend servers
- Web dashboard served by Nginx

Architecture overview:

```text
                 Client
                    |
                    v
          Load Balancer :8080
                    |
        +-----------+-----------+
        |           |           |
        v           v           v
   Server 1     Server 2     Server 3
    :3001        :3002        :3003


          Dashboard :3000
              |
             Nginx
```

## Technologies

- Java 17
- Docker
- Docker Compose
- Nginx

## Project Structure

```text
LoadBalancer/
│
├── src/
│   └── lb/
│       ├── LoadBalancer.java
│       ├── SimpleServer.java
│       └── other Java classes
│
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── .gitignore
├── run.bat
└── README.md
```

## How It Works

1. The client sends a request to the Load Balancer on port `8080`.
2. The Load Balancer forwards the request to one of the available backend servers.
3. Backend servers process the request and return a response.
4. The response is returned to the client through the Load Balancer.

Each backend server runs as a separate Docker container:

- Server 1 → port `3001`
- Server 2 → port `3002`
- Server 3 → port `3003`

## Running the Project

Make sure Docker is installed.

Build and start all services:

```bash
docker compose up --build
```

## Accessing the Application

### Load Balancer

Open:

```text
http://localhost:8080
```

Example response:

```text
Hello from Server 1
```

Repeated requests are distributed between backend servers.

### Dashboard

Open:

```text
http://localhost:3000
```

## Docker Services

| Service       | Port | Description                     |
| ------------- | ---- | ------------------------------- |
| Load Balancer | 8080 | Entry point for client requests |
| Server 1      | 3001 | Backend server instance         |
| Server 2      | 3002 | Backend server instance         |
| Server 3      | 3003 | Backend server instance         |
| Dashboard     | 3000 | Nginx web interface             |

## Useful Commands

Start services:

```bash
docker compose up
```

Build images:

```bash
docker compose build
```

Start with rebuild:

```bash
docker compose up --build
```

Stop services:

```bash
docker compose down
```

Check running containers:

```bash
docker ps
```

View logs:

```bash
docker compose logs
```

## Features

- Multiple backend server instances
- Request forwarding through a load balancer
- Docker containerization
- Docker Compose orchestration
- Independent backend services
- Simple monitoring dashboard

## Future Improvements

- Health checks for backend servers
- Automatic removal of unavailable servers
- More advanced load balancing algorithms
- Metrics collection
- Authentication and security layer
