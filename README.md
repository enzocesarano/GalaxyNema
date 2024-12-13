# 🌜 GALAXYNEMA PROJECT 🎬

## Descrizione del Progetto

Galaxy Nema è un'applicazione backend progettata per gestire un sistema di prenotazione di biglietti per film. Consente agli utenti di registrarsi, effettuare il login, visualizzare film e proiezioni, acquistare biglietti e gestire il proprio profilo. Utilizza tecnologie moderne e un'architettura RESTful per fornire un'interfaccia semplice e intuitiva.

## Tecnologie Utilizzate

- **Java 17**: Linguaggio di programmazione principale.
- **Spring Boot 3.3.5**: Framework per lo sviluppo di applicazioni Java.
- **Spring Data JPA**: Per l'interazione con il database.
- **Spring Security**: Per la gestione della sicurezza e dell'autenticazione.
- **PostgreSQL**: Database relazionale per la memorizzazione dei dati.
- **Stripe**: Per la gestione dei pagamenti online.
- **Maven**: Gestore di dipendenze e build.

## Struttura del Progetto

Il progetto è organizzato in pacchetti, ognuno dei quali ha una responsabilità specifica:

- **Controllers**: Contiene i controller REST per gestire le richieste HTTP.
- **Entities**: Contiene le classi che rappresentano le entità del database.
- **Services**: Contiene la logica di business e le interazioni con il database.
- **DTOs**: Contiene le classi Data Transfer Object per la comunicazione tra client e server.
- **Exceptions**: Contiene le classi per la gestione delle eccezioni personalizzate.

## Endpoint Principali

### Autenticazione

- **POST /auth/login**: Effettua il login e restituisce un token di autenticazione.
- **POST /auth/register**: Registra un nuovo utente.

### Film

- **GET /films**: Recupera tutti i film con paginazione.
- **GET /films/senzaproiezioni**: Recupera film senza proiezioni.
- **GET /films/filters**: Recupera film con filtri specifici.
- **GET /films/{id_film}**: Recupera un film specifico per ID.

### Proiezioni

- **GET /proiezioni**: Recupera tutte le proiezioni con paginazione.
- **GET /proiezioni/{id_proiezione}**: Recupera una proiezione specifica per ID.

### Pagamenti (Stripe)

- **POST /api/stripe/create-checkout-session**: Crea una sessione di checkout per il pagamento. Utilizza Stripe per elaborare il pagamento in modo sicuro.
- **POST /api/stripe/webhook**: Gestisce i webhook di Stripe per gli eventi di pagamento, come la conferma della transazione.

### Utente

- **GET /me**: Recupera il profilo dell'utente autenticato.
- **PUT /me**: Aggiorna il profilo dell'utente.
- **DELETE /me**: Elimina il profilo dell'utente.
- **POST /me/invoices**: Crea una nuova fattura per l'utente.

### Preferiti

- **GET /me/films/preferiti**: Recupera i film preferiti dell'utente.
- **POST /me/films/{id_film}/preferiti**: Aggiunge un film ai preferiti.
- **DELETE /me/films/{id_film}/preferiti**: Rimuove un film dai preferiti.

## Configurazione

1. **Clona il repository**:
   ```bash
   git clone https://github.com/tuo-username/galaxy-nema-backend.git
   cd galaxy-nema-backend
   ```

2. **Configura il database**:
   - Configura le credenziali del database nel file `application.properties` o `application.yml`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/galaxy_nema
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

3. **Configura Stripe**:
   - Crea un account su [Stripe](https://stripe.com).
   - Aggiungi la tua **chiave segreta** Stripe nel file `application.properties`:

   ```properties
   stripe.api.key=your-stripe-secret-key
   ```

4. **Costruisci il progetto**:
   Assicurati di avere **Java 17** e **Maven** installati. Esegui il comando per costruire il progetto:

   ```bash
   mvn clean install
   ```

5. **Avvia l'applicazione**:

   ```bash
   mvn spring-boot:run
   ```

   Il backend sarà disponibile su [http://localhost:8080](http://localhost:8080).

## Contributi

Se desideri contribuire al progetto, segui questi passi:

1. Fork il repository.
2. Crea un nuovo ramo (`git checkout -b feature/your-feature`).
3. Fai le modifiche necessarie.
4. Crea un pull request.

## Autore

- **Nome**: Vincenzo  
- **Email**: vinc.cesarano@gmail.com  
- **LinkedIn**: [[Link al profilo]](https://www.linkedin.com/in/vincenzo-cesarano-6b2602252/)

## Frontend

Per il frontend del progetto, visita il [repository frontend di Galaxy Nema](https://github.com/enzocesarano/GalaxyNema-FE).
