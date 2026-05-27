-- 1. LIMPIEZA DE DATOS PREVIOS (Opcional, usar con cuidado)
-- DELETE FROM trayecto;
-- DELETE FROM vehiculo_documento;
-- DELETE FROM vehiculos;
-- DELETE FROM usuario;
-- DELETE FROM persona;

-- 2. INSERTAR PERSONAS (Conductores y Administradores)
-- Conductor 1: Operativo
INSERT INTO persona (id_persona, identificacion, tipo_identificacion, nombres, apellidos, correo_electronico, tipo_persona, estado_conductor, fecha_vigencia_licencia)
VALUES (1, '12345678', 'CC', 'Juan Carlos', 'Pérez Rodríguez', 'juan.perez@rutas.com', 'C', 'OP', '2030-12-31');

-- Conductor 2: Restringido (Licencia Vencida para probar lógica)
INSERT INTO persona (id_persona, identificacion, tipo_identificacion, nombres, apellidos, correo_electronico, tipo_persona, estado_conductor, fecha_vigencia_licencia)
VALUES (2, '87654321', 'CC', 'Marta Lucía', 'Gómez', 'marta.gomez@rutas.com', 'C', 'RO', '2023-01-01');

-- Administrador
INSERT INTO persona (id_persona, identificacion, tipo_identificacion, nombres, apellidos, correo_electronico, tipo_persona)
VALUES (3, '11223344', 'CC', 'Admin', 'Sistema', 'admin@rutas.com', 'A');

-- 3. INSERTAR USUARIOS (Password: '123456' encriptado con BCrypt habitualmente, aquí ponemos el hash estándar si el sistema lo requiere)
-- Nota: El sistema usa PasswordEncoderFactories.createDelegatingPasswordEncoder(), 
-- por lo que el prefijo {noop} sirve para pruebas sin encriptación real o el hash correspondiente.
INSERT INTO usuario (idpersona, login, password)
VALUES (1, 'juanp', '{noop}123456');
INSERT INTO usuario (idpersona, login, password)
VALUES (3, 'admin', '{noop}123456');

-- 4. INSERTAR VEHÍCULOS
INSERT INTO vehiculos (id, tipo, placa, servicio, combustible, capacidad, color, modelo, marca, linea)
VALUES (1, 'auto', 'KRT123', 'publico', 'gasolina', 5, '#FFFFFF', 2024, 'Toyota', 'Fortuner SW');

-- 5. INSERTAR DOCUMENTOS (Requerido por DocumentoRepository/Entity si existe)
-- Asumimos que existen IDs 1 (SOAT) y 2 (Tecno) en la tabla 'documentos'
-- Si no existen, los creamos primero:
-- INSERT INTO documento (id, nombre) VALUES (1, 'SOAT'), (2, 'Tecnicomecanica');

-- 6. ASOCIAR DOCUMENTOS A VEHÍCULO (Deben estar 'Habilitado' para crear trayectos)
INSERT INTO vehiculo_documento (vehiculo_id, documento_id, fecha_expedicion, fecha_vencimiento, estado)
VALUES (1, 1, '2024-01-01', '2025-01-01', 'Habilitado');
INSERT INTO vehiculo_documento (vehiculo_id, documento_id, fecha_expedicion, fecha_vencimiento, estado)
VALUES (1, 2, '2024-01-01', '2025-01-01', 'Habilitado');

-- 2. INSERTAR PERSONAS (Conductores y Administradores adicionales)
-- Conductor 3: Operativo - Experto en Rutas Nacionales
INSERT INTO persona (id_persona, identificacion, tipo_identificacion, nombres, apellidos, correo_electronico, tipo_persona, estado_conductor, fecha_vigencia_licencia)
VALUES (4, '55667788', 'CC', 'Ricardo', 'Alfonso Sierra', 'ricardo.sierra@rutas.com', 'C', 'OP', '2028-05-20');

-- Conductor 4: Nuevo Ingreso - Pendiente de trámite (RO por defecto)
INSERT INTO persona (id_persona, identificacion, tipo_identificacion, nombres, apellidos, correo_electronico, tipo_persona, estado_conductor, fecha_vigencia_licencia)
VALUES (5, '99001122', 'CC', 'Luisa', 'Fernanda Castro', 'luisa.castro@rutas.com', 'C', 'RO', '2025-02-15');

