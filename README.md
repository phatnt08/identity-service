```bash
# Create a custom Docker network to allow containers to communicate
docker network create my-network

# Run a MySQL container on the custom network with a root password
docker run --network my-network --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:latest

# Build the Docker image for the identity-service application with version 1.0.0
docker build -t identity-service:1.0.0 .

# Optionally, build and tag the image for pushing to a Docker registry
docker build -t phatnt8888/identity-service:0.9.0 .

# Push the tagged image to the Docker registry
docker image push phatnt8888/identity-service:0.9.0

# Run the identity-service container on the custom network, linking it to the MySQL container
docker run --network my-network --name identity-service -p 8080:8080 -e DB_URL=jdbc:mysql://mysql:3306/identity-service identity-service:1.0.0

# Pull iamge just push to docke hub
docker pull phatnt8888/identity-service:0.9.0

# Run docker image phatnt8888/identity-service:0.9.0 on docker desktop
docker run --network my-network --name identity-service -p 8080:8080 -e DB_URL=jdbc:mysql://mysql:3306/identity-service phatnt8888/identity-service:0.9.0
```