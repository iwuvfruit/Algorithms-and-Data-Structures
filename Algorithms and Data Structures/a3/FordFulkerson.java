import java.io.*;
import java.util.*;




public class FordFulkerson {

	
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> Stack = new ArrayList<Integer>();
		// YOUR CODE GOES HERE
		Stack<Integer> stackHelper = new Stack<Integer>();
		HashSet<Integer> visited = new HashSet<Integer>();
		
		stackHelper.push(source);
		while(true) {
			if(stackHelper.peek() == destination) {
				Stack.add(destination);
				break;
			}
			Integer point = stackHelper.pop();
			if(!visited.contains(point)) {
				visited.add(point);
				Stack.add(point);
			}
			ArrayList<Integer> adj = getAdjacents(point, graph, visited);
			for(Integer a: adj) {
				stackHelper.push(a);
				Stack.add(a);
			}
		}
		
		return Stack;
	}
	public static ArrayList<Integer> getAdjacents(int point, WGraph graph, HashSet<Integer> seen){
		ArrayList<Integer> adj = new ArrayList<Integer>();
		for(Edge e : graph.getEdges()) {
			if(e.nodes[0] == point && e.weight != 0 && !seen.contains(e.nodes[1])) {
				adj.add(e.nodes[1]); 
			}
		}

		return adj;	
	}
	
	
	public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath){
		String answer="";
		String myMcGillID = "26000000"; //Please initialize this variable with your McGill ID
		int maxFlow = 0;
		
				/* YOUR CODE GOES HERE
		//
		//
		//
		//
		//
		//
		//
		*/
		
		
		answer += maxFlow + "\n" + graph.toString();	
		writeAnswer(filePath+myMcGillID+".txt",answer);
		System.out.println(answer);
	}
	
	
	public static void writeAnswer(String path, String line){
		BufferedReader br = null;
		File file = new File(path);
		// if file doesnt exists, then create it
		
		try {
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line+"\n");	
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	 public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
		 fordfulkerson(g.getSource(),g.getDestination(),g,f.getAbsolutePath().replace(".txt",""));
	 }
}
