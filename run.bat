@echo off  
echo Compiling EthioTour Connect...
if not exist build\classes mkdir build\classes

javac -d build\classes -cp . src\main\java\com\ethiotour\model\*.java ^
           src\main\java\com\ethiotour\util\*.java ^
           src\main\java\com\ethiotour\service\*.java ^
           src\main\java\com\ethiotour\controller\*.java ^
           src\main\java\com\ethiotour\view\*.java ^
           src\main\java\com\ethiotour\EthioTourApp.java ^
           src\main\java\com\ethiotour\DemoApp.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Starting EthioTour Connect...
    java -cp "build\classes" com.ethiotour.EthioTourApp
) else (
    echo Compilation failed. Please check the errors above.
)
pause