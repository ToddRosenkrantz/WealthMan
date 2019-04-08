Wealth Manager - Stock Recording System
Basic Wealth Management Android app for Agile Class Project

Adapted from code downloaded from:
https://theeasylearn.blogspot.com/

Most recent changes include:
	Full Registration page
	Finish() is called after successful registration or if user select Login
	Color changed to "Dollar Bill" color... I think a darker green is needed
	Main Page - stub is called after successful login
	
	
verify sqlite3 database by connecting via adb.

In the android studio terminal or otehr command prompt:
1. Get a list of devices
	D:\workspace\WealthMan>adb devices
	List of devices attached
	emulator-5554   device

2. Start a shell on the emulator
	D:\workspace\WealthMan>adb -s emulator-5554 shell
	or combine the steps above for an emulator with
	D:\workspace\WealthMan>adb -e shell

3. Change user to the one for the app
	root@generic_x86:/ # run-as com.example.wealthman
	
4. Change to the database directory
	root@generic_x86:/ # cd /data/data/com.example.wealthman/databases
	cd /data/data/com.example.wealthman/databases
	
5. Open the database with sqlite3
	root@generic_x86:/data/data/com.example.wealthman/databases # sqlite3 register.db
	qlite3 register.db                                                            <
	SQLite version 3.8.10.2 2015-05-20 18:17:19
	Enter ".help" for usage hints.
	
6. examine the database as needed
	sqlite> .tables
	.tables
	android_metadata  registeruser
	sqlite> select * from registeruser;
	select * from registeruser;
	1|todd|t|todd@someplace.edu|2222
	sqlite>
	
7. use .exit to exit sqlite3
