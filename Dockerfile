# Usa un'immagine di base con OpenJDK
FROM openjdk:17-jdk-slim

# Imposta la cartella di lavoro nel container
WORKDIR /app

# Installa Maven (se non è presente)
RUN apt-get update && apt-get install -y maven

# Copia tutto il codice sorgente nel container
COPY . /app

# Esegui il build con Maven, generando il file JAR
RUN mvn clean package -DskipTests

# Copia il file JAR creato dalla cartella target nella cartella di lavoro del container
COPY target/GalaxyNema-0.0.1-SNAPSHOT.jar /app/GalaxyNema.jar

# Esponi la porta 8080 (porta predefinita di Spring Boot)
EXPOSE 8080

# Comando per avviare l'applicazione Spring Boot
ENTRYPOINT ["java", "-jar", "GalaxyNema.jar"]
