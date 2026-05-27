# 🚀 Guía Paso a Paso: Pruebas con Postman

Esta guía te enseñará a probar la API del Sistema de Gestión de Rutas desde cero usando Postman.

---

## 🛠️ Paso 1: Configuración Inicial
1. Abre Postman.
2. Crea una nueva **Colección** llamada `PPOOII Phase 3`.
3. (Opcional) Crea un **Environment** con la variable:
   - `base_url`: `http://localhost:8080/api`

---

## 🔐 Paso 2: Autenticación (Obtener Token)
Antes de consultar rutas, necesitas un token JWT.

1. **Nueva Request:**
   - **Método:** `POST`
   - **URL:** `{{base_url}}/auth/login`
2. **Body (raw JSON):**
   ```json
   {
     "login": "admin",
     "password": "123456"
   }
   ```
3. **Enviar:** Haz clic en **Send**.
4. **Resultado:** Copia el valor del campo `token` que aparece en la respuesta.

---

## 📡 Paso 3: Configurar el Token en la Colección
Para no pegar el token en cada petición:
1. Haz clic derecho en la colección `PPOOII Phase 3` -> **Edit**.
2. Ve a la pestaña **Authorization**.
3. En **Type**, selecciona `Bearer Token`.
4. Pega el token que copiaste en el campo **Token**.
5. Haz clic en **Save**.
*Ahora todas las peticiones dentro de esta carpeta usarán ese token automáticamente.*

---

## 🗺️ Paso 4: Probar los Endpoints de Rutas

### 1. Consultar una Ruta por Código
Muestra todos los puntos (paradas) de una ruta ordenados.
- **Método:** `GET`
- **URL:** `{{base_url}}/trayectos/ruta/RUTA-001`
- **Resultado:** Deberías ver los 7 puntos de Ibagué.

### 2. Consultar Rutas por Conductor
Muestra qué rutas tiene asignadas un conductor específico.
- **Método:** `GET`
- **URL:** `{{base_url}}/trayectos/conductor/1` (ID de Juan Carlos)
- **Resultado:** Un array con `["RUTA-001", "RUTA-LOG-01"]`.

### 3. Consultar Excepciones
Identifica trayectos con problemas (vehículos vencidos o conductores bloqueados).
- **Método:** `GET`
- **URL:** `{{base_url}}/trayectos/excepciones`
- **Resultado:** Debería aparecer la parada en "Plaza de Bolívar" de la ruta `RUTA-ERROR`.

---

## 🚧 Paso 5: Probar Reglas de Negocio (POST)

### 1. Intentar crear trayecto con Conductor Bloqueado
- **Método:** `POST`
- **URL:** `{{base_url}}/trayectos`
- **Body (raw JSON):**
   ```json
   {
     "conductor": { "idPersona": 2 },
     "vehiculo": { "id": 1 },
     "codigoRuta": "TEST-BLOQUEO",
     "ubicacion": "Parque de la 60, Ibagué",
     "ordenParada": 0,
     "loginUsuario": "admin"
   }
   ```
- **Resultado Esperado:** Error `500` o `400` con el mensaje: *"El conductor está restringido para operar (RO)"*.

---

## 🔍 Paso 6: Verificación de Geocodificación
Si acabas de insertar los datos en SQL:
1. Ejecuta el `GET` de `RUTA-001`.
2. Si `latitud` y `longitud` son `null`, espera **90 segundos**.
3. Vuelve a ejecutar el `GET`.
4. Ahora deberían aparecer las coordenadas reales obtenidas de Google Maps.

---

## 💡 Pro Tip: Logs del Servidor
Mientras haces las pruebas en Postman, observa la consola de tu backend (Spring Boot). Verás mensajes como:
- `Ejecutando monitoreo de licencias...`
- `Ejecutando geocodificación de trayectos...`
- `ENVIANDO CORREO A: ...` (Simulación de bloqueo).
