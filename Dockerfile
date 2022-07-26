FROM openjdk:18-jdk-alpine

ENV DATABASE_USERNAME=app
ENV DATABASE_PASSWORD=password123
ENV DATABASE_URL="jdbc:mysql://localhost:3306/app"
ENV APP_DOMAIN="localhost"
ENV FE_URL="http://localhost:3000"
ENV VERSION="1.1.0-1"
ENV ENVIRONMENT="local"
ENV TOKEN_SECRET="LKJDHALKJSHDLKAJSHDLKAJSHDLKASJHDLKJASHDLKJAHSDLKJHASLDKJh"
ENV TOKEN_EXPIRATION=1
ENV TOKEN_EXPIRATION_ANONYMOUS=43800

COPY ./target/*.jar ./app.jar

CMD ["java", "-Dserver.port=80","-Dproject.version=${VERSION}", \
    "-Dspring.profiles.active=${ENVIRONMENT}", \
    "-jar", "./app.jar"]