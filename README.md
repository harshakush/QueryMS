QueryMS
=======

First checkin
Problem statement: 
Given a directed labelled graph in form of a knowledge base, and a query, write a parser which can return the edges and/or nodes requested in the query. (Knowledge base and queries are not case sensitive)
Knowledge base is given as input in a text file (input.txt). Junit test cases are also provided to verify output (src/testparser.java). 


And the corresponding knowledge base for this DAG is :
"(<Shelden>, <hasFriend>, <Raj>)"
"(<Shelden>, <hasFriend>, <Leonard>)"
"(<Shelden>, <worksAt>, <Caltech>)"
"(<Leonard>, <worksAt>, <Caltech>)"
"(<Raj>, <worksAt>, <Caltech>)"
"(<Raj>, <age>,“30”)"
