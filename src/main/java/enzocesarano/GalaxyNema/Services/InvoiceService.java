package enzocesarano.GalaxyNema.Services;

import enzocesarano.GalaxyNema.Entities.Enums.Fila;
import enzocesarano.GalaxyNema.Entities.*;
import enzocesarano.GalaxyNema.Exceptions.BadRequestException;
import enzocesarano.GalaxyNema.Exceptions.NotFoundException;
import enzocesarano.GalaxyNema.Repositories.InvoiceRepository;
import enzocesarano.GalaxyNema.dto.InvoiceDTO;
import enzocesarano.GalaxyNema.dto.TicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProiezioneService proiezioneService;

    @Autowired
    private UtenteService utenteService;

    public Invoice findById(UUID id_invoice) {
        return this.invoiceRepository.findById(id_invoice).orElseThrow(() -> new NotFoundException(id_invoice));
    }

    public void findByIdAndDelete(UUID id_invoice) {
        Invoice invoice = this.findById(id_invoice);
        this.invoiceRepository.delete(invoice);
    }

    public Invoice saveInvoice(InvoiceDTO payload, Utente currentAuthenticatedUtente, UUID id_proiezione) {
        Proiezione proiezione1 = this.proiezioneService.findById(id_proiezione);

        if (payload.ticket() == null || payload.ticket().isEmpty()) {
            throw new BadRequestException("Il payload non contiene biglietti validi.");
        }

        Invoice invoice = new Invoice(
                payload.via(),
                payload.civico(),
                payload.cap(),
                payload.comune(),
                payload.provincia()
        );
        invoice.setUtente(currentAuthenticatedUtente);

        double importoTotale = 0.0;
        List<Ticket> ticketList = new ArrayList<>();

        List<Ticket> proiezioneTickets = proiezione1.getTicketList();
        if (proiezioneTickets == null) {
            proiezioneTickets = new ArrayList<>();
        }

        for (TicketDTO ticketDTO : payload.ticket()) {
            PostoASedere postoASedere = new PostoASedere();

            boolean postoOccupato = proiezioneTickets.stream()
                    .anyMatch(t -> t.getPostoASedere().getFila() == ticketDTO.postoASedere().fila()
                            && t.getPostoASedere().getNumeroPosto() == ticketDTO.postoASedere().numeroPosto());

            if (postoOccupato) {
                throw new BadRequestException("Il posto selezionato è già occupato.");
            }

            postoASedere.setFila(ticketDTO.postoASedere().fila());
            postoASedere.setNumeroPosto(ticketDTO.postoASedere().numeroPosto());

            Ticket ticket = new Ticket();
            if (ticketDTO.postoASedere().fila() == Fila.D) {
                postoASedere.setPremium(true);
                ticket.setPrezzo((postoASedere.getPrezzo_base() + 3.00) * proiezione1.getMoltiplicatore_prezzo());
            } else {
                ticket.setPrezzo(postoASedere.getPrezzo_base() * proiezione1.getMoltiplicatore_prezzo());
            }

            try {
                ticket.setData_nascita(LocalDate.parse(ticketDTO.data_nascita()));
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Formato della data di nascita non valido: " + ticketDTO.data_nascita());
            }

            ticket.setNome(ticketDTO.nome());
            ticket.setCognome(ticketDTO.cognome());
            ticket.setPostoASedere(postoASedere);
            ticket.setInvoice(invoice);
            ticket.setProiezione(proiezione1);

            ticketList.add(ticket);
            importoTotale += ticket.getPrezzo();
        }

        invoice.setTicket(ticketList);
        invoice.setImporto(importoTotale);

        // Salva l'invoice
        return this.invoiceRepository.save(invoice);
    }

    public List<Invoice> getInvoicesByUtente(Utente utenteCorrente) {
        Utente utente = this.utenteService.findById(utenteCorrente.getId_utente());
        List<Invoice> invoices = utente.getInvoice();

        if (invoices.isEmpty()) {
            throw new RuntimeException("Nessun ticket trovato per l'utente specificato");
        }

        return invoices;
    }

}
