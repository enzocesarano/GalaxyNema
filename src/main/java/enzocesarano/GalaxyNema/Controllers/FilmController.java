package enzocesarano.GalaxyNema.Controllers;

import enzocesarano.GalaxyNema.Entities.Enums.GenereFilm;
import enzocesarano.GalaxyNema.Entities.Film;
import enzocesarano.GalaxyNema.Services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Film> gettAllFilms(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "titolo") String sortBy) {
        return this.filmService.findAll(page, size, sortBy);
    }

    @GetMapping("/senzaproiezioni")
    @ResponseStatus(HttpStatus.OK)
    public Page<Film> findAllWithoutProiezioni(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "50") int size,
                                               @RequestParam(defaultValue = "titolo") String sortBy
    ) {
        return this.filmService.findAllWithoutProiezioni(page, size, sortBy);
    }

    @GetMapping("/filters")
    @ResponseStatus(HttpStatus.OK)
    public Page<Film> getFilmsWithFilters(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "50") int size,
                                          @RequestParam(defaultValue = "titolo") String sortBy,
                                          @RequestParam(required = false) String titolo,
                                          @RequestParam(required = false) GenereFilm genere,
                                          @RequestParam(required = false) Double minVoteAverage,
                                          @RequestParam(required = false) Double maxVoteAverage,
                                          @RequestParam(required = false) LocalDate proiezioneAfter,
                                          @RequestParam(required = false) LocalDate proiezioneBefore
    ) {
        return this.filmService.findAllWithProiezioni(
                page,
                size,
                sortBy,
                titolo,
                genere,
                minVoteAverage,
                maxVoteAverage,
                proiezioneAfter,
                proiezioneBefore
        );
    }


    @GetMapping("/{id_film}")
    @ResponseStatus(HttpStatus.OK)
    public Film getById(@PathVariable("id_film") UUID id_film) {
        return this.filmService.findById(id_film);
    }

    @GetMapping("/tmdb")
    public List<Film> getFilmsFromTmdb(@RequestParam int number) {
        return filmService.filmByTMDB(number);
    }

    @GetMapping("/news")
    public String getCinemaNews() {
        return filmService.getCinemaNews();
    }
}
