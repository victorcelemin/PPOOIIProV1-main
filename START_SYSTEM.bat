@echo off
title Lanzador PPOOII Pro V1 - Fase 3
echo ====================================================
echo   INICIANDO SISTEMA DE GESTION DE RUTAS (FASE 3)
echo ====================================================
echo.
echo [1/2] Iniciando Backend y Frontend mediante PowerShell...
powershell -ExecutionPolicy Bypass -File run_app.ps1
echo.
echo Proceso de inicio completado.
pause
