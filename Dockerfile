FROM eclipse-temurin:21-jdk-jammy

# Install NGINX, SSH server, Git, Maven, and helper networking tools
RUN apt-get update && apt-get install -y \
    nginx \
    openssh-server \
    git \
    maven \
    curl \
    iproute2 \
    && rm -rf /var/lib/apt/lists/*

# Configure SSH server
RUN mkdir /var/run/sshd
RUN echo 'root:Hello@123' | chpasswd
RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed -i 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' /etc/pam.d/sshd

# Set environment variables for SSH session visibility
ENV NOTVISIBLE="in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

# Copy the entrypoint script into the container
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Expose ports: 8080 (NGINX inside container), 22 (SSH inside container)
EXPOSE 8080 22

ENTRYPOINT ["/entrypoint.sh"]
