package pessoal.tasks.api.domain.tarefa;

import java.time.LocalDateTime;

public record DadosListagemTarefa(Long id, String titulo,String email, String descricao, LocalDateTime vencimento, Prioridade prioridade) {

    public DadosListagemTarefa(Tarefa tarefa) {
        this(tarefa.getId(), tarefa.getTitulo(), tarefa.getEmail(), tarefa.getDescricao(), tarefa.getVencimento(), tarefa.getPrioridade());
    }

}
