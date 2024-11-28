package enzocesarano.GalaxyNema.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import enzocesarano.GalaxyNema.Entities.Enums.GenereFilm;
import enzocesarano.GalaxyNema.Entities.Enums.RoleUtente;
import enzocesarano.GalaxyNema.Entities.Film;
import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Exceptions.BadRequestException;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Exceptions.UnauthorizedException;
import enzocesarano.GalaxyNema.Repositories.FilmRepository;
import enzocesarano.GalaxyNema.dto.FilmDTO;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private UtenteService utenteService;

    @Value("${news_api_key}")
    private String newsApi;

    @Value("${auth_tmdb}")
    private String authTmdb;

    public Film findById(UUID id_film) {
        return this.filmRepository.findById(id_film).orElseThrow(() -> new NotFoundException(id_film));
    }

    public Page<Film> findAll(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.filmRepository.findAll(pageable);
    }

    public Page<Film> findAllWithoutProiezioni(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Specification<Film> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.isEmpty(root.get("proiezioneList")));

        return this.filmRepository.findAll(spec, pageable);
    }


    public Page<Film> findAllWithProiezioni(int page, int size, String sortBy, String titolo, GenereFilm genere,
                                            Double minVoteAverage, Double maxVoteAverage,
                                            LocalDate proiezioneAfter, LocalDate proiezioneBefore) {
        if (size > 30) size = 30;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Specification<Film> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.isNotEmpty(root.get("proiezioneList")));

        if (titolo != null && !titolo.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("titolo")), "%" + titolo.toLowerCase() + "%"));
        }

        if (genere != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("genere"), genere));
        }

        if (minVoteAverage != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("voteAverage"), minVoteAverage));
        }

        if (maxVoteAverage != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("voteAverage"), maxVoteAverage));
        }

        if (proiezioneAfter != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Object, Object> proiezioni = root.join("proiezioneList");
                return criteriaBuilder.greaterThanOrEqualTo(proiezioni.get("data"), proiezioneAfter);
            });
        }

        if (proiezioneBefore != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Object, Object> proiezioni = root.join("proiezioneList");
                return criteriaBuilder.lessThanOrEqualTo(proiezioni.get("data"), proiezioneBefore);
            });
        }

        return this.filmRepository.findAll(spec, pageable);
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
        film.setDataUscita(body.data_uscita());
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

    public List<Film> filmByTMDB() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=it-IT&page=2&primary_release_year=2024&sort_by=popularity.desc")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + authTmdb)
                .build();

        List<Film> films = new ArrayList<>();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(responseBody).get("results");

                if (rootNode != null && rootNode.isArray()) {
                    for (JsonNode movieNode : rootNode) {
                        String movieId = movieNode.get("id").asText();

                        Request detailRequest = new Request.Builder()
                                .url("https://api.themoviedb.org/3/movie/" + movieId + "?language=it-IT")
                                .get()
                                .addHeader("accept", "application/json")
                                .addHeader("Authorization", "Bearer " + authTmdb)
                                .build();

                        Response detailResponse = client.newCall(detailRequest).execute();
                        if (detailResponse.isSuccessful() && detailResponse.body() != null) {
                            String detailResponseBody = detailResponse.body().string();
                            JsonNode detailNode = mapper.readTree(detailResponseBody);

                            Film film = new Film();
                            film.setTitolo(detailNode.get("title").asText());
                            film.setDescrizione(detailNode.get("overview").asText());
                            film.setDataUscita(LocalDate.parse(detailNode.get("release_date").asText()));
                            film.setDurata(detailNode.get("runtime").asInt());
                            film.setVoteAverage(detailNode.get("vote_average").asDouble());
                            film.setPoster_url("https://image.tmdb.org/t/p/w500" + detailNode.get("poster_path").asText());

                            if (detailNode.get("backdrop_path") != null) {
                                film.setBackdrop_url("https://image.tmdb.org/t/p/w1280" + detailNode.get("backdrop_path").asText());
                            }

                            if (detailNode.get("video").asText().isEmpty()) {
                                film.setTrailer_url("https://www.youtube.com/watch?v=" + detailNode.get("video").asText());
                            }
                            film.setGenere(GenereFilm.valueOf(detailNode.get("genres").get(0).get("name").asText().toUpperCase()));

                            Utente utente = this.utenteService.findById(UUID.fromString("7e5aa853-a555-4396-b773-73a2f94d991d"));
                            film.setAdmin(utente);
                            films.add(film);

                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.filmRepository.saveAll(films);

        return films;
    }


    public String getCinemaNews() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://newsapi.org/v2/everything?q=cinema&language=it&apiKey=" + newsApi + "&pageSize=30&sortBy=publishedAt")
                .get()
                .addHeader("accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {

                String errorBody = response.body() != null ? response.body().string() : "No error message";
                return "{\"error\": \"Unable to fetch news\", \"details\": \"" + errorBody + "\"}";
            }
        } catch (IOException e) {

            e.printStackTrace();
            return "{\"error\": \"Internal server error\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

}
