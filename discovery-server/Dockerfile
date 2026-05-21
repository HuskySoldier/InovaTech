FROM eclipse-temurin:21-jre-alpine

# Instalamos curl para que el healthcheck pueda ejecutarse
RUN apk add --no-cache curl

WORKDIR /app

# Copiamos directamente el .jar que ya compilaste en tu PC
COPY target/*.jar app.jar

# Exponemos el puerto de Eureka
EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]