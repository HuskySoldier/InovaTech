package innovasync.ms_tareas.controller;

import innovasync.ms_tareas.model.Prioridad;
import innovasync.ms_tareas.service.PrioridadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import innovasync.ms_tareas.dto.PrioridadDTO;

@RestController
@RequestMapping("/api/prioridades")
@CrossOrigin(origins = "*")   //cambiar a la URL de tu frontend en producción
@RequiredArgsConstructor
public class PrioridadController {

    private final PrioridadService prioridadService;

    @GetMapping
    public ResponseEntity<List<Prioridad>> obtenerTodas() {
        return ResponseEntity.ok(prioridadService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prioridad> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prioridadService.obtenerPorId(id));
    }

    

    @PostMapping
    public ResponseEntity<Prioridad> crear(@RequestBody PrioridadDTO dto) {
    Prioridad prioridad = new Prioridad();
    prioridad.setNombre(dto.getNombre());
    return ResponseEntity.ok(prioridadService.crear(prioridad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        prioridadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
