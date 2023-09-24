package pessoal.tasks.api.domain.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pessoal.tasks.api.domain.tarefa.*;
import pessoal.tasks.api.domain.usuario.DadosCadastroUsuario;
import pessoal.tasks.api.domain.usuario.Usuario;
import pessoal.tasks.api.infra.security.TokenService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TarefaControllerTest {

    @Autowired
    private TokenService service;
    @MockBean
    private TarefaRepository repository;

    @Autowired
    private JacksonTester<DadosCriacaoTarefa> dadosCriacaoTarefaJson;

    @Autowired
    private JacksonTester<DadosAtualizacaoTarefa> dadosAtualizacaoTarefaJson;

    @Autowired
    private MockMvc mock;

    @Test
    @WithMockUser
    @DisplayName("Devera devolver código http 400 caso os dados sejam inválidos")
    void criarTarefaCaso1() throws Exception {
            var response = mock.perform(post("/tarefas"))
                    .andReturn().getResponse();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("Devera devolver código http 201 caso os dados sejam validos")
    @WithMockUser
    void criarTarefaCaso2() throws Exception {

        var data = LocalDateTime.now().plusHours(1);

        var token = service.criarToken(new Usuario(null, "Carlos Andre","carlos.andre@gmail.com", "123456"));

        var tarefa = new Tarefa(null, "Qualquer","qual.quer@gmail.com", "Qualquer",data, Prioridade.BAIXA);

        when(repository.save(any())).thenReturn(tarefa);

        var response = mock.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( dadosCriacaoTarefaJson.write(
                                new DadosCriacaoTarefa(tarefa)
                        ).getJson()
                        )
                        .header("Authorization", token)
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

    }

    @Test
    @WithMockUser
    @DisplayName("Devera devolver código http 400 caso a requisição seja feita de maneira errada")
    void exibirTarefasCaso1() throws Exception {
        var response = mock.perform(get("/tarefas"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Devera devolver código http 200 caso a requisição seja feita de maneira correta")
    void exibirTarefasCaso2() throws Exception {

        var usuario = new Usuario(null, "Carlos Andre","carlos.andre@gmail.com", "123456");

        var token = service.criarToken(usuario);

        PageRequest paginacao = PageRequest.of(1, 10);
        List<Tarefa> centrosDeCustos = new ArrayList<>();
        Page<Tarefa> tarefaPage = new PageImpl<>(centrosDeCustos, paginacao, 0);

        when(repository.findAllByEmail(usuario.getEmail(), paginacao)).thenReturn(tarefaPage);

        var response = mock.perform(get("/tarefas?page=1&size=10")
                        .header("Authorization", token)
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());;
    }

    @Test
    @DisplayName("Deve retornar o código http 404 caso a tarefa não seja encontrada")
    @WithMockUser
    void detalharTarefaCaso1() throws Exception {
        var token = service.criarToken(new Usuario(null, "Carlos Andre","carlos.andre@gmail.com", "123456"));

        when(repository.findById(1L)).thenReturn(Optional.empty());
        mock.perform(MockMvcRequestBuilders.get("/tarefas/{id}", 1L)
                .header("Authorization", token)
        ).andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve retornar o código http 200 caso a tarefa seja encontrada")
    @WithMockUser
    void detalharTarefaCaso2() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var tarefa = new Tarefa(null, "Qualquer","carlos.andre@gmail.com", "Qualquer",data, Prioridade.BAIXA);

        var token = service.criarToken(new Usuario(null, "Carlos Andre","carlos.andre@gmail.com", "123456"));


        when(repository.getReferenceById(1L)).thenReturn(tarefa);

        var response = mock.perform(MockMvcRequestBuilders.get("/tarefas/{id}", 1L)
                        .header("Authorization", token))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    void atualizarTarefaCaso1() throws Exception {
        var data = LocalDateTime.now().plusHours(1);

        var token = service.criarToken(new Usuario(null, "Carlos Andre","carlos.andre@gmail.com", "123456"));

        var tarefa = new Tarefa(1L, "Qualquer","carlos.andre@gmail.com", "Qualquer",data, Prioridade.BAIXA);

        when(repository.getReferenceById(1L)).thenReturn(tarefa);

        var response = mock.perform(put("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( dadosAtualizacaoTarefaJson.write(
                                        new DadosAtualizacaoTarefa(1L, "","" ,data,Prioridade.MEDIANA)
                                ).getJson()
                        )
                        .header("Authorization", token)
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    void atualizarTarefaCaso2() throws Exception {
        var response = mock.perform(put("/tarefas"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    void deletarTarefa() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var tarefa = new Tarefa(1L, "Qualquer","carlos.andre@gmail.com", "Qualquer",data, Prioridade.BAIXA);


        var token = service.criarToken(new Usuario(null, "Carlos Andre","carlos.andre@gmail.com", "123456"));

        when(repository.getReferenceById(1L)).thenReturn(tarefa);
        mock.perform(MockMvcRequestBuilders.delete("/tarefas/{id}", 1L).header("Authorization", token)).andExpect(status().isNoContent());

    }
}