package enzocesarano.GalaxyNema.Controllers;

import enzocesarano.GalaxyNema.Entities.Sala;
import enzocesarano.GalaxyNema.Services.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/sale")
public class SalaController {
    @Autowired
    private SalaService salaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Sala> getAllSale(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "nome") String sortBy) {
        return this.salaService.findAll(page, size, sortBy);
    }

    @GetMapping("/sale/{id_sala}")
    @ResponseStatus(HttpStatus.OK)
    public Sala getById(
            @PathVariable("id_sala") UUID id_sala) {
        return this.salaService.findById(id_sala);
    }


}
