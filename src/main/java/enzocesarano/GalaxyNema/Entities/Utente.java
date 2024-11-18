package enzocesarano.GalaxyNema.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import enzocesarano.GalaxyNema.Entities.Enums.RoleUtente;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Utente {
    @OneToMany(mappedBy = "utente", cascade = CascadeType.REMOVE)
    @JsonBackReference
    List<Ticket> tickets;

    @OneToMany(mappedBy = "admin")
    @JsonBackReference
    List<Film> filmList;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_utente;
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String password;
    private String telefono;
    private LocalDate data_nascita;
    @Enumerated(EnumType.STRING)
    private RoleUtente role;
    @Setter(AccessLevel.NONE)
    private LocalDate created_at;

    private LocalDate update_at;

    public Utente(String nome, String cognome, String username, String email, String password, String telefono, LocalDate data_nascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.data_nascita = data_nascita;
        this.created_at = LocalDate.now();
        this.role = RoleUtente.USER;
    }
}
