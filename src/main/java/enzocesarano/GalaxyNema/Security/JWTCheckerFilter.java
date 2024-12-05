package enzocesarano.GalaxyNema.Security;

import enzocesarano.GalaxyNema.Entities.Utente;
import enzocesarano.GalaxyNema.Exceptions.UnauthorizedException;
import enzocesarano.GalaxyNema.Services.UtenteService;
import enzocesarano.GalaxyNema.Tools.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {
    @Autowired
    private JWT jwt;
    @Autowired
    private UtenteService utenteService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");

        String accessToken = authHeader.substring(7);
        jwt.verifyToken(accessToken);


        String userId = jwt.getIdFromToken(accessToken);
        Utente userCorrente = this.utenteService.findById(UUID.fromString(userId));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userCorrente, null, userCorrente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath()) ||
                new AntPathMatcher().match("/films/**", request.getServletPath()) ||
                new AntPathMatcher().match("/proiezioni/**", request.getServletPath()) ||
                new AntPathMatcher().match("/api/**", request.getServletPath()) ||
                new AntPathMatcher().match("/webhook**", request.getServletPath());
    }
}

