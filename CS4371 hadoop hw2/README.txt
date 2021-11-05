John Kenney jfk150030

First make sure text files are loaded to hdfs by checking at your localhost directory 
if they are not implement these commands in your terminal
cd to file that contains jars and txt files then

hdfs dfs -put soc-LiveJournal1Adj.txt <location of input directory on hadoop>
hdfs dfs -put userdata.txt <location of input directory on hadoop>


Problem 1:
folder problem1 contains
folder MutualFriends contains the source files in the src folder inside
folder outputmutualfriend contains the output of the program
jar file called Mutualfriends.jar

then for running problem 1's jar file to determine mutual friends
type in terminal

hadoop jar MutualFriends.jar mutualfriend <location of adj list in hadoop> <location of output in hadoop>

then to download results type in terminal 

hdfs dfs -get <location of output in hadoop>

then to delete old output folders  in your hdfs type in terminal

hdfs dfs -rm -r <location of output in hadoop>



Problem 3:
folder problem3 contains
folder AverageAgeF contains the source files in the src folder inside
folder outputFriendsAverageAge contains the output of the program
jar file called FriendsAverageAge.jar

then for running problem 3's jar file to determine the average age of friends of each user
type in terminal

hadoop jar FriendsAverageAge.jar FriendsAverageAge <location of adj list in hadoop> <location of userdata in hadoop> <location of output in hadoop>

then to download results type in terminal 

hdfs dfs -get <location of output in hadoop>

then to delete old output folders  in your hdfs type in terminal

hdfs dfs -rm -r <location of output in hadoop>



Problem 4:
folder problem4 contains
folder AgeOfFriends contains the source files in the src folder inside
folder outputFriendsAge contains the output of the program
jar file called FriendsAge.jar

then for running problem 4's jar file to determine the age and first name of every friend of a user ranked by age
type in terminal

hadoop jar FriendsAge.jar FriendsAge <location of adj list in hadoop> <location of userdata in hadoop> <location of output in hadoop>

then to download results type in terminal 

hdfs dfs -get <location of output in hadoop>

then to delete old output folders  in your hdfs type in terminal

hdfs dfs -rm -r <location of output in hadoop>




