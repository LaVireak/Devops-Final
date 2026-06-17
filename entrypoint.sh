#!/bin/bash

# Start SSH service
service ssh start

# Get the container's eth0 IP address
CONTAINER_IP=$(ip -4 addr show eth0 | grep -oP '(?<=inet\s)\d+(\.\d+){3}')
echo "Container IP resolved as: $CONTAINER_IP"

# Configure NGINX to listen on CONTAINER_IP:8080 and proxy to 127.0.0.1:8080
cat <<EOF > /etc/nginx/sites-available/default
server {
    listen ${CONTAINER_IP}:8080;
    server_name _;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host \$http_host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

# Start NGINX service
service nginx start

# Clone the Spring Boot project to /app
echo "Cloning Spring Boot repository..."
rm -rf /app
git clone https://github.com/LaVireak/Devops-Final.git /app

# Build the project
cd /app
echo "Building Spring Boot application..."
mvn clean package -DskipTests

# Wait for MySQL database to be ready before starting Spring Boot
echo "Waiting for MySQL database (B-LA_VIREAK-db:3306) to accept connections..."
while ! timeout 1 bash -c "echo > /dev/tcp/B-LA_VIREAK-db/3306" 2>/dev/null; do
    echo "MySQL is not ready yet. Retrying in 2 seconds..."
    sleep 2
done
echo "MySQL database is ready!"

# Start the Spring Boot application, binding to 127.0.0.1:8080 (so NGINX can proxy it on CONTAINER_IP:8080)
echo "Starting Spring Boot application..."
java -Dserver.address=127.0.0.1 -Dserver.port=8080 -jar target/idcard-0.0.1-SNAPSHOT.jar
