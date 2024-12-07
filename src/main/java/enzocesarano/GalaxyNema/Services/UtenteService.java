package enzocesarano.GalaxyNema.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import enzocesarano.GalaxyNema.Entities.Enums.RoleUtente;
import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Exceptions.BadRequestException;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Repositories.UtenteRepository;
import enzocesarano.GalaxyNema.dto.UtenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder bcryptencoder;
    @Autowired
    private Cloudinary cloudinaryUploader;


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

        this.utenteRepository.findByEmail(body.email()).ifPresent(utente -> {
                    throw new BadRequestException("La mail " + body.email() + " è già in uso.");
                }
        );

        Utente newUtente = new Utente(
                body.nome(),
                body.cognome(),
                body.username(),
                body.email(),
                bcryptencoder.encode(body.password()),
                body.telefono(),
                body.data_nascita()
        );

        newUtente.setAvatar("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());

        return this.utenteRepository.save(newUtente);
    }

    public Utente findByIdAndUpdate(UUID userId, UtenteDTO body) {
        Utente userFound = this.findById(userId);

        if (!userFound.getEmail().equals(body.email())) {
            this.utenteRepository.findByEmail(body.email()).ifPresent(
                    user -> {
                        throw new BadRequestException("Email " + body.email() + " già in uso!");
                    }
            );
        }

        if (!userFound.getUsername().equals(body.username())) {
            this.utenteRepository.findByUsername(body.username()).ifPresent(
                    user -> {
                        throw new BadRequestException("Username " + body.username() + " già in uso!");
                    }
            );
        }

        userFound.setNome(body.nome());
        userFound.setCognome(body.cognome());
        userFound.setEmail(body.email());
        userFound.setUsername(body.username());
        userFound.setTelefono(body.telefono());
        if (userFound.getAvatar().contains("https://ui-avatars.com/api/?name=")) {
            userFound.setAvatar("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        }

        if (body.password() != null && !body.password().isEmpty()) {
            userFound.setPassword(bcryptencoder.encode(body.password()));
        }

        return this.utenteRepository.save(userFound);
    }


    public void findByIdAndDelete(UUID userId) {
        Utente userFound = this.findById(userId);
        this.utenteRepository.delete(userFound);
    }


    public String uploadAvatar(MultipartFile file, Utente currentAuthenticatedUtente) {

        if (file.isEmpty()) {
            throw new BadRequestException("Il file dell'immagine non può essere vuoto");
        }

        String url = null;
        try {
            url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        } catch (IOException e) {
            throw new BadRequestException("Errore nel caricamento dell'immagine");
        }

        Utente utenteFound = this.findById(currentAuthenticatedUtente.getId_utente());
        if (utenteFound == null) {
            throw new BadRequestException("Utente non trovato con l'ID fornito");
        }

        utenteFound.setAvatar(url);
        this.utenteRepository.save(utenteFound);
        return url;
    }

    public Utente cambiaRole(UUID id_utente) {
        Utente utente = this.findById(id_utente);
        if (utente.getRole() == RoleUtente.USER) {
            utente.setRole(RoleUtente.ADMIN);
        }
        return this.utenteRepository.save(utente);
    }
}
