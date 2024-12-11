package enzocesarano.GalaxyNema.Services;

import enzocesarano.GalaxyNema.Entities.Enums.RoleUtente;
import enzocesarano.GalaxyNema.Entities.Film;
import enzocesarano.GalaxyNema.Entities.Proiezione;
import enzocesarano.GalaxyNema.Entities.Sala;
import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Exceptions.BadRequestException;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Exceptions.UnauthorizedException;
import enzocesarano.GalaxyNema.Repositories.ProiezioneRepository;
import enzocesarano.GalaxyNema.dto.ProiezioneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ProiezioneService {

    @Autowired
    private ProiezioneRepository proiezioneRepository;

    @Autowired
    private SalaService salaService;

    @Autowired
    private FilmService filmService;

    public Proiezione findById(UUID id_proiezione) {
        return this.proiezioneRepository.findById(id_proiezione).orElseThrow(() -> new NotFoundException("La proiezione con id: " + id_proiezione + " non è stata trovata!"));
    }

    public Page<Proiezione> findAll(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.proiezioneRepository.findAll(pageable);
    }

    public List<Proiezione> findProiezioniBySala(UUID id_sala) {
        Sala sala = this.salaService.findById(id_sala);
        List<Proiezione> proiezioni = proiezioneRepository.findBySala(sala);

        if (proiezioni.isEmpty()) {
            throw new NotFoundException("Nessuna proiezione trovata per la sala specificata.");
        }

        return proiezioni;
    }

    public Proiezione saveProiezione(ProiezioneDTO body, UUID id_sala, UUID id_film, Utente currentAuthenticatedUtente) {

        if (currentAuthenticatedUtente != null) {
            if (currentAuthenticatedUtente.getRole() != RoleUtente.ADMIN) {
                throw new UnauthorizedException("Non hai i permessi per salvare questa proiezione!");
            }
        }
        Sala sala = this.salaService.findById(id_sala);
        List<Proiezione> proiezioniEsistenti = this.proiezioneRepository.findBySala(sala);

        Film film = this.filmService.findById(id_film);

        LocalDate dataProiezione = body.dataProiezione();
        LocalTime oraInizioTime = LocalTime.parse(body.oraInizio(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime oraInizio = dataProiezione.atTime(oraInizioTime);
        LocalDateTime oraFine = oraInizio.plusMinutes(film.getDurata() + 30);

        oraInizio = dataProiezione.atTime(oraInizio.getHour(), oraInizio.getMinute());
        oraFine = dataProiezione.atTime(oraFine.getHour(), oraFine.getMinute());

        for (Proiezione proiezione : proiezioniEsistenti) {
            if (oraInizio.isBefore(proiezione.getOraFine()) && oraFine.isAfter(proiezione.getOraFine())) {
                throw new BadRequestException("La proiezione si sovrappone con un'altra proiezione esistente.");
            }
        }

        Proiezione newProiezione = new Proiezione();
        newProiezione.setDataProiezione(dataProiezione);
        newProiezione.setOraInizio(oraInizio);
        newProiezione.setOraFine(oraFine);
        newProiezione.setMoltiplicatore_prezzo(body.moltiplicatore_prezzo());
        newProiezione.setSala(sala);
        newProiezione.setFilm(film);

        return this.proiezioneRepository.save(newProiezione);
    }


    public Proiezione findByIdAndUpdate(UUID id_proiezione, ProiezioneDTO body, UUID id_sala, UUID id_film, Utente currentAuthenticatedUtente) {

        if (currentAuthenticatedUtente != null) {
            if (currentAuthenticatedUtente.getRole() != RoleUtente.ADMIN) {
                throw new UnauthorizedException("Non hai i permessi per salvare questa proiezione!");
            }
        }

        Sala sala = this.salaService.findById(id_sala);
        Proiezione proiezioneEsistente = this.findById(id_proiezione);

        List<Proiezione> proiezioniEsistenti = this.proiezioneRepository.findBySala(sala);

        Film film = this.filmService.findById(id_film);

        LocalDate dataProiezione = body.dataProiezione();
        LocalTime oraInizioTime = LocalTime.parse(body.oraInizio(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime oraInizio = dataProiezione.atTime(oraInizioTime);
        LocalDateTime oraFine = oraInizio.plusMinutes(film.getDurata() + 30);

        oraInizio = dataProiezione.atTime(oraInizio.getHour(), oraInizio.getMinute());
        oraFine = dataProiezione.atTime(oraFine.getHour(), oraFine.getMinute());

        if (!oraInizio.equals(proiezioneEsistente.getOraInizio()) || !oraFine.equals(proiezioneEsistente.getOraFine())) {
            for (Proiezione proiezione : proiezioniEsistenti) {
                if (oraInizio.isBefore(proiezione.getOraFine()) && oraFine.isAfter(proiezione.getOraInizio())) {
                    throw new BadRequestException("La proiezione si sovrappone con un'altra proiezione esistente.");
                }
            }
        }

        proiezioneEsistente.setDataProiezione(dataProiezione);
        proiezioneEsistente.setOraInizio(oraInizio);
        proiezioneEsistente.setOraFine(oraFine);
        proiezioneEsistente.setMoltiplicatore_prezzo(body.moltiplicatore_prezzo());
        proiezioneEsistente.setFilm(film);
        proiezioneEsistente.setSala(sala);

        return this.proiezioneRepository.save(proiezioneEsistente);
    }

    public void findByIdAndDelete(UUID id_proiezione, Utente currentAuthenticatedUtente) {
        if (currentAuthenticatedUtente != null) {
            if (currentAuthenticatedUtente.getRole() != RoleUtente.ADMIN) {
                throw new UnauthorizedException("Non hai i permessi per salvare questa proiezione!");
            }
        }
        Proiezione proiezione = this.findById(id_proiezione);
        this.proiezioneRepository.delete(proiezione);
    }
}
