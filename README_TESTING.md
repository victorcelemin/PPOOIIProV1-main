# 🗺️ Guía de Pruebas - Fase 3: Gestión de Rutas

Esta guía contiene los datos necesarios para validar la implementación de la Fase 3, incluyendo las rutas cargadas en el script SQL y cómo probarlas en el frontend y backend.

---

## 🔐 1. Credenciales de Acceso

Para probar los endpoints protegidos o el flujo de creación:

| Usuario | Login | Contraseña | Rol |
| :--- | :--- | :--- | :--- |
| **Juan Carlos (Conductor)** | `juanp` | `123456` | `CONDUTOR` |
| **Administrador** | `admin` | `123456` | `ADMIN` |
| **Ricardo Alfonso** | `ricardos` | `123456` | `CONDUTOR` |

---

## 📍 2. Rutas Disponibles (Cargar vía SQL)

Busca estos códigos en el buscador del Frontend (`http://localhost:3000`):

### A. RUTA-001: Circuito Turístico Ibagué
*   **Descripción:** 7 paradas en puntos emblemáticos de la ciudad.
*   **Puntos:** Conservatorio -> Panóptico -> Plaza de Bolívar -> C.C. La Estación -> Acqua -> Univ. del Tolima -> Terminal.
*   **Estado:** Operativo.

### B. RUTA-IB-BOG: Ruta Nacional Intermunicipal
*   **Descripción:** Trayecto de larga distancia desde Ibagué hasta Bogotá.
*   **Puntos:** Terminal Ibagué -> Peaje Gualanday -> Melgar -> Fusagasugá -> Peaje Chusacá -> Terminal Sur -> Aeropuerto El Dorado.
*   **Estado:** Operativo.

### C. RUTA-LOG-01: Logística Urbana
*   **Descripción:** Reparto en zona comercial e industrial.
*   **Puntos:** Zona Industrial El Papayo -> Makro -> Éxito 80 -> Mercacentro 10.
*   **Estado:** Operativo.

---

## ⚠️ 3. Casos de Prueba Especiales

### ❌ Prueba de Excepciones (`RUTA-ERROR`)
*   **Descripción:** Esta ruta utiliza una Moto con documentos **Vencidos**.
*   **Cómo probar:** Consulta el endpoint `GET /api/trayectos/excepciones`. Debería listar los trayectos de esta ruta indicando que el vehículo no está habilitado.

### 🚫 Prueba de Bloqueo (Conductor RO)
*   **Usuario:** `luisac` (Luisa Fernanda Castro).
*   **Escenario:** Luisa tiene su licencia vencida (o está marcada como RO).
*   **Resultado esperado:** Si intentas crear un trayecto (`POST /api/trayectos`) usando su ID de persona, el backend devolverá un error: *"El conductor está restringido para operar (RO)"*.

---

## ⚙️ 4. Endpoints Principales (Backend)

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/trayectos/ruta/{codigo}` | Obtiene todos los puntos de una ruta ordenados. |
| `GET` | `/api/trayectos/conductor/{id}` | Lista los códigos de ruta de un conductor. |
| `GET` | `/api/trayectos/excepciones` | Lista rutas con conductores en RO o vehículos vencidos. |
| `POST` | `/api/trayectos` | Crea un nuevo punto (requiere validación de habilitación). |

---

## 💡 Notas Adicionales
1.  **Geocodificación:** Al insertar los datos, los trayectos no tienen Lat/Lng. Espera **90 segundos** con el backend encendido para que el Cron Job asigne las coordenadas automáticamente.
2.  **Mapa:** Si el mapa no carga, verifica que tu API Key en `frontend/.env.local` sea válida y tenga activada la "Maps JavaScript API".
