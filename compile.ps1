$ErrorActionPreference = "Stop"

$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$jdkCandidates = @(
    "$env:JAVA_HOME",
    "$env:USERPROFILE\.jdks\ms-21.0.11",
    "$env:LOCALAPPDATA\Programs\IntelliJ IDEA\jbr"
) | Where-Object { $_ -and (Test-Path (Join-Path $_ "bin\java.exe")) }

if (-not $jdkCandidates) {
    throw "No se encontro JDK. Configure JAVA_HOME con JDK 21."
}

$javaHome = $jdkCandidates[0]
$javaExe = Join-Path $javaHome "bin\java.exe"
$wrapperJar = Join-Path $projectDir ".mvn\wrapper\maven-wrapper.jar"

if (-not (Test-Path $wrapperJar)) {
    throw "No se encontro $wrapperJar"
}

& $javaExe "-Dmaven.multiModuleProjectDirectory=$projectDir" -classpath $wrapperJar org.apache.maven.wrapper.MavenWrapperMain -DskipTests compile
exit $LASTEXITCODE
