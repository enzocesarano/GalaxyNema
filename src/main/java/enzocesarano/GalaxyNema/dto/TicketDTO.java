package enzocesarano.GalaxyNema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TicketDTO(
        @NotBlank(message = "Il nome non può essere nullo o vuoto.")
        String nome,

        @NotBlank(message = "Il cognome non può essere nullo o vuoto.")
        String cognome,

        @NotBlank(message = "La data di nascita non può essere nulla o vuota.")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La data di nascita deve essere nel formato YYYY-MM-DD.")
        String data_nascita,

        @NotBlank(message = "Il prezzo non può essere nullo.")
        Double price,
        PostoASedereDTO postoASedere) {
}
