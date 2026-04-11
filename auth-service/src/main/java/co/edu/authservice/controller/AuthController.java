package co.edu.authservice.controller;

import co.edu.authservice.dto.LoginRequest;
import co.edu.authservice.dto.LoginResponse;
import co.edu.authservice.dto.TokenValidationResponse;
import co.edu.authservice.model.Usuario;
import co.edu.authservice.repository.UsuarioRepository;
import co.edu.authservice.util.JwtUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername()).orElse(null);
        if (usuario == null || !passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtUtils.generateToken(usuario.getUsername(), usuario.getRole());
        return ResponseEntity.ok(new LoginResponse(token, usuario.getUsername(), usuario.getRole()));
    }

    @GetMapping("/validate")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TokenValidationResponse> validate(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            var claims = jwtUtils.parseToken(token);
            return ResponseEntity.ok(new TokenValidationResponse(true, claims.getSubject(), claims.get("role", String.class)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponse(false, null, null));
        }
    }
}
