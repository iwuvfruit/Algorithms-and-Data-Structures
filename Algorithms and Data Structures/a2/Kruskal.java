import java.util.*;

public class Kruskal{

    public static WGraph kruskal(WGraph g){
    	WGraph MSTGraph = new WGraph();
    	DisjointSets p = new DisjointSets(g.getNbNodes());
    	ArrayList<Edge> edges = g.listOfEdgesSorted();
    	
    	for(int i = 0; i < edges.size(); i++) {
    		Edge edge = edges.get(i);
    		int node1 = edge.nodes[0];
    		int node2 = edge.nodes[1];
    				
    		if(IsSafe(p, edge)) {
    			p.union(node1, node2);
    			MSTGraph.addEdge(edge);
    		}
    	}

        /* Fill this method (The statement return null is here only to compile) */
        
        return null;
    }

    public static Boolean IsSafe(DisjointSets p, Edge e){
    	int leftNode = e.nodes[0];
    	int rightNode = e.nodes[1];
    	
    	if(p.find(leftNode) == p.find(rightNode)) {
    		return false;
    	}    	
        return true;
    
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        WGraph t = kruskal(g);
        System.out.println(t);

   } 
}
