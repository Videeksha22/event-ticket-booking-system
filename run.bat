@echo off
echo Creating database and loading schema...
echo You may need to enter your MySQL password when prompted

REM Create database if it doesn't exist
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS event_ticket_booking;"

REM Import schema
mysql -u root -p event_ticket_booking < setup_database.sql

echo.
echo Compiling Java files...

REM Create target directory if it doesn't exist
mkdir target 2>nul
mkdir target\classes 2>nul
mkdir lib 2>nul

REM Download MySQL Connector if it doesn't exist
if not exist "lib\mysql-connector-j-8.0.33.jar" (
    echo Downloading MySQL Connector...
    powershell -Command "(New-Object Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar', 'lib\mysql-connector-j-8.0.33.jar')"
)

REM Compile Java files
javac -source 1.8 -target 1.8 -d target/classes -cp "src/main/java;lib/*" src/main/java/com/ticketbooking/ui/*.java src/main/java/com/ticketbooking/service/*.java src/main/java/com/ticketbooking/dao/*.java src/main/java/com/ticketbooking/model/*.java src/main/java/com/ticketbooking/util/*.java src/main/java/com/ticketbooking/*.java

echo.
echo Running application...
REM Run the application with the Main class
java -cp "target/classes;lib/*" com.ticketbooking.Main

pause 