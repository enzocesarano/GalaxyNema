package enzocesarano.GalaxyNema.Services;

import enzocesarano.GalaxyNema.Entities.Ticket;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Repositories.InvoiceRepository;
import enzocesarano.GalaxyNema.Repositories.PostoASedereRepository;
import enzocesarano.GalaxyNema.Repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PostoASedereService postoASedereService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private PostoASedereRepository postoASedereRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProiezioneService proiezioneService;


    public Ticket findById(UUID id_ticket) {
        return this.ticketRepository.findById(id_ticket).orElseThrow(() -> new NotFoundException("Il ticket con id: " + id_ticket + " non è stato trovato!"));
    }

    /*public Ticket saveTicket(UUID id_proiezione, Utente utente, TicketDTO body) {
        Proiezione proiezione1 = this.proiezioneService.findById(id_proiezione);

        Ticket ticket = new Ticket();

        boolean postoOccupato = proiezione1.getTicketList().stream()
                .anyMatch(t -> t.getPostoASedere().getFila() == body.postoASedere().fila()
                        && t.getPostoASedere().getNumeroPosto() == body.postoASedere().numeroPosto());

        if (postoOccupato) {
            throw new BadRequestException("Il posto selezionato è già occupato.");
        }

        PostoASedere newPosto = new PostoASedere();
        newPosto.setFila(body.postoASedere().fila());
        newPosto.setNumeroPosto(body.postoASedere().numeroPosto());

        if (body.postoASedere().fila() == Fila.D) {
            newPosto.setPremium(true);
            ticket.setPrezzo((newPosto.getPrezzo_base() + 3.00) * proiezione1.getMoltiplicatore_prezzo());
        } else {
            ticket.setPrezzo(newPosto.getPrezzo_base() * proiezione1.getMoltiplicatore_prezzo());
        }

        newPosto.setTicket(ticket);

        ticket.setProiezione(proiezione1);
        ticket.setPostoASedere(newPosto);
        Invoice newInvoice = new Invoice(body.invoice().via(), body.invoice().civico(), body.invoice().cap(), body.invoice().comune(), body.invoice().provincia());
        ticket.setInvoice(newInvoice);
        newInvoice.getTicket().add(ticket);

        ticketRepository.save(ticket);

        return ticket;
    }*/


    public void findByIdAndDelete(UUID id_ticket) {
        Ticket ticket = this.findById(id_ticket);
        this.ticketRepository.delete(ticket);
    }
}
