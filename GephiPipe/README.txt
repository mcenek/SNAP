AutoGephiPipe.jar Read Me

Implements node (token) layout for each document in the corpus. 

Must be in same folder as “lib” containing associated Gephi Toolkit Jars

To Compile:
javac -cp .:gephi-toolkit.jar:org-gephi-layout-plugin-circularlayout.jar *.java

To package into jar:
sudo jar cfmv ../assets/GephiPipe/individualGraphProcess.jar MANIFEST.MF *.class

How to run:
java -jar AutoGephiPipeV3_1.jar [Directory Path of files] [int for layout] [double for modularity resolution]

For example
java -jar AutoGephiPipeV3_1.jar ../KTUU 0 0.4


Directory Path: is the directory of .dl graph files to import and process. This can also be just a single file and not a directory.

Nodes are sized by Betweenness and colored by modularity class

Int for Layout: 
Possible values: 0, 1, 2, 3

0: Circular Star Layout - Communities of Nodes are grouped in circles spaced out from each other, ordered around circle by Betweenness centrality.
1: Radial Axis Layout - Communities of Nodes are grouped in Spires around a circle, ordered on spire by Betweenness centrality. 
2: Yifan Hu Layout
3: Force Atlas Layout

Double For Modularity resolution:

Modularity Resolution determines how many Communities are detected is a graph. Lower resolution produces more communities
Higher Resolution produces less communities
Range: 0 < resolution <= 1
Default resolution is 0.4

