package pessoal.tasks.api.domain.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import pessoal.tasks.api.domain.usuario.DadosCadastroUsuario;
import pessoal.tasks.api.domain.usuario.DadosDetalhamentoUsuario;
import pessoal.tasks.api.domain.usuario.Usuario;
import pessoal.tasks.api.domain.usuario.UsuarioRepository;

@RestController()
@RequestMapping("/cadastro")
public class CadastroController {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder builder) {
        var usuario = new Usuario(dados);
        usuario.setSenha(encoder.encode(dados.senha()));
        repository.save(usuario);

        var uri = builder.path("usuario/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }

}
