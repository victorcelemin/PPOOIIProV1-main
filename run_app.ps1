# 🚀 Script para Iniciar el Sistema Completo (Backend + Frontend)

Write-Host "Iniciando Backend (Spring Boot)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd . ; ./mvnw spring-boot:run" -WindowStyle Normal

Write-Host "Esperando a que el Backend inicie (15s)..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

Write-Host "Iniciando Frontend (Next.js)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd frontend ; node 'C:\Program Files\nodejs\node_modules\npm\bin\npm-cli.js' run dev" -WindowStyle Normal

Write-Host "----------------------------------------------------"
Write-Host "✅ Sistema en ejecución!" -ForegroundColor Green
Write-Host "Backend: http://localhost:8080"
Write-Host "Frontend: http://localhost:3000"
Write-Host "----------------------------------------------------"
