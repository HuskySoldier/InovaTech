FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos directamente el .jar que ya compilaste en tu PC
COPY target/*.jar app.jar

# Exponemos el puerto (en el discovery-server cambia esto a EXPOSE 8761)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
