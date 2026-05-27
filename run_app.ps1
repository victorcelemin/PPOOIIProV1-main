# 🚀 Script para Iniciar el Sistema Completo (Backend que incluye Frontend)

Write-Host "Iniciando Sistema Integral (Spring Boot)..." -ForegroundColor Cyan
Write-Host "Nota: El Frontend ha sido integrado en el Backend. No requiere Node.js para ejecutarse." -ForegroundColor Gray
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd . ; ./mvnw spring-boot:run" -WindowStyle Normal

Write-Host "----------------------------------------------------"
Write-Host "✅ Sistema en ejecución!" -ForegroundColor Green
Write-Host "Acceso Local: http://localhost:8080"
Write-Host "----------------------------------------------------"
