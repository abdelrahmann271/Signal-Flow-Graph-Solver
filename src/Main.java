import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {


        Scanner sc=new Scanner(System.in);

        System.out.println("Enter the Number of Vertices");
        int vertices= sc.nextInt();

        //Initializing Graphs.
        GraphUtil w = new GraphUtil(vertices);
        Digraph<String,String> g = new DigraphEdgeList<>();

        //Initializing Vertices.
        for(int i = 0 ; i < vertices ; i++) {
            g.insertVertex(Integer.toString(i));
        }

        System.out.println("Enter the Number of Edges");
        int edges= sc.nextInt();

        System.out.println("Enter The Edges");
        for(int i = 0 ; i < edges ; i++ ) {
            int firstV = sc.nextInt();
            int secondV = sc.nextInt();
            int edgeW =  sc.nextInt();
            w.addEdge(firstV,secondV,edgeW);
            String edge = Integer.toString(edgeW);
            for(int j = 0 ; j < i ; j++) {
                edge = edge + " ";
            }
            g.insertEdge(Integer.toString(firstV) , Integer.toString(secondV) , edge );
        }


        System.out.println("Enter The Source Node");
        int s = sc.nextInt();
        System.out.println("Enter The Destination Node");
        int d = sc.nextInt();


        w.printAllPaths(s, d);
        w.printAllLoops();
        System.out.println("Forward Paths");
        for(int i = 0 ; i < w.forwardPaths.size() ; i++) {
            System.out.println();
            System.out.println(i+1 +" th path");
            System.out.println();
            for(int j = 0 ; j < w.forwardPaths.get(i).size() ; j++) {
                System.out.println("Node " + w.forwardPaths.get(i).get(j).getKey() + " Weight of next edge " + w.forwardPaths.get(i).get(j).getValue());
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Feedback");
        for(int i = 0 ; i < w.loops.size() ; i++) {
            System.out.println("From " + w.loops.get(i).getKey() + " To " + w.loops.get(i).getValue() );
        }

        System.out.println();
        System.out.println(w.loopPaths.size() + " Loops");
        System.out.println();
        for(int i = 0 ; i < w.loopPaths.size() ; i++) {
            System.out.println();
            System.out.println(i+1 +" th loop");
            System.out.println();
            for(int j = 0 ; j < w.loopPaths.get(i).size() ; j++) {
                System.out.println("Node " + w.loopPaths.get(i).get(j).getKey() + " Weight " + w.loopPaths.get(i).get(j).getValue());
            }
            System.out.println();
        }
        w.CalculateMasonFormula();

        Platform.startup( ()-> {
            SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
            SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);
            Scene scene = new Scene(graphView, 1024, 768);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("JavaFXGraph Visualization");
            stage.setScene(scene);
            stage.show();
            graphView.init();
        });
    }
}
