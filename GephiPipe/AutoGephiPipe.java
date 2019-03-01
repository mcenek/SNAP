//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.util.Arrays.sort;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.dynamic.api.DynamicController;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.UndirectedGraph;
//import org.gephi.graph.api.DirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.io.processor.plugin.DynamicProcessor;
import org.gephi.layout.plugin.circularlayout.circlelayout.CircleLayout;
import org.gephi.layout.plugin.circularlayout.radialaxislayout.RadialAxisLayout;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.Modularity;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.filters.plugin.graph.EgoBuilder.EgoFilter;
import org.gephi.filters.plugin.operator.INTERSECTIONBuilder.IntersectionOperator;
import org.gephi.filters.plugin.partition.PartitionBuilder.NodePartitionFilter;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import java.util.concurrent.CountDownLatch;

import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;

/**
 *
 * @author motion411
 */
public class AutoGephiPipe
{
    private static ProjectController pc;
    private static Workspace workspace;
    private static GraphModel graphModel; 
    private static AttributeModel attributeModel;
    private static ImportController importController;
    private static Graph graph;
    private static String processedFile;
    private static DynamicProcessor dynamicProcessor;
    private static Partition partition;
    private static Container container;
    public static Scanner read;
    public static String year, month,day;
    public static String sizeNodesBy;
    public static String dates[]=new String[3000];
    public static int dateCounter=0;
    public static double modResolution=0.4;
    
    
    //fileDate is a regex for extracting the date of each imported file and uses it to append as TimeInterval
    private static String fileDate="^[a-zA-Z0-9/*-]+((([0-1][0-9]{3})|([2][0][0-9]{2}))[-](([0][1-9])|([1][0-2]))[-](([0][1-9])|([1-2][0-9])|([3][0-1]))+).*";
    public static String interiorDate="-?\\d+"; 
    
    public static void initialize()//Initialize a project and a workspace
    {
        //Project must be created to use toolkit features
        //Creating a new project creates a new workspace, Workspaces are containers of all data
        pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        workspace = pc.getCurrentWorkspace();
       
        importController = Lookup.getDefault().lookup(ImportController.class);
        graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
         //Initialize the DynamicProcessor - which will append the container to the workspace
        dynamicProcessor = new DynamicProcessor();
        dynamicProcessor.setDateMode(true);    //Set 'true' if you set real dates (ex: yyyy-mm-dd), it's double otherwise
        dynamicProcessor.setLabelmatching(true);   //Set 'true' if node matching is done on labels instead of ids       
        
        graph = graphModel.getGraph();
        graph.readUnlockAll();
    }
    
