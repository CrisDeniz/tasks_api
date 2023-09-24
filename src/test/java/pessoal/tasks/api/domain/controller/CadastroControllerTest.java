package pessoal.tasks.api.domain.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pessoal.tasks.api.domain.usuario.DadosCadastroUsuario;
import pessoal.tasks.api.domain.usuario.DadosDetalhamentoUsuario;
import pessoal.tasks.api.domain.usuario.Usuario;
import pessoal.tasks.api.domain.usuario.UsuarioRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CadastroControllerTest {

    @MockBean
    private UsuarioRepository repository;

    @Autowired
    JacksonTester<DadosCadastroUsuario> dadosCadastroUsuarioJson;

    @Autowired
    JacksonTester<DadosDetalhamentoUsuario> dadosDetalhamentoUsuarioJson;

    @Autowired
    private MockMvc mock;


    @Test
    @DisplayName("Devera devolver código http 400 caso os dados sejam inválidos")
    void cadastrar() throws Exception {

        var response = mock.perform(post("/cadastro"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("Devera devolver código http 201 caso os dados sejam validos")
    void cadastrarCaso2() throws Exception {

        var usuario = new Usuario(null, "Carlos Andre","carlos.andre@gmail.com", "123456");

        var usuarioDetalhado = new DadosDetalhamentoUsuario(usuario);

        when(repository.save(any())).thenReturn(usuario);

        var response = mock.perform(post("/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroUsuarioJson.write(
                                new DadosCadastroUsuario(usuario)
                        ).getJson()))
                .andReturn().getResponse();

        var jsonUsuarioDetalhado = dadosDetalhamentoUsuarioJson.write(
                usuarioDetalhado
        ).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        assertThat(response.getContentAsString()).isEqualTo(jsonUsuarioDetalhado);

    }

}