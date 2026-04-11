package co.edu.authservice.service;

import co.edu.authservice.dto.LoginRequest;
import co.edu.authservice.dto.LoginResponse;
import co.edu.authservice.dto.TokenValidationResponse;
import co.edu.authservice.model.Usuario;
import co.edu.authservice.repository.UsuarioRepository;
import co.edu.authservice.util.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String token = jwtUtils.generateToken(usuario.getUsername(), usuario.getRole());
        return new LoginResponse(token, usuario.getUsername(), usuario.getRole());
    }

    @Override
    public TokenValidationResponse validate(String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            var claims = jwtUtils.parseToken(token);
            return new TokenValidationResponse(true, claims.getSubject(), claims.get("role", String.class));
        } catch (Exception ex) {
            return new TokenValidationResponse(false, null, null);
        }
    }
}
