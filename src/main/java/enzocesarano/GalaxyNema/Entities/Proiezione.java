package enzocesarano.GalaxyNema.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Proiezione {
    @OneToMany(mappedBy = "proiezione")
    @JsonBackReference
    List<Ticket> ticketList;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_proiezione;
    private LocalDate ora_inizio;
    private LocalDate ora_fine;
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

    public Proiezione(LocalDate ora_inizio, LocalDate ora_fine, double moltiplicatore_prezzo) {
        this.ora_inizio = ora_inizio;
        this.ora_fine = ora_fine;
        this.moltiplicatore_prezzo = moltiplicatore_prezzo;
        this.created_at = LocalDate.now();
    }
}
