# Utiliza una imagen base de OpenJDK 20
FROM openjdk:20-jdk-slim

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR de tu proyecto Spring Boot al contenedor
COPY target/airline-api-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8080 que utiliza Spring Boot
EXPOSE 8009

# Comando para ejecutar la aplicación Spring Boot
CMD ["java", "-jar", "app.jar"]




