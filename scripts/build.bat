@echo off

call "%~dp0config.bat"

echo ================================ > "%LOG_DIR%\build.log"
echo Building project...

pushd "%PROJECT_DIR%"

call mvn clean package >> "%LOG_DIR%\build.log" 2>&1

IF ERRORLEVEL 1 (
    echo.
    echo Build Failed.
    popd
    exit /b 1
)

popd

echo Build Successful.
exit /b 0