package enzocesarano.GalaxyNema.Services;

import enzocesarano.GalaxyNema.Entities.Sala;
import enzocesarano.GalaxyNema.Exceptions.BadRequestException;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Repositories.SalaRepository;
import enzocesarano.GalaxyNema.dto.SalaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public Sala findById(UUID id_sala) {
        return this.salaRepository.findById(id_sala).orElseThrow(() -> new NotFoundException("La sala con id: " + id_sala + " non è stata trovata!"));
    }

    public Page<Sala> findAll(int page, int size, String sortBy) {
        if (size > 5) size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.salaRepository.findAll(pageable);
    }

    public Sala findByNome(String nome) {
        return this.salaRepository.findByNome(nome).orElseThrow(() -> new NotFoundException("La sala " + nome + " non è stata trovata!"));
    }

    public Sala saveSala(SalaDTO body) {
        this.salaRepository.findByNome(body.nome()).ifPresent(sala -> {
                    throw new BadRequestException("La sala " + body.nome() + " è già presente.");
                }
        );

        Sala newSala = new Sala(body.nome());
        return this.salaRepository.save(newSala);
    }

    public Sala findByIdAndUpdate(UUID id_sala, SalaDTO body) {
        Sala sala = this.findById(id_sala);

        this.salaRepository.findByNome(body.nome()).ifPresent(sala1 -> {
                    throw new BadRequestException("La sala " + body.nome() + " è già presente.");
                }
        );

        sala.setNome(body.nome());
        return this.salaRepository.save(sala);
    }

    public void findByIdAndDelete(UUID id_sala) {
        Sala sala = this.findById(id_sala);
        this.salaRepository.delete(sala);
    }

}
