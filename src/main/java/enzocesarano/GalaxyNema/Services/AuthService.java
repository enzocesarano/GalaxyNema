package enzocesarano.GalaxyNema.Services;

import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Exceptions.UnauthorizedException;
import enzocesarano.GalaxyNema.Tools.JWT;
import enzocesarano.GalaxyNema.dto.UtenteLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UtenteService userService;
    @Autowired
    private JWT jwt;
    @Autowired
    private PasswordEncoder bcryptencoder;

    public String checkCredenzialiAndToken(UtenteLoginDTO body) {
        Utente userFound = this.userService.findByUsername(body.username());
        if (bcryptencoder.matches(body.password(), userFound.getPassword())) {
            String accessToken = jwt.createToken(userFound);
            return accessToken;
        } else {
            throw new UnauthorizedException("Le credenziali inserite sono errate");
        }
    }

}

