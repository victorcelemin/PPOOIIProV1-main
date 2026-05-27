# 🚚 PPOOII Pro V1 - Fase 3: Gestión de Rutas y Conductores

Este proyecto es un sistema integral para la gestión de rutas logísticas, monitoreo de conductores y visualización en tiempo real mediante mapas.

---

## 🚀 Cómo Iniciar el Proyecto (Rápido)

Para facilitar la ejecución, he incluido un archivo ejecutable que inicia todo el ecosistema (Backend + Frontend) con un solo clic:

1. **Doble clic en:** `START_SYSTEM.bat`
2. El script abrirá dos terminales:
   - **Terminal 1:** Ejecutando el Backend en `http://localhost:8080`.
   - **Terminal 2:** Ejecutando el Frontend en `http://localhost:3000`.

---

## 🧪 Cómo Probar la Aplicación

He incluido múltiples formas de validar el sistema:

### 1. Script de Prueba Automático
- Ejecuta `test_api.ps1` (Clic derecho -> Ejecutar con PowerShell).
- Este script probará automáticamente el Login y la consulta de rutas en el backend.

### 2. Guías Detalladas
- **[README_TESTING.md](./README_TESTING.md):** Guía de rutas, conductores y lógica de negocio.
- **[POSTMAN_TESTING.md](./POSTMAN_TESTING.md):** Guía paso a paso para usar Postman desde cero.

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 25 / Spring Boot 4**
- **Spring Data JPA & MySQL**
- **Spring Security (JWT)**
- **Scheduling (Cron Jobs):** Monitoreo automático de licencias y documentos.
- **Google Maps API:** Geocodificación automática de trayectos.

### Frontend
- **React / Next.js (App Router)**
- **Tailwind CSS:** Diseño moderno y responsivo.
- **Google Maps JS API:** Visualización interactiva de rutas.
- **Lucide React:** Iconografía dinámica.

---

## 🗃️ Base de Datos
Antes de iniciar, asegúrate de tener una base de datos MySQL llamada `lab02` y ejecutar el script de datos iniciales ubicado en:
`src/main/resources/sql/data_fase3.sql`

---

## 🔑 Configuración de API Keys
Las llaves de Google Maps ya están configuradas en:
- **Backend:** `src/main/resources/application.properties`
- **Frontend:** `frontend/.env.local`

---
*Desarrollado por Gemini CLI para la Fase 3 del proyecto PPOOII.*
