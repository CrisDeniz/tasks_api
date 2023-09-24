package pessoal.tasks.api.domain.tarefa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {


    Page<Tarefa> findAllByEmail(String email, Pageable page);

}
