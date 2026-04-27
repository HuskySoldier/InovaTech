package innovasync.ms_tareas.controller;

import innovasync.ms_tareas.model.HistorialTarea;
import innovasync.ms_tareas.service.HistorialTareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial-tareas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HistorialTareaController {

    private final HistorialTareaService historialTareaService;

    
    @GetMapping("/tarea/{tareaId}")
    public ResponseEntity<List<HistorialTarea>> obtenerPorTarea(@PathVariable Long tareaId) {
        return ResponseEntity.ok(historialTareaService.obtenerPorTarea(tareaId));
    }

    
    @PostMapping("/tarea/{tareaId}")
    public ResponseEntity<HistorialTarea> registrar(
            @PathVariable Long tareaId,
            @RequestBody String descripcion) {
        return ResponseEntity.ok(historialTareaService.registrar(tareaId, descripcion));
    }
}