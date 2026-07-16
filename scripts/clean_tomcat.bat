@echo off

call "%~dp0config.bat"

echo Cleaning deployment...

IF EXIST "%TOMCAT_DIR%\webapps\BakeCheri_v2" (
    rmdir /S /Q "%TOMCAT_DIR%\webapps\BakeCheri_v2"
)

IF EXIST "%TOMCAT_DIR%\webapps\BakeCheri_v2.war" (
    del "%TOMCAT_DIR%\webapps\BakeCheri_v2.war"
)

echo Clean complete.