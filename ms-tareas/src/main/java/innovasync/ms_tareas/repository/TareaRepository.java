package innovasync.ms_tareas.repository;

import innovasync.ms_tareas.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository indica que esta interfaz maneja la base de datos
@Repository
// JpaRepository nos da métodos como save(), findAll(), findById(), delete() gratis
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // No necesitas escribir nada más, JPA lo hace automáticamente
}
