# MeSQL

## Introduction

MeSQL is an extendable command-line-only in-memory database management system, written in Java. The project is purely for educational and informational purposes.

Some features are below (demonstrated in `Examples.sql`):

* The keywords are case-insensitive;
* It supports float point numbers (e.g. 107.1f) and long integers (e.g 999999999999L);
* It uses the string type covering all visible ASCII characters;
* Two comment styles are found;
* ...

The following process is applied to Linux/Unix systems only:

* Install Java Compiler-Compiler (JavaCC) first; if one works on Ubuntu, try `sudo apt install javacc`;
* Run the tree-building preprocessor by `jjtree ./src/shell/parser/MeSQL.jjt`;
* Then run the parser by `javacc ./src/shell/parser/MeSQL.jj`;
* Finally compile the project by `javac ./src/Shell.java`;
