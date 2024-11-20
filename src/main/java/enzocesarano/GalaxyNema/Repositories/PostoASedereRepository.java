package enzocesarano.GalaxyNema.Repositories;

import enzocesarano.GalaxyNema.Entities.PostoASedere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostoASedereRepository extends JpaRepository<PostoASedere, UUID> {
}
