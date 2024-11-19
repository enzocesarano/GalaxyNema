package enzocesarano.GalaxyNema.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UtenteDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 50, message = "Il nome deve essere compreso tra 2 e 50 caratteri")
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 50, message = "Il cognome deve essere compreso tra 2 e 50 caratteri")
        String cognome,

        @NotBlank(message = "L'username è obbligatorio")
        @Size(min = 4, max = 20, message = "L'username deve essere compreso tra 4 e 20 caratteri")
        String username,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Il formato dell'email non è valido")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale"
        )
        String password,

        @NotBlank(message = "Il telefono è obbligatorio")
        @Pattern(
                regexp = "^\\+?[0-9]{8,15}$",
                message = "Il numero di telefono deve essere valido e contenere tra 8 e 15 cifre"
        )
        String telefono,

        @NotNull(message = "La data di nascita è obbligatoria")
        @Past(message = "La data di nascita deve essere nel passato")
        LocalDate data_nascita
) {
}

