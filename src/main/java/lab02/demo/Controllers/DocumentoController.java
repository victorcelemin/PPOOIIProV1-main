package lab02.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lab02.demo.Entities.Documento;
import lab02.demo.Services.DocumentoService;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @GetMapping
    public List<Documento> listar() {
        return documentoService.listarTodos();
    }

    @PostMapping
    public Documento crear(@RequestBody Documento documento) {
        return documentoService.guardar(documento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Documento> actualizar(@PathVariable Long id, @RequestBody Documento documento) {
        Documento docExistente = documentoService.buscarPorId(id);
        documento.setId(docExistente.getId());
        return ResponseEntity.ok(documentoService.guardar(documento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        documentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}