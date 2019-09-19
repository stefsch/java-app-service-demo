# Builds an image using the Zulu JRE, which gives you free support and security patches on Azure.
FROM mcr.microsoft.com/java/jre-headless:8u212-zulu-alpine
COPY ./target/app.jar /usr/src/
WORKDIR /usr/src
EXPOSE 80
CMD ["java", "-jar", "app.jar"]