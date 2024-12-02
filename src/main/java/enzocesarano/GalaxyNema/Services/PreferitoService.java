package enzocesarano.GalaxyNema.Services;

import enzocesarano.GalaxyNema.Entities.Film;
import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PreferitoService {
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private UtenteRepository utenteRepository;

    @Transactional
    public Film aggiungiPreferito(UUID idUtente, UUID idFilm) {
        Utente utente = utenteService.findById(idUtente);
        Film film = filmService.findById(idFilm);
        utente.getPreferiti().add(film);
        utenteRepository.save(utente);
        return film;
    }

    @Transactional
    public void rimuoviPreferito(UUID idUtente, UUID idFilm) {
        Utente utente = utenteService.findById(idUtente);
        Film film = filmService.findById(idFilm);
        utente.getPreferiti().remove(film);
        utenteRepository.save(utente);
    }
}