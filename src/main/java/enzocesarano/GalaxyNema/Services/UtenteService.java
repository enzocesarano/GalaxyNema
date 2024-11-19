package enzocesarano.GalaxyNema.Services;

import com.cloudinary.Cloudinary;
import enzocesarano.GalaxyNema.Entities.Ticket;
import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Exceptions.BadRequestException;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Repositories.TicketRepository;
import enzocesarano.GalaxyNema.Repositories.UtenteRepository;
import enzocesarano.GalaxyNema.dto.UtenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder bcryptencoder;
    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private TicketRepository ticketRepository;


    public Utente findByUsername(String username) {
        return this.utenteRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("L'utente " + username + " non è stato trovato!"));
    }

    public Page<Utente> findAll(int page, int size, String sortBy) {
        if (size > 15) size = 15;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.utenteRepository.findAll(pageable);
    }

    public Utente findById(UUID userId) {
        return this.utenteRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public Utente saveUtente(UtenteDTO body) {
        this.utenteRepository.findByUsername(body.username()).ifPresent(utente -> {
                    throw new BadRequestException("L'username " + body.username() + " è già in uso.");
                }
        );
        Utente newUtente = new Utente(body.nome(), body.cognome(), body.username(), body.email(), bcryptencoder.encode(body.password()), body.telefono(), body.data_nascita());
        newUtente.setAvatar("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        return this.utenteRepository.save(newUtente);
    }

    public List<Ticket> ticketListByUtente(Utente currentAuthenticatedUser) {
        return ticketRepository.findByUtente(currentAuthenticatedUser);
    }
}
