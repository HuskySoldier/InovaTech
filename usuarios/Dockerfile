# Etapa 1: Compilar
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn install -N -f pom.xml
COPY usuarios/pom.xml usuarios/pom.xml
COPY usuarios/src usuarios/src
RUN mvn package -DskipTests -f usuarios/pom.xml

# Etapa 2: Ejecutar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/usuarios/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
