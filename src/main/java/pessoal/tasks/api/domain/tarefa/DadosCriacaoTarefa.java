package pessoal.tasks.api.domain.tarefa;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosCriacaoTarefa(

        @NotBlank
        String titulo,

        @NotBlank
        String descricao,

        @NotNull
        @Future
        LocalDateTime vencimento,

        @NotNull
        Prioridade prioridade

) {


        public DadosCriacaoTarefa(Tarefa tarefa) {
                this(tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getVencimento(), tarefa.getPrioridade());
        }
}
