//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package AutoGephi;

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
        System.out.println("AutoGephiPipe:");
        int individualGexfs = 1;
        if(args.length >= 4){
            System.err.println("Length: "+args.length);
            individualGexfs = Integer.parseInt(args[3]);
        }
        if(individualGexfs == 1){
            System.out.println("Ind dl2gexf: "+Paths.get(args[1]));
            try(Stream<Path> paths = Files.walk(Paths.get(args[0]))){
                paths.forEach(filePath -> {
                    if(Files.isRegularFile(filePath)){
                        System.out.println(filePath.toString());
                        if(filePath.toString().endsWith(".dl")){
                            AutoGephiPipe.initialize();

                            AutoGephiPipe.importDirectory(filePath.toString());

                            AutoGephiPipe.setModResolution(Double.parseDouble( args[2]));

                            AutoGephiPipe.setSizeNodesBy("Betweenness");

                            AutoGephiPipe.sizeNodes();

                            AutoGephiPipe.colorByCommunity();

                            if(args[1].equals("0"))
                            {
                                AutoGephiPipe.circularStarLayout();
                            }
                            else if(args[1].equals("1"))
                            {
                                AutoGephiPipe.radialAxLayout();
                            }
                            else if(args[1].equals("2"))
                            {
                                AutoGephiPipe.yifanHuLayout();
                            }
                            else if(args[1].equals("3"))
                            {
                                AutoGephiPipe.forceAtlasLayout();
                            }

                            AutoGephiPipe.exportGraph(filePath.toString());
                            //AutoGephiPipe.exportDates();
                        }
                    }
                });
            }
        }
    }
    
    
}
