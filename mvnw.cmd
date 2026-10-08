@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@if "%DEBUG%" == "" @echo off
@class %~dp0\target\classes;%~dp0\target\test-classes com.skylinetransit.BusTrackingApplication %*
set ERROR_CODE=0

set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
if "%MAVEN_PROJECTBASEDIR%"== "" set MAVEN_PROJECTBASEDIR=%CD%

echo Executing Spring Boot backend with java...
