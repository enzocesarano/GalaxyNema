# Usa un'immagine con OpenJDK
FROM openjdk:17-jdk-slim

# Imposta il directory di lavoro dentro il container
WORKDIR /app

# Copia il JAR del tuo progetto nella cartella /app del container
COPY target/GalaxyNema-0.0.1-SNAPSHOT.jar /app/GalaxyNema.jar

# Esponi la porta su cui Spring Boot ascolta
EXPOSE 8080

# Comando per avviare l'applicazione Spring Boot
ENTRYPOINT ["java", "-jar", "GalaxyNema.jar"]
