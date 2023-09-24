package pessoal.tasks.api.domain.tarefa;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAtualizacaoTarefa(

        @NotNull
        Long id,

        String titulo,

        String descricao,

        @Future
        LocalDateTime vencimento,

        Prioridade prioridade

) {
}
