# DQL
[![Build Status](https://travis-ci.org/scalahub/DQL.svg?branch=master)](https://travis-ci.org/scalahub/DQL)

DQL is a Domain Specific Language for querying a database of facts (also called a table). Internally, it uses Datalog as its query engine, and is short for Data(log) Query Language.
	
A DQL query consists of either `def` or `find` statements. A few example of DQL queries are given below:

    def #sql as #Invoke where {@method ~ '*Query' and @class = 'java.sql.Statement'}
    def #web as #Invoke where {@method = 'getParameter' and @class ~ '*Servlet*'}
    def #SQLInjection as #DataFlow where {@source = #web and @dest = #sql}
    find #SQLInjection

The complete grammar of DQL is given [here](https://github.com/scalahub/DQL/blob/master/DSLToXDSL/src/main/scala/DSL.g "here").

# Mapping 

 By default, a find query returns rowIDs of the matching rows. 
 Therefore, a query such as `find #SQLInjection` would return something that looks like the following:
 
	// query: find #SQLInjection
    temp2(stmtID1, stmtID2).
    temp2(stmtID4, stmtID10).
 
(where the columns in the tuple define the statement IDs matching the query)
 
Mappings define how to map the above IDs to meaningful values. An example of mapping would be:

	map :srcCode as $stmtID => #Statement.@lineNo,@fileName, 
	                $stmtID => #Statement.@lineNo,@fileName;
	
We can then use the mapping in a find query as: 

    find #SQLInjection:srcCode

The query would return something like:

    // query: find #SQLInjection:srcCode
    // (lineNo, fileName, <>, lineNo, fileName)
    temp2(12, 'WebController.java', '<>', 14, 'UserType.java').
    temp2(16, 'OrderLoader.java', '<>', 19, 'OrderProcess.java').
	
The complete grammar of Mapping is given [here](https://github.com/scalahub/DQL/blob/master/DSLToXDSL/src/main/scala/Mapping.g "here").
# DQL Implementation
DQL compiler is written in Scala using ANTLR3 and works as follows:

1. First it converts DQL to an intermediate representation called XDSL (the X stands for XML)
2. XDSL is validated against the initial domain specific configuration. 
3. XDSL is converted to Datalog to be used by a Datalog engine.

# Building DQL

To build DQL yourself (in Linux), clone the repo and install:

	1. JDK 11+
	3. SBT

**Important**: JDK below 11 are no longer supported.  

Then change to the directory DQL and give the command:
	
	sbt package

# DQL Demo

Do the following steps to get a hand-on session with DQL using the DQL console. This shows you how to 
1. Define a basis table.
2. Populate the table from a CSV file
3. Define a mapping.
4. Issue find queries


Start DQL console by typing  
```
sbt run
```
Inside the console that opens up, type `help` help to see available commands. Type `exit` to exit the console anytime. Type 
`run myScript.dql` to invoke the command `run` with parameter `myScript.dql`, the name of a file containing a list of commands to execute. The demo file `myScript.dql` contains the following text:
```
load basis users.basis // load the basis from the file
basis // show the basis

// The file `users.csv` contains sample data above table. Ensure that this file exists.
!cat users.csv // ! executes a shell command. In this case, it displays users.csv
load #Users users.csv  // Load the table using the console command:

map :userName as $userID => #Users.@firstName; // Define a mapping called ':userName'
// Each mapping name must be prefixed by ':'. This peculiar syntax makes the parser simpler

// Issue 'find' queries and test responses
find #Users //  without any result mapping 
find #Users:userName //  with mapping userName
find #Users:userName where {@firstName ~ 'A*'} //  with mapping userName and filter
```
The following is a transcript of execution using the above demo file:
```
DQL> run myScript.dql
-------
[RUN] load basis users.basis // load the basis from the file
Loaded basis from: users.basis
-------
[RUN] basis // show the basis
Users(userID:String[userID], firstName:String, lastName:String, time:Int, balance:Int, salary:Int)
-------
[RUN] // The file `users.csv` contains sample data above table. Ensure that this file exists.
-------
[RUN] !cat users.csv // ! executes a shell command. In this case, it displays users.csv

"USERID","FIRSTNAME","LASTNAME","TIME","BALANCE","SALARY"
"1","Adam","Smith","123","345","456"
"2","Alex","Brown","3323","1110","300"
"3","Bob","Mayo","1355","9292","499"
"4","James","Brian","6757","390505","267"
"5","John","Nash","7887","790864","500"
"6","Alice","Baker","4849","44272","650"
-------
[RUN] load #Users users.csv  // Load the table using the console command:
loaded 5 facts into table Users
-------
[RUN] map :userName as $userID => #Users.@firstName; // Define a mapping called ':userName'
Ok
-------
[RUN] // Each mapping name must be prefixed by ':'. This peculiar syntax makes the parser simpler
-------
[RUN] // Issue 'find' queries and test responses
-------
[RUN] find #Users //  without any result mapping
// no mapping
temp1('1').
temp1('2').
temp1('3').
temp1('4').
temp1('5').
temp1('6').
[6 rows]

-------
[RUN] find #Users:userName //  with mapping userName
// map :userName as $userID => #Users.@firstName;
temp2('Adam').
temp2('Alex').
temp2('Bob').
temp2('James').
temp2('John').
temp2('Alice').
[6 rows]

-------
[RUN] find #Users:userName where {@firstName ~ 'A*'} //  with mapping userName and filter
// map :userName as $userID => #Users.@firstName;
temp2('Adam').
temp2('Alex').
temp2('Alice').
[3 rows]

DQL>
```

The script `myScript2.dql` contains some more examples, such as how to define the basis using the console.

For more information and syntax of DQL, see the documentation at this [link](http://github.com/scalahub/DQL/blob/master/docs/dsl.pdf "link").

# Projects related to DQL

The following projects are related to DQL:

1. DSLToXDSL:  converts DQL to XDSL 
2. XDSLLoader: loads XDSL into memory
3. XDSLToDatalog: converts loaded XDSL to Datalog
4. DatalogSolver: process a Datalog query
5. DQLAnalyzer: connects the above projects together
6. DQLDemo: full demo of DQL query engine
