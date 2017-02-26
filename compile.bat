mkdir bin
javac -cp .;lib\gson-2.8.0.jar -d bin src\*.java
javac -cp .;bin;lib\hamcrest-core-1.3.jar;lib\junit-4.12.jar -d bin src\test\*.java
