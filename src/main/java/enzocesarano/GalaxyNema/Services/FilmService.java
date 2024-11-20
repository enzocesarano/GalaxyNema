package enzocesarano.GalaxyNema.Services;

import enzocesarano.GalaxyNema.Entities.Enums.RoleUtente;
import enzocesarano.GalaxyNema.Entities.Film;
import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Exceptions.BadRequestException;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Exceptions.UnauthorizedException;
import enzocesarano.GalaxyNema.Repositories.FilmRepository;
import enzocesarano.GalaxyNema.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    public Film findById(UUID id_film) {
        return this.filmRepository.findById(id_film).orElseThrow(() -> new NotFoundException(id_film));
    }

    public Page<Film> findAll(int page, int size, String sortBy) {
        if (size > 15) size = 15;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.filmRepository.findAll(pageable);
    }

    public Film findByTitolo(String titolo) {
        return this.filmRepository.findByTitolo(titolo).orElseThrow(() -> new NotFoundException(titolo));
    }

    public Film saveFilm(FilmDTO body, Utente admin) {
        this.filmRepository.findByTitolo(body.titolo()).ifPresent(film -> {
                    throw new BadRequestException("Il Film " + body.titolo() + " è già presente.");
                }
        );
        Film newFilm = new Film(body.titolo(), body.descrizione(), body.durata(), body.genere(), body.data_uscita(), body.poster_url(), body.trailer_url());
        newFilm.setAdmin(admin);
        return this.filmRepository.save(newFilm);
    }

    public Film findByIdAndUpdate(UUID id_film, FilmDTO body) {
        Film film = this.findById(id_film);

        if (!film.getTitolo().equals(body.titolo())) {
            this.filmRepository.findByTitolo(body.titolo()).ifPresent(
                    user -> {
                        throw new BadRequestException("Titolo " + body.titolo() + " già in uso!");
                    }
            );
        }

        film.setTitolo(body.titolo());
        film.setDescrizione(body.descrizione());
        film.setDurata(body.durata());
        film.setGenere(body.genere());
        film.setData_uscita(body.data_uscita());
        film.setPoster_url(body.poster_url());
        film.setTrailer_url(body.trailer_url());
        return this.filmRepository.save(film);
    }

    public void findByIdAndDelete(UUID film_id, Utente currentAuthenticatedUtente) {
        if (currentAuthenticatedUtente != null) {
            if (currentAuthenticatedUtente.getRole() != RoleUtente.ADMIN) {
                throw new UnauthorizedException("Non hai i permessi per salvare questa proiezione!");
            }
        }
        Film film = this.findById(film_id);
        this.filmRepository.delete(film);
    }
}
