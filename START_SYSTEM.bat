@echo off
title Lanzador PPOOII Pro V1 - Fase 3
echo ====================================================
echo   INICIANDO SISTEMA DE GESTION DE RUTAS (FASE 3)
echo ====================================================
echo.
echo [1/1] Iniciando Sistema Integral (Backend + Frontend embebido)...
echo Nota: Este proceso solo requiere Java. No es necesario Node.js.
powershell -ExecutionPolicy Bypass -File run_app.ps1
echo.
echo Proceso de inicio completado.
pause
