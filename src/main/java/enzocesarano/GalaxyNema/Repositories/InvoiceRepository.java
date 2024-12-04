package enzocesarano.GalaxyNema.Repositories;

import enzocesarano.GalaxyNema.Entities.Invoice;
import enzocesarano.GalaxyNema.Entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    List<Invoice> findByUtente(Utente utente);
}
