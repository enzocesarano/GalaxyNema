package enzocesarano.GalaxyNema.Controllers;


import enzocesarano.GalaxyNema.Entities.Ticket;
import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public Utente getProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @GetMapping("/me/tickets")
    @ResponseStatus(HttpStatus.OK)
    public List<Ticket> ticketListByUtente(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return utenteService.ticketListByUtente(currentAuthenticatedUser);
    }
}
