@echo off
REM ========================================
REM Deploy Script for BakeCheri_v2 Project
REM ========================================

REM Set paths
SET PROJECT_DIR=%~dp0
SET TOMCAT_DIR=C:\apache-tomcat-10.1.53
SET WAR_NAME=BakeCheri_v2.war
SET LOG_FILE=%PROJECT_DIR%logs/restart_server.log

REM Timestamp
echo ======================================== >> "%LOG_FILE%"
echo Deploy started at %DATE% %TIME% >> "%LOG_FILE%"
echo ======================================== >> "%LOG_FILE%"

REM Stop Tomcat
echo Stopping Tomcat...
call "%TOMCAT_DIR%\bin\shutdown.bat" >> "%LOG_FILE%" 2>&1
echo Waiting 10 seconds for Tomcat to stop...
timeout /t 10 /nobreak >nul

REM Build WAR with Maven
echo Building WAR... >> "%LOG_FILE%"
cmd /c mvn clean package >> "%LOG_FILE%" 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo Maven build failed! Check deploy.log
    pause
    exit /b %ERRORLEVEL%
)

REM Copy WAR to Tomcat webapps
echo Copying WAR to Tomcat... >> "%LOG_FILE%"
cmd /c copy /Y "%PROJECT_DIR%target\%WAR_NAME%" "%TOMCAT_DIR%\webapps\" >> "%LOG_FILE%" 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo Failed to copy WAR! Check deploy.log
    pause
    exit /b %ERRORLEVEL%
)

REM Stop Tomcat, before re-build.
echo Starting Tomcat...
call "%TOMCAT_DIR%\bin\startup.bat" >> "%LOG_FILE%" 2>&1

echo Deployment complete! >> "%LOG_FILE%"
echo ======================================== >> "%LOG_FILE%"
echo Done! Check deploy.log for details.
pause