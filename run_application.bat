@echo off
echo Compiling Java files...

REM Create target directory if it doesn't exist
mkdir target 2>nul

echo Compiling with classpath...
javac --release 8 -Xlint:none -d target -cp "lib\mysql-connector-j-8.0.33.jar" src\main\java\com\ticketbooking\*.java src\main\java\com\ticketbooking\dao\*.java src\main\java\com\ticketbooking\model\*.java src\main\java\com\ticketbooking\service\*.java src\main\java\com\ticketbooking\ui\*.java src\main\java\com\ticketbooking\util\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Running application...
REM Run the application
java -cp "target;lib\mysql-connector-j-8.0.33.jar" com.ticketbooking.Main

pause 