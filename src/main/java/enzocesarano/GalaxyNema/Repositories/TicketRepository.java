package enzocesarano.GalaxyNema.Repositories;

import enzocesarano.GalaxyNema.Entities.Ticket;
import enzocesarano.GalaxyNema.Entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByUtente(Utente utente);
}
