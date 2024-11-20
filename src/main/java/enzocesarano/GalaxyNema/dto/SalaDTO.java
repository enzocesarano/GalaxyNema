package enzocesarano.GalaxyNema.dto;

import jakarta.validation.constraints.NotBlank;

public record SalaDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        String nome) {
}
