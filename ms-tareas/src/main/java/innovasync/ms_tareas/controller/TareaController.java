package innovasync.ms_tareas.controller;

import innovasync.ms_tareas.dto.TareaDTO;
import innovasync.ms_tareas.dto.TareaResponseDTO;
import innovasync.ms_tareas.model.Tarea;
import innovasync.ms_tareas.service.TareaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<TareaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(tareaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerPorId(id));
    }

    // Ahora recibe TareaDTO en vez del modelo directo
    @PostMapping
    public ResponseEntity<Tarea> crear(@Valid @RequestBody TareaDTO tareaDTO) {
        return ResponseEntity.ok(tareaService.crear(tareaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable Long id, @Valid @RequestBody TareaDTO tareaDTO) {
        return ResponseEntity.ok(tareaService.actualizar(id, tareaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}