-- 3. INSERTAR USUARIOS adicionales
INSERT INTO usuario (idpersona, login, password)
VALUES (4, 'ricardos', '{noop}123456');
INSERT INTO usuario (idpersona, login, password)
VALUES (5, 'luisac', '{noop}123456');

-- 4. INSERTAR VEHÍCULOS adicionales (Camión y Moto)
INSERT INTO vehiculos (id, tipo, placa, servicio, combustible, capacidad, color, modelo, marca, linea)
VALUES (2, 'camion', 'TRK999', 'publico', 'diesel', 2, '#FF0000', 2022, 'Hino', '500 Series');
INSERT INTO vehiculos (id, tipo, placa, servicio, combustible, capacidad, color, modelo, marca, linea)
VALUES (3, 'moto', 'MTX55A', 'privado', 'gasolina', 2, '#000000', 2023, 'Yamaha', 'MT-03');

-- 6. ASOCIAR DOCUMENTOS (Vehículo 2 Habilitado, Vehículo 3 Vencido)
INSERT INTO vehiculo_documento (vehiculo_id, documento_id, fecha_expedicion, fecha_vencimiento, estado)
VALUES (2, 1, '2024-03-10', '2025-03-10', 'Habilitado');
INSERT INTO vehiculo_documento (vehiculo_id, documento_id, fecha_expedicion, fecha_vencimiento, estado)
VALUES (2, 2, '2024-03-10', '2025-03-10', 'Habilitado');

INSERT INTO vehiculo_documento (vehiculo_id, documento_id, fecha_expedicion, fecha_vencimiento, estado)
VALUES (3, 1, '2023-01-01', '2024-01-01', 'Vencido'); -- Para probar Excepciones

-- 7. RUTA NACIONAL: IBAGUÉ - BOGOTÁ (RUTA-IB-BOG)
-- Conductor: Ricardo Alfonso, Vehículo: Camión Hino
INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (4, 2, 'RUTA-IB-BOG', 'Terminal de Transportes de Ibagué, Colombia', 0, 'admin');

INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (4, 2, 'RUTA-IB-BOG', 'Peaje de Gualanday, Coello, Tolima, Colombia', 1, 'admin');

INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (4, 2, 'RUTA-IB-BOG', 'Parque Principal de Melgar, Tolima, Colombia', 2, 'admin');

INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (4, 2, 'RUTA-IB-BOG', 'Chinauta, Fusagasugá, Cundinamarca, Colombia', 3, 'admin');

INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (4, 2, 'RUTA-IB-BOG', 'Peaje de Chusacá, Sibaté, Cundinamarca, Colombia', 4, 'admin');

INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (4, 2, 'RUTA-IB-BOG', 'Terminal del Sur, Bogotá, Colombia', 5, 'admin');

INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (4, 2, 'RUTA-IB-BOG', 'Aeropuerto El Dorado, Bogotá, Colombia', 6, 'admin');

-- 8. RUTA DE LOGÍSTICA URBANA (RUTA-LOG-01) - REPARTO LOCAL
-- Conductor: Juan Carlos, Vehículo: Toyota
INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (1, 1, 'RUTA-LOG-01', 'Zona Industrial El Papayo, Ibagué, Colombia', 0, 'admin');

INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (1, 1, 'RUTA-LOG-01', 'Makro Ibagué, Colombia', 1, 'admin');

INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (1, 1, 'RUTA-LOG-01', 'Éxito Calle 80, Ibagué, Colombia', 2, 'admin');

INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (1, 1, 'RUTA-LOG-01', 'Mercacentro 10 El Poblado, Ibagué, Colombia', 3, 'admin');

-- 9. CASO DE EXCEPCIÓN (Vehículo con documentos vencidos)
-- Se inserta directamente para probar la consulta de excepciones de la API
INSERT INTO trayecto (id_persona, id_vehiculo, codigo_ruta, ubicacion, orden_parada, login_usuario)
VALUES (1, 3, 'RUTA-ERROR', 'Plaza de Bolívar, Ibagué, Colombia', 0, 'admin');

