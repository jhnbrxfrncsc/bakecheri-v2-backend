@echo off

call "%~dp0config.bat"

echo Stopping Tomcat...

call "%TOMCAT_DIR%\bin\shutdown.bat" > "%LOG_DIR%\shutdown.log" 2>&1

timeout /t 5 /nobreak > nul

echo Tomcat stopped.