@echo off  
echo Compiling EthioTour Connect with SQLite Backend...
if not exist build\classes mkdir build\classes

REM Download dependencies if lib folder doesn't exist
if not exist lib mkdir lib
REM MS SQL download skipped (using SQLite by default)
if not exist lib\HikariCP-5.1.0.jar (
    echo Downloading HikariCP...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/zaxxer/HikariCP/5.1.0/HikariCP-5.1.0.jar' -OutFile 'lib\HikariCP-5.1.0.jar'"
)
if not exist lib\SLF4J-api-2.0.7.jar (
    echo Downloading SLF4J API...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.7/slf4j-api-2.0.7.jar' -OutFile 'lib\SLF4J-api-2.0.7.jar'"
)
if not exist lib\flatlaf-3.4.1.jar (
    echo Downloading FlatLaf...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4.1/flatlaf-3.4.1.jar' -OutFile 'lib\flatlaf-3.4.1.jar'"
)
if not exist lib\flatlaf-extras-3.4.1.jar (
    echo Downloading FlatLaf Extras...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/formdev/flatlaf-extras/3.4.1/flatlaf-extras-3.4.1.jar' -OutFile 'lib\flatlaf-extras-3.4.1.jar'"
)
if not exist lib\sqlite-jdbc-3.45.1.0.jar (
    echo Downloading SQLite JDBC...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar' -OutFile 'lib\sqlite-jdbc-3.45.1.0.jar'"
)
if not exist lib\slf4j-simple-2.0.7.jar (
    echo Downloading SLF4J Simple...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.7/slf4j-simple-2.0.7.jar' -OutFile 'lib\slf4j-simple-2.0.7.jar'"
)

echo Compiling with classpath: .;lib\*
javac -d build\classes -cp ".;lib\*" src\main\java\com\ethiotour\model\*.java ^
           src\main\java\com\ethiotour\util\*.java ^
           src\main\java\com\ethiotour\config\*.java ^
           src\main\java\com\ethiotour\service\*.java ^
           src\main\java\com\ethiotour\controller\*.java ^
           src\main\java\com\ethiotour\view\*.java ^
           src\main\java\com\ethiotour\EthioTourApp.java ^
           src\main\java\com\ethiotour\DemoApp.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
    echo.
    echo Copying configuration files...
    if not exist build\classes\resources mkdir build\classes\resources
    copy /Y src\main\resources\database.properties build\classes\resources\database.properties >nul
    copy /Y src\main\resources\seed_data.sql build\classes\resources\seed_data.sql >nul
    
    echo Starting EthioTour Connect...
    echo.
    java --enable-native-access=ALL-UNNAMED -cp "build\classes;build\classes\resources;lib\*" com.ethiotour.EthioTourApp
) else (
    echo Compilation failed. Please check the errors above.
)
pause