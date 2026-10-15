@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM Delegates to the system Maven installation (mvn).
@REM ----------------------------------------------------------------------------

@if "%DEBUG%" == "" @echo off
@setlocal

set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
if "%MAVEN_PROJECTBASEDIR%"=="" set MAVEN_PROJECTBASEDIR=%CD%

if not defined MAVEN_OPTS set MAVEN_OPTS=

mvn %*

set ERROR_CODE=%ERRORLEVEL%
endlocal
exit /b %ERROR_CODE%
