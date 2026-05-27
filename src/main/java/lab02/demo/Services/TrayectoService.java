package lab02.demo.Services;

import lab02.demo.Entities.Trayecto;
import java.util.List;

public interface TrayectoService {
    Trayecto crearTrayecto(Trayecto trayecto);
    List<Trayecto> obtenerPorCodigoRuta(String codigoRuta);
    List<Trayecto> obtenerPorConductor(Long idPersona);
    List<Trayecto> obtenerPorVehiculo(String placa);
    List<Trayecto> obtenerExcepciones();
}
