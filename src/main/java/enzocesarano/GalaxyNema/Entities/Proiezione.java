package enzocesarano.GalaxyNema.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Proiezione {
    @OneToMany(mappedBy = "proiezione")
    @JsonBackReference
    List<Ticket> ticketList;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_proiezione;
    @Column(name = "data_proiezione")
    private LocalDate dataProiezione;
    @Column(name = "ora_inizio")
    private LocalDateTime oraInizio;
    @Column(name = "ora_fine")
    private LocalDateTime oraFine;
    private double moltiplicatore_prezzo;
    @Setter(AccessLevel.NONE)
    private LocalDate created_at;
    @ManyToOne
    @JoinColumn(name = "id_sala")
    @JsonManagedReference
    private Sala sala;
    @ManyToOne
    @JoinColumn(name = "id_film")
    @JsonManagedReference
    private Film film;

    public Proiezione() {
        this.created_at = LocalDate.now();
    }
}
