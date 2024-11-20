package enzocesarano.GalaxyNema.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import enzocesarano.GalaxyNema.Entities.Enums.StatoTicket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_ticket;

    @Setter(AccessLevel.NONE)
    private LocalDate data_acquisto;

    private double prezzo;

    @Enumerated(EnumType.STRING)
    private StatoTicket statoTicket;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonManagedReference
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_proiezione")
    @JsonManagedReference
    private Proiezione proiezione;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_postoASedere")
    @JsonManagedReference
    private PostoASedere postoASedere;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_ticket")
    @JsonBackReference
    private Invoice invoice;

    public Ticket() {
        this.statoTicket = StatoTicket.EMESSO;
        this.data_acquisto = LocalDate.now();
    }
}
