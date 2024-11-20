package enzocesarano.GalaxyNema.Repositories;

import enzocesarano.GalaxyNema.Entities.Proiezione;
import enzocesarano.GalaxyNema.Entities.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProiezioneRepository extends JpaRepository<Proiezione, UUID> {
    List<Proiezione> findBySala(Sala sala);
}
