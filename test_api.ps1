# 🧪 Script de Prueba Automática de la API

$baseUrl = "http://localhost:8080/api"

Write-Host "--- 1. Probando Autenticación ---" -ForegroundColor Cyan
$loginBody = @{
    login = "admin"
    password = "123456"
} | ConvertTo-Json

try {
    $authResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $token = $authResponse.token
    Write-Host "✅ Login Exitoso. Token obtenido." -ForegroundColor Green
} catch {
    Write-Host "❌ Error en Login: $_" -ForegroundColor Red
    exit
}

$headers = @{
    Authorization = "Bearer $token"
}

Write-Host "`n--- 2. Probando Consulta de Ruta (RUTA-001) ---" -ForegroundColor Cyan
try {
    $ruta = Invoke-RestMethod -Uri "$baseUrl/trayectos/ruta/RUTA-001" -Method Get -Headers $headers
    Write-Host "✅ Ruta obtenida. Puntos encontrados: $($ruta.Count)" -ForegroundColor Green
    foreach ($p in $ruta) {
        Write-Host "   - [$($p.ordenParada)] $($p.ubicacion)"
    }
} catch {
    Write-Host "❌ Error al obtener ruta: $_" -ForegroundColor Red
}

Write-Host "`n--- 3. Probando Excepciones ---" -ForegroundColor Cyan
try {
    $ex = Invoke-RestMethod -Uri "$baseUrl/trayectos/excepciones" -Method Get -Headers $headers
    Write-Host "✅ Excepciones obtenidas: $($ex.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ Error al obtener excepciones: $_" -ForegroundColor Red
}

Write-Host "`n--- Pruebas Finalizadas ---" -ForegroundColor Yellow
pause
