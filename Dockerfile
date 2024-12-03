# Usa un'immagine con OpenJDK
FROM openjdk:17-jdk-slim

# Imposta il directory di lavoro dentro il container
WORKDIR /app

# Copia il codice sorgente nel container
COPY . /app

# Installa Maven (se non è già presente nell'immagine base)
RUN apt-get update && apt-get install -y maven

# Esegui il comando Maven per costruire il JAR
RUN mvn clean package -DskipTests

# Copia il JAR generato nella cartella di lavoro del container
COPY target/GalaxyNema-0.0.1-SNAPSHOT.jar /app/GalaxyNema.jar

# Esponi la porta su cui Spring Boot ascolta
EXPOSE 8080

# Comando per avviare l'applicazione Spring Boot
ENTRYPOINT ["java", "-jar", "GalaxyNema.jar"]