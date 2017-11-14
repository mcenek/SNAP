//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package AutoGephi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main 
{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException 
    {
        System.out.println("AutoGephiPipeV3_1");
        int individualGexfs = 1;
        if(args.length >= 4){
            System.err.println("Length: "+args.length);
            individualGexfs = Integer.parseInt(args[3]);
        }
        if(individualGexfs == 1){
            System.out.println("HEReeee");
            try(Stream<Path> paths = Files.walk(Paths.get(args[0]))){
                System.out.println("AFSF");
                paths.forEach(filePath -> {
                    if(Files.isRegularFile(filePath)){
                        System.out.println(filePath.toString());
                        if(filePath.toString().endsWith(".dl")){
                            AutoGephiPipeV3.initialize();

                            AutoGephiPipeV3.importDirectory(filePath.toString());

                            AutoGephiPipeV3.setModResolution(Double.parseDouble( args[2]));

                            AutoGephiPipeV3.setSizeNodesBy("Betweenness");

                            AutoGephiPipeV3.sizeNodes();

                            AutoGephiPipeV3.colorByCommunity();

                            if(args[1].equals("0"))
                            {
                                AutoGephiPipeV3.circularStarLayout();
                            }
                            else if(args[1].equals("1"))
                            {
                                AutoGephiPipeV3.radialAxLayout();
                            }
                            else if(args[1].equals("2"))
                            {
                                AutoGephiPipeV3.yifanHuLayout();
                            }
                            else if(args[1].equals("3"))
                            {
                                AutoGephiPipeV3.forceAtlasLayout();
                            }




                            AutoGephiPipeV3.exportGraph();

                            //AutoGephiPipeV3.exportDates();
                        }
                    }
                });
            }
        }
    }
    
    
}
