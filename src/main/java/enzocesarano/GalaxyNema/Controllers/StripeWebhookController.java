package enzocesarano.GalaxyNema.Controllers;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import enzocesarano.GalaxyNema.Services.StripeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    private final StripeService stripeService;
    @Value("${stripe.secret.key}")
    private String webhookSecret;

    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            // Verifica la firma del webhook per garantire che l'evento provenga da Stripe
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            // Se la firma non è valida, rispondi con un errore
            return ResponseEntity.status(400).body("Webhook error: " + e.getMessage());
        }

        // Gestisci l'evento
        switch (event.getType()) {
            case "checkout.session.completed":
                // Logica per gestire il pagamento completato
                break;
            case "payment_intent.succeeded":
                // Logica per gestire il pagamento riuscito
                break;
            case "payment_intent.payment_failed":
                // Logica per gestire il pagamento fallito
                break;
            default:

        }

        return ResponseEntity.ok("Event received");
    }
}
