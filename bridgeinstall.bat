@ECHO OFF

SET CHEC_CUSTOMER_NAME=JanLinders
IF "~%SI_APP_DATA%~" == "~~" SET SI_APP_DATA=C:\Program Files\IBM\StoreIntegrator
IF "~%INSTALL_LOG_DIR%~" == "~~" SET INSTALL_LOG_DIR=C:\Install\Log
SET INSTALL_LOG=%INSTALL_LOG_DIR%\%CHEC_CUSTOMER_NAME%LaneInstall.log

REM Copy in the posbbridge.jar
ECHO Copying POSBC Bridge...
ECHO Copying POSBC Bridge... >> %INSTALL_LOG%
IF EXIST "posbcbridge.jar" XCOPY ".\posbcbridge.jar" "%SI_APP_DATA%\user\posbc\posbcbridge.jar" /S /F /R /Y >> %INSTALL_LOG% 2>&1
IF ERRORLEVEL 1 ECHO Unable to copy Store Integrator extensions. ERROR=%ERRORLEVEL%
IF ERRORLEVEL 1 ECHO Unable to copy Store Integrator extensions. ERROR=%ERRORLEVEL% >> %INSTALL_LOG%
IF ERRORLEVEL 1 EXIT /b %ERRORLEVEL%