    public static void radialAxLayout()//--Runs Radial Axis layout, each Spire represents a separate community.
    {
        RadialAxisLayout radLayout = new RadialAxisLayout(null,1.0,false);
        radLayout.setGraphModel(graphModel);
        radLayout.resetPropertiesValues();
        radLayout.setScalingWidth(1.0);      
        //Node placement
        ///Makes each Spar a seperate Modularity Class
        radLayout.setNodePlacement(Modularity.MODULARITY_CLASS+"-Att");
        ///Nodes are positioned in spar by these selected measures
        if(sizeNodesBy=="Betweenness")
        {
            radLayout.setSparNodePlacement(GraphDistance.BETWEENNESS+"-Att");  
        }
        else if(sizeNodesBy=="Closeness")
        {
            radLayout.setSparNodePlacement(GraphDistance.CLOSENESS+"-Att");
        }
        else if(sizeNodesBy=="Degree")//--Currently 
        {
            try{
                 radLayout.setSparNodePlacement(Ranking.DEGREE_RANKING+"-Att");//Currently does nothing.
            }
            catch(Exception e){
                e.printStackTrace();
                return;
            }
        }
        radLayout.setSparSpiral(true);
        radLayout.setSparOrderingDirection(Boolean.FALSE);
        radLayout.setKnockdownSpars(Boolean.FALSE);
       
        
        radLayout.initAlgo();//start algorithm
        for(int i=0; i<100 && radLayout.canAlgo(); i++)
        {
            radLayout.goAlgo();
        }
    }
    public static void circLayout()
    {
        CircleLayout circLayout=new CircleLayout(null,1.0,true);
      //  circLayout.
        circLayout.setGraphModel(graphModel);
        circLayout.resetPropertiesValues();
        circLayout.setNodePlacementNoOverlap(Boolean.TRUE);
        //circLayout.setNodePlacement(Modularity.MODULARITY_CLASS+"-Att");
        circLayout.setNodePlacement(GraphDistance.BETWEENNESS+"-Att");
        //circLayout.
        circLayout.initAlgo();
        for(int i=0; i<1000 && circLayout.canAlgo(); i++)
        {
            circLayout.goAlgo();
        }
    }
    public static void yifanHuLayout(){
        YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
        layout.setGraphModel(graphModel);
        layout.initAlgo();
        layout.resetPropertiesValues();
        layout.setOptimalDistance(200f);

        for (int i = 0; i < 1000 && layout.canAlgo(); i++) {
           layout.goAlgo();
        }
        layout.endAlgo();
    }
    public static void forceAtlasLayout()
    {
        ForceAtlasLayout layout = new ForceAtlasLayout(null);
        layout.setGraphModel(graphModel);
        layout.initAlgo();
        layout.resetPropertiesValues();

        for (int i = 0; i < 1000 && layout.canAlgo(); i++) {
           layout.goAlgo();
        }
        layout.endAlgo();
    }

    public static void sizeNodes()//---Resize nodes by centrality measures or degree, further categories can be added later
    {
        //Get Centrality and then size nodes by measure
        GraphDistance distance = new GraphDistance();
        distance.setDirected(false);
        distance.execute(graphModel, attributeModel);
        //Size by Betweeness centrality
        RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
        Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
        //---Default to Size by Betweenness----------//
        AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
        
        //---Set Size by different centralities base on input---------//
        if(sizeNodesBy=="Betweenness")
        {
            centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
        }
        else if(sizeNodesBy=="Closeness")
        {
            centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.CLOSENESS);
        }
        //---To Do, find proper input to utilize Eigenvector---//
//        else if(sizeNodesBy=="Eigenvector")
//        {
//            centralityColumn = attributeModel.getNodeTable().getColumn(EIGENVECTOR);
//        }
        Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
        AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
        
