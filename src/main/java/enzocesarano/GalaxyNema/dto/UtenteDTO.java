package enzocesarano.GalaxyNema.dto;

import enzocesarano.GalaxyNema.Repositories.OnCreate;
import enzocesarano.GalaxyNema.Repositories.OnUpdate;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UtenteDTO(
        @NotBlank(message = "Il nome è obbligatorio", groups = OnCreate.class)
        @Size(min = 2, max = 50, message = "Il nome deve essere compreso tra 2 e 50 caratteri", groups = {OnCreate.class, OnUpdate.class})
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio", groups = OnCreate.class)
        @Size(min = 2, max = 50, message = "Il cognome deve essere compreso tra 2 e 50 caratteri", groups = {OnCreate.class, OnUpdate.class})
        String cognome,

        @NotBlank(message = "L'username è obbligatorio", groups = OnCreate.class)
        @Size(min = 4, max = 20, message = "L'username deve essere compreso tra 4 e 20 caratteri", groups = {OnCreate.class, OnUpdate.class})
        String username,

        @NotBlank(message = "L'email è obbligatoria", groups = OnCreate.class)
        @Email(message = "Il formato dell'email non è valido", groups = {OnCreate.class, OnUpdate.class})
        String email,

        @NotBlank(message = "La password è obbligatoria", groups = OnCreate.class)
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale",
                groups = OnCreate.class
        )
        String password,

        @Pattern(
                regexp = "^\\+?[0-9]{8,15}$",
                message = "Il numero di telefono deve essere valido e contenere tra 8 e 15 cifre",
                groups = {OnCreate.class, OnUpdate.class}
        )
        String telefono,

        @NotNull(message = "La data di nascita è obbligatoria", groups = OnCreate.class)
        @Past(message = "La data di nascita deve essere nel passato", groups = {OnCreate.class, OnUpdate.class})
        LocalDate data_nascita
) {
}


