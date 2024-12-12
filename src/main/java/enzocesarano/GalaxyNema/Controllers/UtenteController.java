package enzocesarano.GalaxyNema.Controllers;


import enzocesarano.GalaxyNema.Entities.*;
import enzocesarano.GalaxyNema.Exceptions.BadRequestException;
import enzocesarano.GalaxyNema.Services.*;
import enzocesarano.GalaxyNema.dto.FilmDTO;
import enzocesarano.GalaxyNema.dto.InvoiceDTO;
import enzocesarano.GalaxyNema.dto.ProiezioneDTO;
import enzocesarano.GalaxyNema.dto.UtenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private ProiezioneService proiezioneService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PreferitoService preferitoService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public Utente getProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    /*@GetMapping("/me/tickets")
    @ResponseStatus(HttpStatus.OK)
    public List<Ticket> ticketListByUtente(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return utenteService.ticketListByUtente(currentAuthenticatedUser);
    }*/

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public Utente updateProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser, @RequestBody @Validated UtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Errore nel payload! " + message);
        }
        return this.utenteService.findByIdAndUpdate(currentAuthenticatedUser.getId_utente(), body);
    }


    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        this.utenteService.findByIdAndDelete(currentAuthenticatedUser.getId_utente());
    }

    @PatchMapping("me/avatar")
    @ResponseStatus(HttpStatus.OK)
    public String updateAvatar(@RequestParam("avatar") MultipartFile file,
                               @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return this.utenteService.uploadAvatar(file, currentAuthenticatedUtente);
    }

    /*@PostMapping("/me/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket saveTicketByAdmin(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                                    @RequestParam UUID id_proiezione,
                                    @RequestBody @Validated TicketDTO body,
                                    BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nei dati forniti!");
        }
        return ticketService.saveTicket(id_proiezione, currentAuthenticatedUtente, body);
    }*/

    @PostMapping("me/invoices")
    public Invoice createInvoice(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                                 @RequestParam UUID id_proiezione,
                                 @RequestBody @Validated InvoiceDTO body,
                                 BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nei dati forniti!");
        }
        return this.invoiceService.saveInvoice(body, currentAuthenticatedUtente, id_proiezione);
    }

    @PostMapping("/me/films")
    @ResponseStatus(HttpStatus.OK)
    public Film saveFilmByAdmin(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                                @RequestBody @Validated FilmDTO body,
                                BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nei dati forniti!");
        }
        return this.filmService.saveFilm(body, currentAuthenticatedUtente);
    }

    @PutMapping("/me/films/{id_film}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Film updateFilm(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                           @PathVariable("id_film") UUID id_film,
                           @RequestBody @Validated FilmDTO body,
                           BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nei dati forniti!");
        }
        return this.filmService.findByIdAndUpdate(currentAuthenticatedUtente, id_film, body);
    }

    @DeleteMapping("/me/films/{id_film}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                           @PathVariable("id_film") UUID id_film) {
        this.filmService.findByIdAndDelete(id_film, currentAuthenticatedUtente);
    }

    @PostMapping("/me/proiezioni")
    @ResponseStatus(HttpStatus.OK)
    public Proiezione saveProiezioneByAdmin(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                                            @RequestBody @Validated ProiezioneDTO body,
                                            BindingResult validationResult,
                                            @RequestParam UUID id_sala,
                                            @RequestParam UUID id_film) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nei dati forniti!");
        }
        return this.proiezioneService.saveProiezione(body, id_sala, id_film, currentAuthenticatedUtente);
    }

    @DeleteMapping("/me/proiezioni/{id_proiezione}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProiezione(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                                 @PathVariable("id_proiezione") UUID id_proiezione) {
        this.proiezioneService.findByIdAndDelete(id_proiezione, currentAuthenticatedUtente);
    }

    @PutMapping("/me/proiezioni/{id_proiezione}")
    @ResponseStatus(HttpStatus.OK)
    public Proiezione updateProiezione(
            @PathVariable("id_proiezione") UUID idProiezione,
            @RequestParam("id_sala") UUID idSala,
            @RequestParam("id_film") UUID idFilm,
            @RequestBody ProiezioneDTO proiezioneDTO,
            BindingResult validationResult,
            @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nei dati forniti!");
        }
        return this.proiezioneService.findByIdAndUpdate(
                idProiezione,
                proiezioneDTO,
                idSala,
                idFilm,
                currentAuthenticatedUtente);
    }

    @GetMapping("me/films/preferiti")
    public List<Film> getPreferiti(@AuthenticationPrincipal Utente utente) {
        return utente.getPreferiti();
    }

    @PostMapping("/me/films/{id_film}/preferiti")
    @ResponseStatus(HttpStatus.OK)
    public Film aggiungiFilmAiPreferiti(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                                        @PathVariable("id_film") UUID id_film) {
        return this.preferitoService.aggiungiPreferito(currentAuthenticatedUtente.getId_utente(), id_film);
    }

    @DeleteMapping("/me/films/{id_film}/preferiti")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rimuoviFilmDaiPreferiti(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                                        @PathVariable("id_film") UUID id_film) {
        this.preferitoService.rimuoviPreferito(currentAuthenticatedUtente.getId_utente(), id_film);
    }

    @GetMapping("/me/invoices")
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> getInvoicesByUtente(@AuthenticationPrincipal Utente utente) {
        return this.invoiceService.getInvoicesByUtente(utente);
    }

    @PutMapping("/tickets/{id_ticket}")
    @ResponseStatus(HttpStatus.OK)
    public Ticket findByIdAndUpdate(@PathVariable("id_ticket") UUID id_ticket) {
        return this.ticketService.findByIdAndUpdate(id_ticket);
    }

    @PutMapping("/{id_utente}")
    @ResponseStatus(HttpStatus.OK)
    public Utente cambiaRole(@PathVariable("id_utente") UUID id_utente) {
        return this.utenteService.cambiaRole(id_utente);
    }
}
