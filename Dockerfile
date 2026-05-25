# =========================================
# Etapa 1: Build
# =========================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Recibimos el nombre de la carpeta del microservicio como argumento
ARG MODULE

# Copiamos todo el código fuente del proyecto (incluido el pom padre)
COPY . .

# Compilamos SOLO el módulo específico y sus dependencias internas (-am)
RUN mvn clean package -pl ${MODULE} -am -DskipTests

# =========================================
# Etapa 2: Runtime
# =========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# ¡AÑADE ESTA LÍNEA! Instalamos curl para que los healthchecks funcionen
RUN apk add --no-cache curl

# Volvemos a declarar el argumento para esta etapa
ARG MODULE

# Copiamos el jar compilado desde la carpeta del módulo correspondiente
COPY --from=build /app/${MODULE}/target/*.jar app.jar

# El puerto interno (8080 por defecto, pero Discovery Server usa 8761)
EXPOSE 8080
EXPOSE 8761

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]