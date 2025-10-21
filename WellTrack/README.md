Health & Wellness Assistant (Swing Java Desktop Application)
===========================================================

Overview
--------
This is a desktop Java Swing application that combines a BMI tracker and a medicine reminder module.
Features:
- Calculate BMI and save health history (CSV)
- Add medicine reminders; app checks every 30 seconds and shows pop-up notifications at the scheduled time
- Data persistence using CSV files in /data

How to build and run
--------------------
Requires: JDK 8+ installed.

From project root (where src/ and data/ folders are):
1. Compile:
   javac -d out $(find src -name "*.java")

   (on Windows you can use an IDE like IntelliJ or compile with a wildcard)

2. Run:
   java -cp out com.healthassistant.Main

Project structure
-----------------
- src/com/healthassistant/...  : Java source files
- data/health_history.csv      : BMI records (created automatically)
- data/reminders.csv           : Reminders (created automatically)

Notes
-----
- The reminder check uses LocalTime (HH:mm). Ensure your system clock is correct.
- UI uses Swing for cross-platform compatibility.
