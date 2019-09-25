# Base image uses the Zulu JRE, which gives you free support and security patches when used on Azure.
FROM mcr.microsoft.com/java/jre-headless:8u212-zulu-alpine

RUN apk add openssh \
     && echo "root:Docker!" | chpasswd 

# Copy the application JAR, the SSH config file, and the startup script
COPY ./target/app.jar /usr/src/
COPY sshd_config /etc/ssh/
COPY startup.sh /opt/startup

WORKDIR /usr/src
EXPOSE 80 2222

# startup.sh starts the SSH daemon and the Java app
CMD "/opt/startup/startup.sh"
#CMD ["/usr/sbin/sshd", "java", "-jar", "app.jar"]