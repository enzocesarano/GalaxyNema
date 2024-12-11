package enzocesarano.GalaxyNema.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ProiezioneDTO(
        @NotNull(message = "La data della proiezione non può essere null.")
        @FutureOrPresent(message = "La data della proiezione deve essere una data futura.")
        LocalDate dataProiezione,

        @NotNull(message = "L'ora di inizio non può essere null.")
        String oraInizio,

        @Positive(message = "Il moltiplicatore di prezzo deve essere un valore positivo.")
        double moltiplicatore_prezzo) {
}
