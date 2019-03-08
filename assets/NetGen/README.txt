NetGen.jar Readme

Place Network Generator (NetGen.jar) jar file into this directory

To comile the source files located in the SNAP/NetGen
1. make sure that the junit is installed and the shared resources are placed into
   Linux: /usr/share/java/junit4.jar

   "javac -cp .:/usr/share/java/junit4.jar *.java"

   Alternatively if you don't have junit installed, you can compile, but leave out the NetGenTest.java
   "javac Filter.java IO.java Main.java Network.java SemanticPair.java Tokenizer.java"

   "sudo jar cfmv ../assets/NetGen/NetGenL4.jar MANIFEST.MF *.class"

   Change the owner if needed (and permissions)
   Linux: sudo chown www-data:www-data NetGenL4.jar

Terminal command to run NetGenL4.jar:
java -jar NetGenL4.jar input.txt stopword.txt frequency* freqMin* freqMax*

input.txt: The path and name of the file you wish to create a network of.
stopword.txt: The path and name of the file containing the stop words.

*Not mandatory arguments to run the jar.

frequency: If set to 1 then frequency of word usage is used to create the network in place of position. Default is set to 0. Frequency is the inverse usage of a word and is determined by 1/n where n is equal to word usage. So if a word is used 2 times it will have a frequency of 1/2 or 50%, for a word used 4 times it will have a frequency of 1/4 or 25%.

freqMin: The minimum value a frequency should have. When set to 25 (25%) a word used less than 4 times will be removed from the network. Default set to 35 (35%).

freqMax: The maximum value a frequency should have. When set to 5 (5%) a word used more than 20 times will be removed from the network. Default set to 5 (5%).
