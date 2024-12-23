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
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_invoice;

    @Setter(AccessLevel.NONE)
    private LocalDate data_fattura;
    private double importo;

    private String via;
    private String civico;
    private String cap;
    private String comune;
    private String provincia;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ticket> ticket;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonBackReference
    private Utente utente;

    public Invoice(String via, String civico, String cap, String comune, String provincia) {
        this.via = via;
        this.civico = civico;
        this.cap = cap;
        this.comune = comune;
        this.provincia = provincia;
        this.data_fattura = LocalDate.now();
    }
}
