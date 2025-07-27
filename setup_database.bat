@echo off
echo Setting up database...

REM Create database if it doesn't exist
echo Creating database event_ticket_booking
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS event_ticket_booking;"

REM Import schema
echo Importing database schema
mysql -u root -p event_ticket_booking < sql/db_schema.sql

echo Database setup complete.
pause 