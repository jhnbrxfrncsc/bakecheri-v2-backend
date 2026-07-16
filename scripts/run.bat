@echo off

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

call "%~dp0start_tomcat.bat"

echo.
echo =====================================
echo Deployment Successful!
echo =====================================

pause
exit /b 0

:fail
echo.
echo =====================================
echo Deployment Failed!
echo =====================================
pause
exit /b 1