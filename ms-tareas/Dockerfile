# Etapa 1: Compilar
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn install -N -f pom.xml
COPY ms-tareas/pom.xml ms-tareas/pom.xml
COPY ms-tareas/src ms-tareas/src
RUN mvn package -DskipTests -f ms-tareas/pom.xml

# Etapa 2: Ejecutar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/ms-tareas/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
