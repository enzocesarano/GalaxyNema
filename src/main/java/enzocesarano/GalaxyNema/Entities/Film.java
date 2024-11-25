package enzocesarano.GalaxyNema.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import enzocesarano.GalaxyNema.Entities.Enums.GenereFilm;
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
public class Film {
    @OneToMany(mappedBy = "film")
    @JsonManagedReference
    List<Proiezione> proiezioneList;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_film;

    private String titolo;

    @Column(length = 2048)
    private String descrizione;
    private int durata;

    @Enumerated(EnumType.STRING)
    private GenereFilm genere;

    @Column(name = "data_uscita")
    private LocalDate dataUscita;
    private String poster_url;
    private String trailer_url;
    private String backdrop_url;
    @Column(name = "vote_average")
    private double voteAverage;

    @Setter(AccessLevel.NONE)
    private LocalDate created_at = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "id_admin")
    @JsonManagedReference
    private Utente admin;

    public Film(String titolo, String descrizione, int durata, GenereFilm genere, LocalDate data_uscita, String poster_url, String trailer_url) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.durata = durata;
        this.genere = genere;
        this.dataUscita = data_uscita;
        this.poster_url = poster_url;
        this.trailer_url = trailer_url;
    }
}

