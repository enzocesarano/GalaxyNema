package enzocesarano.GalaxyNema.Controllers;

import enzocesarano.GalaxyNema.Entities.Proiezione;
import enzocesarano.GalaxyNema.Services.ProiezioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/proiezioni")
public class ProiezioneController {
    @Autowired
    private ProiezioneService proiezioneService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Proiezione> getAllProiezioni(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataProiezione") String sortBy) {
        return this.proiezioneService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id_proiezione}")
    @ResponseStatus(HttpStatus.OK)
    public Proiezione getById(@PathVariable("id_proiezione") UUID id_proiezione) {
        return this.proiezioneService.findById(id_proiezione);
    }


}
