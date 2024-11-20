package enzocesarano.GalaxyNema.Services;

import enzocesarano.GalaxyNema.Entities.Invoice;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice findById(UUID id_invoice) {
        return this.invoiceRepository.findById(id_invoice).orElseThrow(() -> new NotFoundException(id_invoice));
    }

    public void findByIdAndDelete(UUID id_invoice) {
        Invoice invoice = this.findById(id_invoice);
        this.invoiceRepository.delete(invoice);
    }


}
