package enzocesarano.GalaxyNema.dto;


import enzocesarano.GalaxyNema.Entities.Enums.GenereFilm;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record FilmDTO(
        @NotBlank(message = "Il titolo non può essere vuoto.")
        @Size(max = 255, message = "Il titolo non può superare i 255 caratteri.")
        String titolo,

        @NotBlank(message = "La descrizione non può essere vuota.")
        @Size(max = 2000, message = "La descrizione non può superare i 2000 caratteri.")
        String descrizione,

        @Min(value = 1, message = "La durata deve essere almeno di 1 minuto.")
        int durata,

        @NotNull(message = "Il genere del film è obbligatorio.")
        GenereFilm genere,

        @NotNull(message = "La data di uscita è obbligatoria.")
        LocalDate dataUscita,

        @NotBlank(message = "L'URL del poster non può essere vuoto.")
        @Pattern(
                regexp = "^(http|https)://.*$",
                message = "L'URL del poster deve essere un link valido."
        )
        String poster_url,

        String trailer_url,

        String backdrop_url
) {
}
