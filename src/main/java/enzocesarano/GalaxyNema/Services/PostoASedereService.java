package enzocesarano.GalaxyNema.Services;

import enzocesarano.GalaxyNema.Entities.PostoASedere;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Repositories.PostoASedereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostoASedereService {
    @Autowired
    private PostoASedereRepository postoASedereRepository;

    @Autowired
    private ProiezioneService proiezioneService;

    public PostoASedere findById(UUID id_postoASedere) {
        return this.postoASedereRepository.findById(id_postoASedere).orElseThrow(() -> new NotFoundException("Posto a sedere non trovato"));
    }

}
