NetGen readme file

to compile:
javac Filter.java  IO.java  Main.java Network.java  SemanticPair.java  Tokenizer.java
to create a jar file
sudo jar cfmv ../assets/NetGen/NetGenL4.jar MANIFEST.MF *.class

To run from CLI:
java Main <string: file to process> <string: stopword file> <int:0/1 to run frequency removal filter> <int: min freq> <int: max frequency> <window size>

