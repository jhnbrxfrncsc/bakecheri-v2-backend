@echo off

call "%~dp0config.bat"

echo Deploying WAR...

copy /Y "%PROJECT_DIR%\target\%WAR_NAME%" "%TOMCAT_DIR%\webapps\" >> "%LOG_DIR%\deploy.log" 2>&1

IF ERRORLEVEL 1 (
    echo Deployment failed.
    exit /b 1
)

echo Deployment successful.