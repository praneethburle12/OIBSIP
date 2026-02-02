@echo off
REM Maven wrapper script for Windows

setlocal enabledelayedexpansion

if not "!JAVA_HOME!" == "" goto javaOk
echo Error: JAVA_HOME not found in your environment.
exit /b 1

:javaOk
set WRAPPER_JAR=%~dp0\.mvn\wrapper\maven-wrapper.jar
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

"!JAVA_HOME!\bin\java.exe" ^
  -classpath "%WRAPPER_JAR%" ^
  -Dmaven.multiModuleProjectDirectory="%~dp0" ^
  !MAVEN_OPTS! ^
  "%WRAPPER_LAUNCHER%" %*

endlocal
