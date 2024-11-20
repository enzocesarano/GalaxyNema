package enzocesarano.GalaxyNema.Repositories;

import enzocesarano.GalaxyNema.Entities.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalaRepository extends JpaRepository<Sala, UUID> {
    Optional<Sala> findByNome(String nome);
}
