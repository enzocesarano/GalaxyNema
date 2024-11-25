package enzocesarano.GalaxyNema.Repositories;

import enzocesarano.GalaxyNema.Entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilmRepository extends JpaRepository<Film, UUID>, JpaSpecificationExecutor<Film> {
    Optional<Film> findByTitolo(String titolo);

}
