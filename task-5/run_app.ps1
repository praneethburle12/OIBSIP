$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Auto-kill process on port 8081
$port = 8081
$tcpConnection = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue | Select-Object -First 1
if ($tcpConnection) {
    $processId = $tcpConnection.OwningProcess
    if ($processId -gt 0) {
        Write-Host "Port $port is in use by PID $processId. Stopping it..."
        Stop-Process -Id $processId -Force
        Start-Sleep -Seconds 2
    }
}

$wrapperJar = Join-Path $PSScriptRoot ".mvn\wrapper\maven-wrapper.jar"
$mainClass = "org.apache.maven.wrapper.MavenWrapperMain"
Write-Host "Starting Digital Library..."
Write-Host "JAVA_HOME: $env:JAVA_HOME"
Write-Host "Project Root: $PSScriptRoot"

& "$env:JAVA_HOME\bin\java.exe" -classpath "$wrapperJar" "-Dmaven.multiModuleProjectDirectory=$PSScriptRoot" $mainClass spring-boot:run
