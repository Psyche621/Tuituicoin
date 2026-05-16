@echo off
REM Tuiranode CLI wrapper script for Windows

REM Get the directory where this script is located
set SCRIPT_DIR=%~dp0

REM JAR file location
set JAR_FILE=%SCRIPT_DIR%tuituicoin\target\tuituinode-1.0-SNAPSHOT-shaded.jar

REM Check if JAR exists
if not exist "%JAR_FILE%" (
    echo Error: JAR file not found at %JAR_FILE%
    echo Please run: mvn clean package
    exit /b 1
)

REM Execute the JAR with all passed arguments
java -jar "%JAR_FILE%" %*
