package enzocesarano.GalaxyNema.Controllers;

import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Exceptions.BadRequestException;
import enzocesarano.GalaxyNema.Services.AuthService;
import enzocesarano.GalaxyNema.Services.UtenteService;
import enzocesarano.GalaxyNema.dto.UtenteDTO;
import enzocesarano.GalaxyNema.dto.UtenteLoginDTO;
import enzocesarano.GalaxyNema.dto.UtenteLoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/login")
    public UtenteLoginResponseDTO login(@RequestBody UtenteLoginDTO body) {
        return new UtenteLoginResponseDTO(this.authService.checkCredenzialiAndToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente save(@RequestBody @Validated UtenteDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.utenteService.saveUtente(payload);
    }
}
