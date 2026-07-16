@echo off

REM ==========================
REM BakeCheri Configuration
REM ==========================

SETLOCAL EnableDelayedExpansion

REM Project root
SET "PROJECT_DIR=%~dp0.."
FOR %%I IN ("%PROJECT_DIR%") DO SET "PROJECT_DIR=%%~fI"

REM Tomcat
SET "TOMCAT_DIR=C:\apache-tomcat-10.1.53"

REM Project
SET "WAR_NAME=BakeCheri_v2.war"

REM Logs
SET "LOG_DIR=%PROJECT_DIR%\logs"

IF NOT EXIST "%LOG_DIR%" (
    mkdir "%LOG_DIR%"
)

ENDLOCAL & (
    SET "PROJECT_DIR=%PROJECT_DIR%"
    SET "TOMCAT_DIR=%TOMCAT_DIR%"
    SET "WAR_NAME=%WAR_NAME%"
    SET "LOG_DIR=%LOG_DIR%"
)