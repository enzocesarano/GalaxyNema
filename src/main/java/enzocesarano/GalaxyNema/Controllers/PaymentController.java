package enzocesarano.GalaxyNema.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/stripe")
public class PaymentController {

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody Map<String, Object> paymentData) {
        try {
            System.out.println("Dati ricevuti: " + paymentData);

            UUID idProiezione = UUID.fromString(paymentData.get("id_proiezione").toString());
            UUID userId = UUID.fromString(paymentData.get("id_utente").toString());
            String via = (String) paymentData.get("via");
            String civico = (String) paymentData.get("civico");
            String cap = (String) paymentData.get("cap");
            String comune = (String) paymentData.get("comune");
            String provincia = (String) paymentData.get("provincia");

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tickets = (List<Map<String, Object>>) paymentData.get("ticket");

            System.out.println("Ticket ricevuti: " + tickets);

            SessionCreateParams.Builder builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:3000")
                    .setCancelUrl("http://localhost:3000/notfound");

            for (Map<String, Object> ticket : tickets) {
                System.out.println("Ticket: " + ticket);

                // Ottieni i dettagli del ticket
                Map<String, Object> postoASedereMap = (Map<String, Object>) ticket.get("postoASedere");
                String fila = (String) postoASedereMap.get("fila");
                String numeroPosto = (String) postoASedereMap.get("numeroPosto");

                String nome = (String) ticket.get("nome");
                String cognome = (String) ticket.get("cognome");
                Double price = Double.valueOf(ticket.get("price").toString());

                // Calcola il prezzo in centesimi
                Long priceInCents = Math.round(price * 100);

                // Aggiungi la linea per ogni ticket
                builder.addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(priceInCents)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(fila + " " + numeroPosto) // Usa fila e numeroPosto per il nome del prodotto
                                        .setDescription("Biglietto per " + nome + " " + cognome)
                                        .build())
                                .build())
                        .build());
            }

            // Serializza la lista di ticket in JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String ticketsJson = objectMapper.writeValueAsString(tickets);

            // Aggiungi metadati alla sessione Stripe
            builder.putMetadata("via", via)
                    .putMetadata("civico", civico)
                    .putMetadata("cap", cap)
                    .putMetadata("comune", comune)
                    .putMetadata("provincia", provincia)
                    .putMetadata("id_proiezione", idProiezione.toString())
                    .putMetadata("id_utente", userId.toString())
                    .putMetadata("ticket", ticketsJson);  // Assicurati che "ticket" sia una stringa JSON

            // Crea la sessione Stripe
            SessionCreateParams params = builder.build();
            Session session = Session.create(params);

            // Restituisci la risposta con l'id della sessione e l'url
            return ResponseEntity.ok(Map.of("id", session.getId(), "url", session.getUrl()));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}