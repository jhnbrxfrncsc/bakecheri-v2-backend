@echo off

call "%~dp0config.bat"

echo.
echo =====================================
echo BakeCheri Deployment
echo =====================================
echo.

call "%~dp0build.bat"
IF ERRORLEVEL 1 goto fail

call "%~dp0stop_tomcat.bat"

call "%~dp0clean_tomcat.bat"

call "%~dp0deploy.bat"
IF ERRORLEVEL 1 goto fail


echo.
echo ============================================
echo        BakeCheri Tomcat Debug Mode
echo ============================================
echo.
echo JPDA Address : localhost:8000
echo Debugger     : IntelliJ IDEA
echo.
echo Waiting for debugger to attach...
echo Press Ctrl+C to stop Tomcat.
echo.

REM Optional: Change debug port
SET JPDA_ADDRESS=8000

REM Optional: Use dt_socket transport
SET JPDA_TRANSPORT=dt_socket

call "%TOMCAT_DIR%\bin\catalina.bat" jpda run

echo.
echo =====================================
echo Deployment Successful!
echo =====================================
