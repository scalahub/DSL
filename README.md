# DQL

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

	map :srcCode as $stmtID => #Statement.@lineNo,@fileName, $stmtID => #Statement.@lineNo,@fileName;
	
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

	1. JDK 8+
	3. SBT
	
Then change to the directory DQL and give the command:
	
	sbt package

# DQL Demo

Do the following steps to get an hand-on session with DQL. 

1. Start DQL console using `sbt run`. Type `help` in the console to see available commands.

2. Define a basis table by typing the following in DQL console:
`table Users(userID:String[userID], firstName:String, lastName:String, time:Int, balance:Int, salary:Int);`

3. The file `users.csv` contains sample data for the above table. Ensure that this file is in your current folder.

4. Load the table using the console command:
`load #Users users.csv`

5. Define a mapping using:
`map :userName as $userID => #Users.@firstName;`

6. Issue find queries and test responses:
`find #Users`
`find #Users:userName`
`find #Users:userName where {@firstName ~ 'A*'}`

For more information and syntax of DQL, see the documentation at this [link](http://github.com/scalahub/DQL/blob/master/docs/dsl.pdf "link").

# Projects related to DQL

The following projects are related to DQL:

1. DSLToXDSL:  converts DQL to XDSL 
2. XDSLLoader: loads XDSL into memory
3. XDSLToDatalog: converts loaded XDSL to Datalog
4. DatalogSolver: process a Datalog query
5. DQLAnalyzer: connects the above projects together
6. DQLDemo: full demo of DQL query engine
