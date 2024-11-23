package enzocesarano.GalaxyNema.Controllers;

import enzocesarano.GalaxyNema.Entities.Film;
import enzocesarano.GalaxyNema.Services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id_film}")
    @ResponseStatus(HttpStatus.OK)
    public Film getById(@PathVariable("id_film") UUID id_film) {
        return this.filmService.findById(id_film);
    }

    @GetMapping("/tmdb")
    public List<Film> getFilmsFromTmdb() {
        return filmService.filmByTMDB();
    }
}
