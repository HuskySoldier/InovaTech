package innovasync.ms_tareas.repository;
import innovasync.ms_tareas.model.HistorialTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialTareaRepository extends JpaRepository<HistorialTarea, Long> {

    List<HistorialTarea> findByTareaId(Long tareaId);

}
