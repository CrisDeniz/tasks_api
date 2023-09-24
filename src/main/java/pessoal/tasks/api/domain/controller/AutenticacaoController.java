package pessoal.tasks.api.domain.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pessoal.tasks.api.domain.usuario.DadosAutenticacao;
import pessoal.tasks.api.domain.usuario.Usuario;
import pessoal.tasks.api.infra.security.TokenJWT;
import pessoal.tasks.api.infra.security.TokenService;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private TokenService service;
    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity<TokenJWT> fazerLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var token = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(token);
        var tokenJWT = service.criarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenJWT(tokenJWT));
    }

}
