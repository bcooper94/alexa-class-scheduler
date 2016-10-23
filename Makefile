all: run

compile:
	javac -Xlint -d ./build -classpath .:./lib/junit-4.12.jar ./src/*.java

run: compile
	java -classpath ./build:./lib/sqlite-jdbc-3.14.2.1.jar:./lib/hamcrest-core-1.3.jar:./junit-4.12.jar Main

test: compile
	java -classpath ./build:./lib/sqlite-jdbc-3.14.2.1.jar:./lib/hamcrest-core-1.3.jar:./lib/junit-4.12.jar org.junit.runner.JUnitCore Tests

clean:
	rm ./build/* app.db   
