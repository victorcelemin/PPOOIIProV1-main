package lab02.demo.Services;
import lab02.demo.Repositoies.*;
import lab02.demo.Entities.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public List<Documento> listarTodos() {
        return documentoRepository.findAll();
    }

    public Documento guardar(Documento documento) {
        return documentoRepository.save(documento);
    }

    public Documento buscarPorId(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento paramétrico no encontrado"));
    }

    public void eliminar(Long id) {
        documentoRepository.deleteById(id);
    }
    
    // Aquí iría el método PUT para actualizar
}