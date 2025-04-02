# java-identity

# docker network create my-network
# docker run --network my-network --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:latest
# docker build -t identity-service:1.0.0 .
# docker run --network my-network --name identity-service -p 8080:8080 -e DB_URL=jdbc:mysql://mysql:3306/identity-service identity-service:1.0.0