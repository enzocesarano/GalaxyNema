package enzocesarano.GalaxyNema.dto;

import enzocesarano.GalaxyNema.Entities.Enums.Fila;
import enzocesarano.GalaxyNema.Entities.Enums.NumeroPosto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PostoASedereDTO(
        @NotNull(message = "La fila non può essere null.")
        @Pattern(regexp = "^(A|B|C|D|E)$", message = "La fila deve essere una delle seguenti: A, B, C, D, E.")
        Fila fila,

        @NotNull(message = "Il numero del posto non può essere null.")
        @Pattern(regexp = "^P[1-9]|P1[0-5]$", message = "Il numero del posto deve essere uno dei seguenti: P1, P2, ..., P15.")
        NumeroPosto numeroPosto) {
}
