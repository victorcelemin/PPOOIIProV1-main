# 🚀 Script para Iniciar el Sistema Completo (Backend que incluye Frontend)

Write-Host "Verificando puerto 8080..." -ForegroundColor Yellow
$portStatus = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($portStatus) {
    Write-Host "⚠️ El puerto 8080 ya está en uso. Intentando cerrarlo..." -ForegroundColor Red
    Stop-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
}

Write-Host "Iniciando Sistema Integral (Spring Boot)..." -ForegroundColor Cyan
Write-Host "Si la ventana se cierra, verifica que tengas JAVA 21+ instalado y MySQL/Postgres corriendo." -ForegroundColor Gray

# Usamos -NoExit para que el usuario pueda ver el error si la app falla al iniciar
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd . ; ./mvnw spring-boot:run" -WindowStyle Normal

Write-Host "Esperando validación de arranque (10s)..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "----------------------------------------------------"
Write-Host "✅ Intento de inicio completado!" -ForegroundColor Green
Write-Host "Acceso Local: http://localhost:8080"
Write-Host "Si ves 'Connection Refused', revisa la terminal del Backend para ver el error exacto." -ForegroundColor Yellow
Write-Host "----------------------------------------------------"
