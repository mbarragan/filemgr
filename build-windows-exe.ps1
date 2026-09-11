param(
    [string]$Version = "1.0.0",
    [ValidateSet("app-image", "exe")]
    [string]$PackageType = "app-image"
)

$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$SrcDir = Join-Path $ProjectRoot "src"
$BuildDir = Join-Path $ProjectRoot "build"
$ClassesDir = Join-Path $BuildDir "classes"
$InputDir = Join-Path $BuildDir "jpackage-input"
$DistDir = Join-Path $ProjectRoot "dist"
$AppName = "FileMgr"
$MainClass = "dev.quercusdata.indexer.IndexadorUI"
$JarName = "filemgr.jar"
$Vendor = "QuercusData"
$Description = "My Files Manager"
$MenuGroup = "QuercusData"
$PortableName = "$AppName-$Version-windows"
$PortableDir = Join-Path $DistDir $PortableName
$ZipPath = Join-Path $DistDir ($PortableName + ".zip")

Write-Host "==> Limpiando carpetas temporales"
if (Test-Path $ClassesDir) { Remove-Item $ClassesDir -Recurse -Force }
if (Test-Path $InputDir) { Remove-Item $InputDir -Recurse -Force }
if (Test-Path $PortableDir) { Remove-Item $PortableDir -Recurse -Force }
if (Test-Path $ZipPath) { Remove-Item $ZipPath -Force }
New-Item -ItemType Directory -Path $ClassesDir | Out-Null
New-Item -ItemType Directory -Path $InputDir | Out-Null
if (-not (Test-Path $DistDir)) { New-Item -ItemType Directory -Path $DistDir | Out-Null }

Write-Host "==> Compilando fuentes Java"
$JavaFiles = Get-ChildItem -Path (Join-Path $SrcDir "dev") -Recurse -Filter *.java | ForEach-Object { $_.FullName }
if (-not $JavaFiles -or $JavaFiles.Count -eq 0) {
    throw "No se encontraron fuentes Java en '$SrcDir\\dev'."
}
& javac -encoding UTF-8 -d $ClassesDir $JavaFiles
if ($LASTEXITCODE -ne 0) { throw "Fallo compilando con javac." }

Write-Host "==> Copiando recursos runtime (.properties)"
Copy-Item -Path (Join-Path $SrcDir "*.properties") -Destination $ClassesDir -Force

Write-Host "==> Empaquetando JAR"
Push-Location $ClassesDir
try {
    & jar --create --file (Join-Path $InputDir $JarName) .
    if ($LASTEXITCODE -ne 0) { throw "Fallo creando el JAR." }
}
finally {
    Pop-Location
}

Write-Host "==> Generando paquete Windows ($PackageType)"
if (Test-Path (Join-Path $DistDir $AppName)) { Remove-Item (Join-Path $DistDir $AppName) -Recurse -Force }
$jpackageArgs = @(
    "--type", $PackageType,
    "--name", $AppName,
    "--app-version", $Version,
    "--input", $InputDir,
    "--main-jar", $JarName,
    "--main-class", $MainClass,
    "--dest", $DistDir,
    "--vendor", $Vendor,
    "--description", $Description,
    "--copyright", "(c) QuercusData"
)

if ($PackageType -eq "exe") {
    $jpackageArgs += @("--win-menu", "--win-menu-group", $MenuGroup, "--win-shortcut")
}

& jpackage @jpackageArgs
if ($LASTEXITCODE -ne 0) { throw "Fallo generando paquete con jpackage." }

$ImageDir = Join-Path $DistDir $AppName
if (-not (Test-Path $ImageDir)) {
    throw "No se generó la app-image esperada en '$ImageDir'."
}

Write-Host "==> Versionando artefacto final"
Copy-Item -Path $ImageDir -Destination $PortableDir -Recurse -Force
Compress-Archive -Path (Join-Path $PortableDir "*") -DestinationPath $ZipPath -Force

Write-Host "==> Listo"
Write-Host "EXE:  $PortableDir\\$AppName.exe"
Write-Host "ZIP:  $ZipPath"

