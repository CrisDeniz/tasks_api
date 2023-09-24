package pessoal.tasks.api.domain.tarefa;

import java.time.LocalDateTime;

public record DadosDetalhamentoTarefa(Long id, String titulo, String descricao, LocalDateTime vencimento, Prioridade prioridade) {

    public DadosDetalhamentoTarefa(Tarefa tarefa) {
        this(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getVencimento(), tarefa.getPrioridade());
    }

}
