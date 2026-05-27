-- 1. Tabla Vehículos
CREATE TABLE IF NOT EXISTS vehiculos (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('auto', 'moto')),
    placa CHAR(6) NOT NULL UNIQUE,
    servicio VARCHAR(10) NOT NULL CHECK (servicio IN ('publico', 'privado')),
    combustible VARCHAR(15) NOT NULL CHECK (combustible IN ('gasolina', 'gas', 'diesel')),
    capacidad SMALLINT NOT NULL,
    color VARCHAR(7),
    modelo INTEGER NOT NULL,
    marca VARCHAR(50) NOT NULL,
    linea VARCHAR(50) NOT NULL
);

-- 2. Tabla Documentos (Maestra)
CREATE TABLE IF NOT EXISTS documentos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    aplica_a VARCHAR(2) NOT NULL CHECK (aplica IN ('A', 'M', 'AM')),
    obligatoriedad VARCHAR(2) NOT NULL CHECK (obligatoriedad IN ('RA', 'RM', 'RR')),
    descripcion TEXT
);

-- 3. Tabla Relación (Vehículo <-> Documentos)
CREATE TABLE IF NOT EXISTS vehiculo_documentos (
    id BIGSERIAL PRIMARY KEY,
    vehiculo_id BIGINT NOT NULL REFERENCES vehiculos(id),
    documento_id BIGINT NOT NULL REFERENCES documentos(id),
    fecha_expedicion DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('Habilitado', 'Vencido', 'En Verificación'))
);

-- =============================================
-- 1. MODIFICACIÓN DE TABLA EXISTENTE (ENTREGA 2)
-- =============================================
-- Agregamos el campo para el documento PDF en Base64
-- Usamos TEXT porque un Base64 largo puede superar los límites de un VARCHAR
ALTER TABLE vehiculo_documentos 
ADD COLUMN documento_pdf TEXT; 


-- =============================================
-- 2. NUEVAS ENTIDADES
-- =============================================

-- Tabla Persona
CREATE TABLE IF NOT EXISTS persona (
    id_persona BIGSERIAL PRIMARY KEY,
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    tipo_identificacion CHAR(2) NOT NULL CHECK (tipo_identificacion = 'CC'),
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(100) UNIQUE NOT NULL,
    tipo_persona CHAR(1) NOT NULL CHECK (tipo_persona IN ('C', 'A')) -- C: Conductor, A: Administrativo
);

-- Tabla Usuario (Llave primaria compuesta según requerimiento)
CREATE TABLE IF NOT EXISTS usuario (
    login VARCHAR(50) NOT NULL,
    idpersona BIGINT NOT NULL,
    password VARCHAR(255) NOT NULL,
    apikey VARCHAR(255) NOT NULL,
    PRIMARY KEY (idpersona, login),
    CONSTRAINT fk_usuario_persona FOREIGN KEY (idpersona) REFERENCES persona(id_persona) ON DELETE CASCADE
);

-- Tabla Relación Vehículo <-> Persona (Conductores)
CREATE TABLE IF NOT EXISTS vehiculo_conductor (
    id BIGSERIAL PRIMARY KEY,
    vehiculo_id BIGINT NOT NULL REFERENCES vehiculos(id),
    persona_id BIGINT NOT NULL REFERENCES persona(id_persona),
    fecha_asociacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado_conductor CHAR(2) NOT NULL CHECK (estado_conductor IN ('PO', 'EA', 'RO')), -- PO, EA, RO
    CONSTRAINT fk_conductor_persona FOREIGN KEY (persona_id) REFERENCES persona(id_persona)
);