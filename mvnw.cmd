@REM Maven Wrapper - downloads Maven 3.9.6 on first run then executes it
@echo off
setlocal enabledelayedexpansion

set MAVEN_VERSION=3.9.6
set MAVEN_DIST_DIR=%USERPROFILE%\.mvn-wrapper\apache-maven-%MAVEN_VERSION%
set MVN_EXE=%MAVEN_DIST_DIR%\bin\mvn.cmd

if not exist "%MVN_EXE%" (
    echo [mvnw] Apache Maven %MAVEN_VERSION% not found. Downloading...
    set MAVEN_ZIP=%TEMP%\apache-maven-%MAVEN_VERSION%-bin.zip
    set MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip

    powershell -Command "Write-Host '[mvnw] Downloading from Maven Central...'; [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '!MAVEN_URL!' -OutFile '!MAVEN_ZIP!' -UseBasicParsing"
    if errorlevel 1 (
        echo [mvnw] ERROR: Download failed. Check your internet connection.
        exit /b 1
    )

    powershell -Command "Write-Host '[mvnw] Extracting...'; Expand-Archive -Path '!MAVEN_ZIP!' -DestinationPath '%USERPROFILE%\.mvn-wrapper' -Force"
    del "!MAVEN_ZIP!" >nul 2>&1

    echo [mvnw] Maven %MAVEN_VERSION% ready.
)

set JAVA_HOME_OVERRIDE=C:\Users\User\Downloads\jdk-26_windows-x64_bin\jdk-26.0.1
if exist "%JAVA_HOME_OVERRIDE%\bin\java.exe" (
    set JAVA_HOME=%JAVA_HOME_OVERRIDE%
)

"%MVN_EXE%" %*
