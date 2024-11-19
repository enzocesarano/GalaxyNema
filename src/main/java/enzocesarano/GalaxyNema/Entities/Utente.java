package enzocesarano.GalaxyNema.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import enzocesarano.GalaxyNema.Entities.Enums.RoleUtente;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"id_utente", "password", "authorities", "enabled", "credentialsNonExpired", "accountNonExpired", "accountNonLocked"})
public class Utente implements UserDetails {
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

    private String avatar;

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
        this.avatar = "https://ui-avatars.com/api/?name=" + this.getNome() + "+" + this.getCognome();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

}