        sizeTransformer.setMinSize(20);
        sizeTransformer.setMaxSize(100);
        rankingController.transform(centralityRanking,sizeTransformer);
        //-----Seperate case since not compatible with centrality column----------//
        if(sizeNodesBy=="Degree" )
        {
             rankingController.transform(degreeRanking,sizeTransformer);     
        }  
    }
    
    public static void colorByCommunity()
    {
        
            ///Color by Community but running modularity measures
            PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
            System.out.println("Passed Partion Controller");
             //Run modularity algorithm - community detection
            Modularity modularity = new Modularity();
            if(modularity!=null)
            {
                System.out.println("Passed New Modularity");
            }
            
            modularity.setResolution(modResolution);
            modularity.setUseWeight(true);
            modularity.setRandom(true);
            System.out.println("Passed Modularity Resolution");
            try
            {
                modularity.execute(graphModel, attributeModel);
            }
            catch(RuntimeException e){
                e.printStackTrace();
                System.out.println("Failed Modularity Execute");
                
            }
            
            System.out.println("Passed Modularity Execute");

            //Partition with 'modularity_class', just created by Modularity algorithm
            AttributeColumn modColumn = attributeModel.getNodeTable().getColumn(Modularity.MODULARITY_CLASS);
            System.out.println("Passed Modularity Column");
            //AttributeColumn timeColumn=attributeModel.getNodeTable().
            partition = partitionController.buildPartition(modColumn, graph);
            System.out.println("Passed Modularity Partition");
            System.out.println(partition.getPartsCount() + " Communities found");
            NodeColorTransformer nodeColorTransformer2 = new NodeColorTransformer();
            nodeColorTransformer2.randomizeColors(partition);
            partitionController.transform(partition, nodeColorTransformer2);
        
        
    }
    // UNused should be deleted!
    public static void runModularity()
    {
        ///Color by Community but running modularity measures
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
         //Run modularity algorithm - community detection
        Modularity modularity = new Modularity();
        modularity.setResolution(modResolution);
        modularity.execute(graphModel, attributeModel);
    
    }
    
    public static void circularStarLayout()
    {
        int communityCount;
        try
        {
            communityCount=partition.getPartsCount();
        }
        catch(Exception e){
            e.printStackTrace();
            return;
        }
        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
    
        NodePartitionFilter partitionFilter = new NodePartitionFilter(partition);
        Query query2;// Queries are ran on filtered partitions
        GraphView view2;
        
        double rankingByPercent[] = new double[communityCount];
        
        int placementQuadrant[]=new int[communityCount];
        float shifts[]={1,1,1,1,1,1,1,1,1,1,1,1};//As community's are placed in quadrants, increment these shifts as multipliers of placement
        for(int i=0; i<=communityCount-1;i++)
        {
            partitionFilter.unselectAll();
            partitionFilter.addPart(partition.getPartFromValue(i));//Makes a community Active
            
            try
            {
                graph.readUnlockAll();
                graphModel=graph.getGraphModel();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return;
            }
            query2 = filterController.createQuery(partitionFilter);
            view2 = filterController.filter(query2);
            graphModel.setVisibleView(view2);
            //Assigns Partition Node Percentage to Array for sorting
            for(Node n : graphModel.getGraphVisible().getNodes())
            {
                rankingByPercent[i]=partitionFilter.getPartition().getPart(n).getPercentage();
            }   
        }
        //sort(rankingByPercent);
        double sortedPercent []=new double[rankingByPercent.length];
        for(int i=0; i<sortedPercent.length;i++)
        {
            sortedPercent[i]=rankingByPercent[i];
        }
        
        sort(sortedPercent);//Orders from smallest to largest percentage of nodes
        for(int i =0; i<rankingByPercent.length;i++)
        {
            for(int j=0;j<sortedPercent.length;j++)
            {
                if(sortedPercent[j]==rankingByPercent[i])
            //Match the percentages, and store the index, so the the indexes reflect smallest to largest
                {
                  //  System.out.println("Swapped "+ sortedPercent[j]+" for " + i);
                    sortedPercent[j]=i;
                    break;   
                }
            }
        }
        int ordering []=new int [sortedPercent.length];
//        for(int i=0; i<sortedPercent.length;i++)// debug
//        { 
//            System.out.println("Sorted percent "+i+" Percentage "+sortedPercent[i]);
//            System.out.println("Ranking By Percent Partition "+sortedPercent[i]+" Percentage " +rankingByPercent[(int)sortedPercent[i]]);
//        }
        for(int i=0; i<=ordering.length-1; i++)//Assign Quadrants for placement of communities
        {
          //  ordering[i]=1;i++;
            if(i<ordering.length){
                ordering[i]=1;i++;
            }
            if(i<ordering.length){
                ordering[i]=2;i++;
            }
            if(i<ordering.length)
            {
                ordering[i]=3;i++;
            }
            if(i<ordering.length){
                ordering[i]=4;i++;
            }
            if(i<ordering.length){
                ordering[i]=5;i++;
            }
            if(i<ordering.length)
            {
                ordering[i]=6;i++;
            }
            if(i<ordering.length){
                ordering[i]=7;i++;
            }
            if(i<ordering.length){
                ordering[i]=8;i++;
            }
        }
        for(int i=sortedPercent.length-1; i>=0; i--)//Array is sorted smallest to largest so iterate from largest to smallest for community placement
        // for(int i=0; i<=sortedPercent.length-1; i++)//Array is sorted smallest to largest so iterate from largest to smallest for community placement
        {
            try
            {
                partitionFilter.unselectAll();
                partitionFilter.addPart(partition.getPartFromValue((int)sortedPercent[i]));//Makes a community Active
                //System.out.println("Node Count: "+partitionFilter.getCurrentPartition().getElementsCount());
                //System.out.println("Node Count: "+partitionFilter.getCurrentPartition().getPartsCount());

                query2 = filterController.createQuery(partitionFilter);
                view2 = filterController.filter(query2);
                graphModel.setVisibleView(view2);
                circLayout();
                float commCountFloat=(float)communityCount;
                float percentage=(float) i/commCountFloat;
               // System.out.println("Percentage: "+percentage);
                float placementDegree=percentage*360;
                float placementRadians=(float) (placementDegree*3.14159265359)/180;
                //Dependending on the ordering, nodes are shifted over so they do not overlap, 
                //this loop increments the shift multipliers
                if(ordering[i]==1)
                {
                       shifts[0]=shifts[0]+1;
                }
                else if(ordering[i]==2)
                {
                       shifts[1]=shifts[1]+1;
                }
                if(ordering[i]==3)
                {
                       shifts[2]=shifts[2]+1;
                }
                if(ordering[i]==4)
                {
                       shifts[3]=shifts[3]+1;
                }
                if(ordering[i]==5)
                {
                       shifts[4]=shifts[4]+1;
                }
                if(ordering[i]==6)
                {
                       shifts[5]=shifts[5]+1;
                }
                if(ordering[i]==7)
                {
                       shifts[6]=shifts[6]+1;
                }
                if(ordering[i]==8)
                {
                       shifts[7]=shifts[7]+1;
                }
                //Apply shifting to groups of nodes as calculated above.
                for (Node n : graphModel.getGraphVisible().getNodes().toArray())    
                {
                    if(ordering[i]==1)
                    {
                        n.getNodeData().setX((n.getNodeData().x()+1000*shifts[0]));
                        n.getNodeData().setY((n.getNodeData().y()+1000*shifts[0]));
                    }
                    else if(ordering[i]==2)
                    {
                        n.getNodeData().setX((n.getNodeData().x()+1000*shifts[1]));
                        n.getNodeData().setY((n.getNodeData().y()-1000*shifts[1]));
                    }
                    else if(ordering[i]==3)
                    {
                        n.getNodeData().setX((n.getNodeData().x()-1000*shifts[2]));
                        n.getNodeData().setY((n.getNodeData().y()-1000*shifts[2]));
                    }
                    else if(ordering[i]==4)
                    {
                        n.getNodeData().setX((n.getNodeData().x()-1000*shifts[3]));
                        n.getNodeData().setY((n.getNodeData().y()+1000*shifts[3]));
                    }
                    else if(ordering[i]==5)
                    {
                        n.getNodeData().setX((n.getNodeData().x()+1000*shifts[4]));
                        n.getNodeData().setY(n.getNodeData().y());
                    }
                    else if(ordering[i]==6)
                    {
                        n.getNodeData().setX(n.getNodeData().x());
                        n.getNodeData().setY(n.getNodeData().y()+1000*shifts[5]);
                    }
                    else if(ordering[i]==7)
                    {
                        n.getNodeData().setX((n.getNodeData().x()-1000*shifts[6]));
                        n.getNodeData().setY(n.getNodeData().y());
                    }
                     else if(ordering[i]==8)
                    {
                        n.getNodeData().setX(n.getNodeData().x());
                        n.getNodeData().setY(n.getNodeData().y()-1000*shifts[7]);
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return;
            }
        }
        
        partitionFilter.selectAll();//Make all circles of nodes reappear
        
        query2 = filterController.createQuery(partitionFilter);
        view2 = filterController.filter(query2);
        graphModel.setVisibleView(view2);

    }
   
    public static void importGraph(File file)//////imports graph and appends it to existing graph
    {
        Pattern p=Pattern.compile(fileDate);//used to extract FileDate from imported graph
        try//place imported data into container
        {
            String fileName=file.getName();
            
            
            String[] tokens = fileName.split("\\.(?=[^\\.]+$)");///Eliminate input extension for use in output name.
            processedFile=tokens[0];///Used for export file name
            //Searches file name for a proper date and then appends the date as a time interval
            Matcher m = p.matcher(fileName);
            if (m.find()) 
            {
                //Set date for this file
                System.out.println(fileName);
                System.out.println("Date In "+ m.group(1)+" Length: " +m.group(1).length());
                if(isDate(m.group(1))==true)
                {
                    dates[dateCounter]=m.group(1);//-- Gathers Dates of files to be printed to text file for later use
                    dateCounter++;
                    System.out.println("File imported: " +file.toString());
                    container = importController.importFile(file);
                    if(container==null){
                        System.out.println("Container is null");
                    }
                    container.getLoader().setEdgeDefault(EdgeDefault.UNDIRECTED);//set to undirected
                    System.out.println(fileName+" was appended to graph.");
                    dynamicProcessor.setDate(m.group(1));///Set time interval
                    System.out.println("Date Set: " +dynamicProcessor.getDate());
                    importController.process(container, dynamicProcessor, workspace);
                    
//                    dates[dateCounter]=newDate;//-- Gathers Dates of files to be printed to text file for later use
//                    dateCounter++;
//                    System.out.println("File imported: " +file.toString());
//                    container = importController.importFile(file);
//                    container.getLoader().setEdgeDefault(EdgeDefault.UNDIRECTED);//set to undirected
//                    System.out.println(fileName+" was appended to graph.");
//                    dynamicProcessor.setDate(newDate);///Set time interval
//                    System.out.println("Date Set: " +dynamicProcessor.getDate());
//                    //Process the container using the DynamicProcessor
//                    importController.process(container, dynamicProcessor, workspace);
                }
                else
                {
                    System.out.println("Error, File "+processedFile+" was not appended to graph due to improper File date");
                }
            }
            else//If the Date was not in the file name, attempt to grab date from file itself by findiing the first three integers in file
            {
                System.out.println("Invalid date in File name, searching .dl for date");
                int count=0;
                try
		{
			read=new Scanner(file);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
                while(read.hasNext())
                {
                    String temp=read.next();
                    p=Pattern.compile(interiorDate);
                    m=p.matcher(temp);
                    if(m.find())
                    {
                        count++;
                        if(count==1 && temp.length()==4)
                        {
                            year=temp;
                        }
                        else if(count==2)
                        {
                            if(temp.length()==1)//if date is 2008 1 1, must be converted to 2008 01 01 so that it is compatible as time interval
                            {
                                temp="0"+temp;
                                month=temp;  
                            }
                            else
                            {
                                month=temp;  
                            } 
                        }
                        else if(count==3)
                        {
                            if(temp.length()==1)//if date is 2008 1 1, must be converted to 2008 01 01 so that it is compatible as time interval
                            {
                                temp="0"+temp;
                                day=temp; 
                            }
                            else
                            {
                                day=temp;
                            }
                            break;
                        }  
                    }  
                }
                System.out.println(year+"-"+month+"-"+day);
                String newDate=year+"-"+month+"-"+day;
                if(isDate(newDate)==true)
                {
                    
                    dates[dateCounter]=newDate;//-- Gathers Dates of files to be printed to text file for later use
                    dateCounter++;
                    System.out.println("File imported: " +file.toString());
                    container = importController.importFile(file);
                    container.getLoader().setEdgeDefault(EdgeDefault.UNDIRECTED);//set to undirected
                    System.out.println(fileName+" was appended to graph.");
                    dynamicProcessor.setDate(newDate);///Set time interval
                    System.out.println("Date Set: " +dynamicProcessor.getDate());
                    //Process the container using the DynamicProcessor
                    importController.process(container, dynamicProcessor, workspace);
                    
                }
                else
                {
                    System.out.println("Error, File "+processedFile+" was not appended to graph due to improper File date");
                }    
            }   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            
        }
        
    }
     
    public static void importDirectory(String dirName)//Takes in an entire Directory of files and attempts to import each one of them,
            //If the import file is determined to not be a directory, a simple single file import is called.
    {
     
        File tempFile=new File(dirName);
        if(tempFile.isDirectory()==true)
        {
            File[] files = new File(dirName).listFiles();
            System.out.println(dirName);

            for (int i = 0; i <= files.length - 1; i++) {
                try 
                {
                    importGraph(files[i]);
                } 
                catch (Exception e) 
                {
                    System.out.println(files[i].toString() + " is not a graph file");
                }
            }
            
        }
        else if(tempFile.isFile()==true)
        {
            System.out.println("Single File ");
            try
            {
                importGraph(tempFile);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }   
  
    public static void exportGraph(String dirName)//--- Exports a gexf file to be used in Gephi or Partiview, exports PDF for easy sample readability of entire graph.
    {
        
        //Set 'show labels' option in Preview - and disable node size influence on text size
        PreviewModel previewModel = Lookup.getDefault().lookup(PreviewController.class).getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.FALSE);

        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try
        {
            Path path = ((Paths.get(dirName)).getParent()).getParent(); 
            //System.out.println("writeGraphOut " + path.toString()); // processedFile +" "
            //ec.exportFile(new File("completeLayout.gexf"));
            ec.exportFile(new File(path.toString()+"/partiview_generator/"+processedFile+".pdf")); 
            ec.exportFile(new File(path.toString()+"/partiview_generator/"+processedFile+".gexf"));
            //ec.exportFile(new File(processedFile+".pdf"));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            return;
        }
    }
    
    public static void exportDates()//----Exports the discrete date of each file in a .txt file to be utilized later in Partiview
            //to discretize time intervals.
    {
        try
        {
            PrintWriter pr = new PrintWriter("FileDates.txt");    

            for (int i=0; i<dates.length ; i++)
            {
                if(dates[i]!= null)
                {
                     pr.println(dates[i]);
                }
            }
            pr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("No such file exists.");
        }   
    }
    public static boolean isDate(String dateIn)
    {
        boolean isDate;
        int year;
        int month;
        int day;
        String hyphen1;
        String hyphen2;
        try
        {

            year = Integer.parseInt(dateIn.substring(0,4));
            if(year<=0 ||year >2050){
                System.out.println("No way a file has been created with that date yet");
                return isDate= false;
            }
            hyphen1=dateIn.substring(4,5);
            month = Integer.parseInt(dateIn.substring(5, 7));
            if(month<=0 ||month >12){
                System.out.println("Not a Month");
                return isDate= false;
            }
            hyphen2=dateIn.substring(7,8);
            day = Integer.parseInt(dateIn.substring(8, 10));
            if(day<=0 ||day >31){
                System.out.println("Out of range of days in months");
                return isDate= false;
            }
            isDate=true;
        }
        catch(Exception e)
        {
            isDate= false;
        }   
        return isDate;
    }
    
    public static String getSizeNodesBy(){
        return sizeNodesBy;
    }
    public static void setSizeNodesBy(String size)
    {
        sizeNodesBy=size;
    }
    public static double getModResolution()
    {
        return modResolution;
    }
    public static void setModResolution(double resIn)
    {
        if(resIn > 0 && resIn <= 1.0)
        {
            modResolution=resIn;
        }
        else//reset to default
        {
            modResolution=0.4;
        }
    }
    
}
