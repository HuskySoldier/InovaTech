# Etapa 1: Compilar
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn install -N -f pom.xml
COPY proyectos/pom.xml proyectos/pom.xml
COPY proyectos/src proyectos/src
RUN mvn package -DskipTests -f proyectos/pom.xml

# Etapa 2: Ejecutar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/proyectos/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
