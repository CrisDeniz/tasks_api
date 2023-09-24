package pessoal.tasks.api.domain.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pessoal.tasks.api.domain.tarefa.*;
import pessoal.tasks.api.domain.tarefa.*;
import pessoal.tasks.api.infra.exceptions.TarefaNaoEncontradaException;
import pessoal.tasks.api.infra.security.TokenService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("tarefas")
@SecurityRequirement(name = "bearer-key")
public class TarefaController {

    @Autowired
    private TokenService service;

    @Autowired
    private TarefaRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoTarefa> criarTarefa(@RequestHeader(name = "Authorization") String token, @RequestBody @Valid DadosCriacaoTarefa dados, UriComponentsBuilder builder) {
        var tarefa = repository.save(new Tarefa(dados, service.getSubject(token)));

        var uri = builder.path("/tarefas/{id}").buildAndExpand(tarefa.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoTarefa(tarefa));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTarefa>> exibirTarefas(@RequestHeader(name = "Authorization") String token, @PageableDefault Pageable pagina) {
        var tarefas = repository.findAllByEmail(service.getSubject(token), pagina).map(DadosListagemTarefa::new);

        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTarefa> detalharTarefa(@RequestHeader(name = "Authorization") String token, @PathVariable Long id) {
        var email = service.getSubject(token);
        var tarefa = repository.getReferenceById(id);

        if(tarefa == null) {
            return ResponseEntity.notFound().build();
        }

        if (!Objects.equals(email, tarefa.getEmail())) {
            throw new TarefaNaoEncontradaException();
        }

        return ResponseEntity.ok(new DadosDetalhamentoTarefa(tarefa));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoTarefa> atualizarTarefa (@RequestHeader(name = "Authorization") String token,@RequestBody @Valid DadosAtualizacaoTarefa dados) {
        var email = service.getSubject(token);
        var tarefa = repository.getReferenceById(dados.id());

        if (!Objects.equals(email, tarefa.getEmail())) {
            throw new TarefaNaoEncontradaException();
        }

        tarefa.atualizar(dados);

        return ResponseEntity.ok(new DadosDetalhamentoTarefa(tarefa));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Tarefa> deletarTarefa(@RequestHeader(name = "Authorization") String token,@PathVariable Long id) {
        var email = service.getSubject(token);
        var tarefa = repository.getReferenceById(id);

        if(tarefa == null) {
            return ResponseEntity.notFound().build();
        }

        if (!Objects.equals(email, tarefa.getEmail())) {
            throw new TarefaNaoEncontradaException();
        }

        repository.deleteById(tarefa.getId());

        return ResponseEntity.noContent().build();
    }

}
