# Base image uses the Zulu JRE, which gives you free support and security patches when used on Azure.
FROM mcr.microsoft.com/java/jre-headless:8u212-zulu-alpine

# App Service needs these env vars defined
ENV PORT 8080
ENV SSH_PORT 2222 

# Install ssh server, set username/password
RUN apk add openssh \
     && echo "root:Docker!" | chpasswd 

# Copy the application JAR, the SSH config file, and the startup script
COPY ./target/app.jar /usr/src/
COPY startup/sshd_config /etc/ssh/
COPY startup/startup.sh /opt/startup/
COPY startup/ssh_setup.sh /opt/startup/

RUN chmod -R +x /opt/startup/ssh_setup.sh \
   && (sleep 1;/opt/startup/ssh_setup.sh 2>&1 > /dev/null)

WORKDIR /usr/src
EXPOSE 8080 2222

# startup.sh starts the SSH daemon and the Java app
CMD ["sh", "/opt/startup/startup.sh"]