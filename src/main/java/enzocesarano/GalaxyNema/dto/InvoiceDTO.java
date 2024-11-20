package enzocesarano.GalaxyNema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InvoiceDTO(

        @NotBlank(message = "La via non può essere vuota.")
        @Size(max = 255, message = "La via non può superare i 255 caratteri.")
        String via,

        @NotBlank(message = "Il civico non può essere vuoto.")
        @Pattern(
                regexp = "^[0-9a-zA-Z]+$",
                message = "Il civico può contenere solo lettere e numeri."
        )
        String civico,

        @NotBlank(message = "Il CAP non può essere vuoto.")
        @Pattern(
                regexp = "^\\d{5}$",
                message = "Il CAP deve essere composto da 5 cifre."
        )
        String cap,

        @NotBlank(message = "Il comune non può essere vuoto.")
        @Size(max = 255, message = "Il comune non può superare i 255 caratteri.")
        String comune,

        @NotBlank(message = "La provincia non può essere vuota.")
        @Pattern(
                regexp = "^[A-Z]{2}$",
                message = "La provincia deve essere composta da due lettere maiuscole."
        )
        String provincia) {
}
