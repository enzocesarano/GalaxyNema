package enzocesarano.GalaxyNema.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import enzocesarano.GalaxyNema.Entities.Enums.Fila;
import enzocesarano.GalaxyNema.Entities.Enums.NumeroPosto;
import enzocesarano.GalaxyNema.Entities.Invoice;
import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Services.InvoiceService;
import enzocesarano.GalaxyNema.Services.UtenteService;
import enzocesarano.GalaxyNema.dto.InvoiceDTO;
import enzocesarano.GalaxyNema.dto.PostoASedereDTO;
import enzocesarano.GalaxyNema.dto.TicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    @Value("${webhook.secret.key}")
    private String webhookSecret;

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Errore nel Webhook: " + e.getMessage());
        }

        if ("checkout.session.completed".equals(event.getType())) {
            return handleCheckoutSessionCompleted(event);
        }

        return ResponseEntity.ok("Evento ricevuto.");
    }

    private ResponseEntity<String> handleCheckoutSessionCompleted(Event event) {
        try {
            Session session = (Session) event.getData().getObject();

            System.out.println("Sessione ricevuta: " + session);
            System.out.println("Metadati: " + session.getMetadata());

            Utente utente = this.utenteService.findById(UUID.fromString(session.getMetadata().get("id_utente")));
            UUID idProiezione = UUID.fromString(session.getMetadata().get("id_proiezione"));
            String via = session.getMetadata().get("via");
            String civico = session.getMetadata().get("civico");
            String cap = session.getMetadata().get("cap");
            String comune = session.getMetadata().get("comune");
            String provincia = session.getMetadata().get("provincia");

            String ticketsJson = session.getMetadata().get("ticket");
            List<TicketDTO> tickets = null;
            if (ticketsJson != null && !ticketsJson.isEmpty()) {
                try {
                    tickets = new ObjectMapper().readValue(ticketsJson, new ObjectMapper().getTypeFactory().constructCollectionType(List.class, TicketDTO.class));
                } catch (Exception e) {
                    System.err.println("Errore nel parsing del JSON dei ticket: " + e.getMessage());
                }
            }

            String postoASedere = session.getMetadata().get("postoASedere");
            PostoASedereDTO postoASedereDTO = null;
            if (postoASedere != null && !postoASedere.isEmpty()) {
                String[] parts = postoASedere.split(" ");
                if (parts.length == 2) {
                    Fila fila = Fila.valueOf(parts[0]);
                    NumeroPosto numeroPosto = NumeroPosto.valueOf(parts[1]);
                    postoASedereDTO = new PostoASedereDTO(fila, numeroPosto);
                } else {
                    System.err.println("Formato postoASedere non valido. Atteso formato 'A P1'.");
                }
            }

            InvoiceDTO invoiceDTO = new InvoiceDTO(via, civico, cap, comune, provincia, tickets);
            Invoice invoice = invoiceService.saveInvoice(invoiceDTO, utente, idProiezione);

            return ResponseEntity.ok("Checkout completato con successo. Invoice ID: " + invoice.getId_invoice());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Errore durante l'elaborazione: " + e.getMessage());
        }
    }


}
