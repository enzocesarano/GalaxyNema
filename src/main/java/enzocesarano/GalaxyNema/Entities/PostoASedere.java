package enzocesarano.GalaxyNema.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import enzocesarano.GalaxyNema.Entities.Enums.Fila;
import enzocesarano.GalaxyNema.Entities.Enums.NumeroPosto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PostoASedere {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_postoASedere;

    private Fila fila;
    private NumeroPosto numeroPosto;

    private boolean is_premium;

    @Value("${prezzo_base}")
    private double prezzo_base;

    @Setter(AccessLevel.NONE)
    private LocalDate created_at;

    @OneToOne(mappedBy = "postoASedere")
    @JsonBackReference
    private Ticket ticket;

    public PostoASedere(Fila fila, NumeroPosto numeroPosto) {
        this.fila = fila;
        this.numeroPosto = numeroPosto;
        this.created_at = LocalDate.now();
    }
}
