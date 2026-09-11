# Ejecutable Windows (sin Java instalado)

Este proyecto puede distribuirse como app Windows auto-contenida.
El script `build-windows-exe.ps1` compila el código, empaqueta un JAR y crea una `app-image` con su propio runtime Java.

## Requisitos para construir

- Windows
- JDK 21+ con `javac`, `jar` y `jpackage` en `PATH`

## Construcción

Ejemplo con versión `1.0.3`:

```powershell
powershell -ExecutionPolicy Bypass -File .\build-windows-exe.ps1 -Version 1.0.3 -PackageType app-image
```

## Artefactos generados

- Carpeta portable: `dist\FileMgr-1.0.3-windows\`
- Ejecutable: `dist\FileMgr-1.0.3-windows\FileMgr.exe`
- ZIP para distribuir: `dist\FileMgr-1.0.3-windows.zip`

## Metadatos

- El ejecutable incluye metadatos de aplicación (vendor y descripción).

Para generar instalador `exe` (con acceso directo y menú inicio):

```powershell
powershell -ExecutionPolicy Bypass -File .\build-windows-exe.ps1 -Version 1.0.3 -PackageType exe
```

## Uso en una máquina sin Java

1. Copiar `FileMgr-<version>-windows.zip` a la máquina destino.
2. Descomprimir.
3. Ejecutar `FileMgr.exe`.

No requiere instalación previa de Java porque el runtime ya va embebido.

NOTA: el CSV generado usa el carácter `|` como separador único de campos.

