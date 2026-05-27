package lab02.demo.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab02.demo.Entities.Documento;
import lab02.demo.Entities.Vehiculo;
import lab02.demo.Entities.VehiculoDocumento;
import lab02.demo.Repositoies.DocumentoRepository;
import lab02.demo.Repositoies.VehiculoDocumentoRepository;
import lab02.demo.Repositoies.VehiculoRepository;

@Service
public class VehiculoService {
    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private VehiculoDocumentoRepository vehiculoDocumentoRepository;
    @Transactional
    public Vehiculo crearVconD(Vehiculo vehiculo, List<Long> documentosIds){
        if(documentosIds == null || documentosIds.isEmpty()){
            throw new RuntimeException("No se puede crear un vehiculo sin documentos");
        }
        Vehiculo nuevoVehiculo = vehiculoRepository.save(vehiculo);
        for(Long docId : documentosIds){
            Documento docParametrico = documentoRepository.findById(docId)
            .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
            VehiculoDocumento relacion = new VehiculoDocumento();
            relacion.setVehiculo(nuevoVehiculo);
            relacion.setDocumento(docParametrico);
            relacion.setEstado("En Verificación");
            vehiculoDocumentoRepository.save(relacion);
        }
        return nuevoVehiculo;
    }
    // --- REQUERIMIENTO: BÚSQUEDAS ---

    public Vehiculo buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlacaIgnoreCase(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con placa: " + placa));
    }

    public List<Vehiculo> buscarPorTipo(String tipo) {
        return vehiculoRepository.findByTipo(tipo);
    }

    public List<Vehiculo> buscarPorEstadoDocumento(String estado) {
        // Buscamos en la tabla de relación y extraemos solo los vehículos
        return vehiculoDocumentoRepository.findByEstado(estado)
                .stream()
                .map(VehiculoDocumento::getVehiculo)
                .distinct() // Evita duplicados si un carro tiene 2 documentos en el mismo estado
                .collect(Collectors.toList());
    }

    public List<Vehiculo> buscarPorTipoDocumento(Long documentoId) {
        return vehiculoDocumentoRepository.findByDocumento_Id(documentoId)
                .stream()
                .map(VehiculoDocumento::getVehiculo)
                .distinct()
                .collect(Collectors.toList());
    }

    // --- REQUERIMIENTO: AGREGAR DOCUMENTOS A VEHÍCULO EXISTENTE ---
    @Transactional
    public void agregarDocumentoAVehiculo(Long vehiculoId, Long documentoId, LocalDate exp, LocalDate ven) {
        Vehiculo v = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        Documento d = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        VehiculoDocumento relacion = new VehiculoDocumento();
        relacion.setVehiculo(v);
        relacion.setDocumento(d);
        relacion.setEstado("En Verificación");
        relacion.setFechaExpedicion(exp);
        relacion.setFechaVencimiento(ven);

        vehiculoDocumentoRepository.save(relacion);
    }

    @Transactional
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        // REQUERIMIENTO: No se puede crear sin documento asociado
        if (vehiculo.getDocumentos() == null || vehiculo.getDocumentos().isEmpty()) {
            throw new RuntimeException("Error: No se puede crear un vehículo sin documentos asociados.");
        }

        // REQUERIMIENTO: Estado inicial "En Verificación"
        for (VehiculoDocumento rel : vehiculo.getDocumentos()) {
            rel.setVehiculo(vehiculo); // Vinculamos la relación al objeto padre
            rel.setEstado("En Verificación"); // Forzamos el estado inicial
        }

        // Al guardar el vehículo con CascadeType.ALL, se guardan las relaciones automáticamente
        return vehiculoRepository.save(vehiculo);
    }


    //GEt datos
    public List<Vehiculo> listarTodos(){
        return vehiculoRepository.findAll();
    }

    //PUT
    public Vehiculo findById(Long id){
        return vehiculoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No hay veh por id"));
    }

    @Transactional
    public Vehiculo actualizarDatosBasicos(Long id, Vehiculo datosNuevos) {
        // 1. Buscar el vehículo actual
        Vehiculo vehiculoExistente = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + id));

        // 2. Actualizar solo campos básicos (NO tocamos la lista de documentos aquí)
        vehiculoExistente.setMarca(datosNuevos.getMarca());
        vehiculoExistente.setModelo(datosNuevos.getModelo());
        vehiculoExistente.setColor(datosNuevos.getColor());
        vehiculoExistente.setLinea(datosNuevos.getLinea());
        vehiculoExistente.setServicio(datosNuevos.getServicio());
        vehiculoExistente.setCapacidad(datosNuevos.getCapacidad());
        vehiculoExistente.setCombustible(datosNuevos.getCombustible());

        // 3. Guardar cambios
        return vehiculoRepository.save(vehiculoExistente);
    }

    public void deleteById(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con ID: " + id);
        }
        vehiculoRepository.deleteById(id);
    }
}
