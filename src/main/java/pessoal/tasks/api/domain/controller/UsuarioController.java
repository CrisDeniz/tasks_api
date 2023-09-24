package pessoal.tasks.api.domain.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pessoal.tasks.api.domain.usuario.DadosDetalhamentoUsuario;
import pessoal.tasks.api.domain.usuario.UsuarioRepository;
import pessoal.tasks.api.infra.security.TokenService;

import java.util.Objects;

@RestController
@RequestMapping("usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService service;

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoUsuario> detalhar(@RequestHeader("Authorization")String token, @PathVariable Long id) {
        var email = service.getSubject(token);
        var usuario = repository.getReferenceById(id);

        if (!Objects.equals(usuario.getEmail(), email)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

}
