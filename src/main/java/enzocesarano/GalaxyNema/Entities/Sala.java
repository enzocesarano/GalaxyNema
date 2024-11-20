package enzocesarano.GalaxyNema.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Sala {
    @OneToMany(mappedBy = "sala")
    @JsonBackReference
    List<Proiezione> proiezioneList;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_sala;

    private String nome;
    private int numero_posti;

    @Setter(AccessLevel.NONE)
    private LocalDate created_at;

    private LocalDate update_at;

    public Sala(String nome) {
        this.nome = nome;
        this.created_at = LocalDate.now();
        this.numero_posti = 75;
    }
}
