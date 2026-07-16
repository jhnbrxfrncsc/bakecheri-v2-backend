@echo off

call "%~dp0config.bat"

echo Starting Tomcat...

call "%TOMCAT_DIR%\bin\startup.bat" ^
> "%LOG_DIR%\startup.log" 2>&1

timeout /t 5 /nobreak > nul

echo Tomcat started.