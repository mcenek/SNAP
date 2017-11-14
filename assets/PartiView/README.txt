PartiGen.jar readme

Place PartiView Generator (PartiGen.jar) jar file into this directory

Examples of different commands to call the jar:

java -jar PartiGen.jar file.gexf dateFile.txt
java -jar PartiGen.jar file.gexf dateFile.txt 60 1 1 10 solid 

Run the jar using:

java -jar PartiGen.jar file.gexf[.gexf] dateFile.txt[.txt] *range[int](91) *xSkew[int](1) *ySkew[int](1) *zSkew[int](1) *shape[wire/solid/point](wire) *individualGEXFFolder[path]

* represents an optional field. So the only required argument are the first two. The .gexf file and the date text file.

[type] represents the type of argument needed.

(default values) represents the default value it's set at. so range is set to a default value of 91 and the skews are set to 1.

Argument breakdown:

file.gexf[.gexf]
This is the first argument and is a required argument. This is a data file which contains nearly all the information to be imported.

dateFile.txt[.txt]
This is the second argument and is a required argument. This is a text file containing all the dates that the data should be using.

*range[int](91)
This is the third argument and is an optional argument. It has a default value of 91. This is the range limit for which the layers will connect. If two layers are more than the provided number of days apart then a mesh will not be formed between the two layers.

*xSkew[int](1) *ySkew[int](1) *zSkew[int](1)
This is the fourth, fifth, and sixth argument respectively. They are all required and come with a default value of 1. They represent how the 3D model will be transformed. So if the x is set to 2 then the 3D model will be stretched twice as long on the x axis. If the y is set to 10 then the y axis will be stretched by 10 times what it would normally be.

*shape[wire/solid/point](wire)
This is the seventh argument and is an optional argument. It has a default value of "wire". wire will create the 3D model with a wire outline. solid will create the 3D model with solid transparent sides. point will create the 3D model with points at the verticies.

*individualGEXFFOLDER[path]
This is the eigth argument and is and optional argument. It takes the path of the folder containing individual gexfs (.gexf).
