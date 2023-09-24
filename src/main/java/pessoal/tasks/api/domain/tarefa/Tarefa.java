package pessoal.tasks.api.domain.tarefa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "Tarefas")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Tarefa {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String email;

    private String descricao;

    private LocalDateTime vencimento;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;


    public Tarefa(DadosCriacaoTarefa dados, String email) {
        this.titulo = dados.titulo();
        this.email = email;
        this.descricao = dados.descricao();
        this.vencimento = dados.vencimento();
        this.prioridade = dados.prioridade();
    }

    public void atualizar(DadosAtualizacaoTarefa dados) {
        if (dados.titulo() != null) {
            this.titulo = dados.titulo();
        }
        if (dados.descricao() != null) {
            this.descricao = dados.descricao();
        }
        if (dados.vencimento() != null) {
            this.vencimento = dados.vencimento();
        }
        if (dados.prioridade() != null) {
            this.prioridade = dados.prioridade();
        }
    }
}
