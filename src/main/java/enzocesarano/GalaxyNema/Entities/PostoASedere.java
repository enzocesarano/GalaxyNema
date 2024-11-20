package enzocesarano.GalaxyNema.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import enzocesarano.GalaxyNema.Entities.Enums.Fila;
import enzocesarano.GalaxyNema.Entities.Enums.NumeroPosto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PostoASedere {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_postoASedere;
    @Enumerated(EnumType.STRING)
    private Fila fila;
    @Enumerated(EnumType.STRING)
    private NumeroPosto numeroPosto;

    private boolean premium;

    private double prezzo_base;

    @Setter(AccessLevel.NONE)
    private LocalDate created_at;

    @OneToOne(mappedBy = "postoASedere")
    @JsonBackReference
    private Ticket ticket;

    public PostoASedere() {
        this.created_at = LocalDate.now();
        this.prezzo_base = 5.00;
    }
}
