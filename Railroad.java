/* Diego La Rosa
 * Dr. Steinberg
 * COP3503 Fall 2023
 * Programming Assignment 5
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

class Edge {
	String src, dest;
	int weight;
	int srcNum;
	int destNum;
    Edge(String src, String dest, int weight, int srcNum, int destNum) {
        int order = src.compareTo(dest);

        if (order < 0) {
        	this.src = src;
        	this.dest = dest;
        	this.srcNum = srcNum;
        	this.destNum = destNum;
        }else {
        	this.src = dest;
        	this.srcNum = destNum;
        	this.dest = src;
        	this.destNum = srcNum;
        }
        this.weight = weight;
    }
    
    @Override
    public String toString() {
    	return src + "---" + dest+ "\t$" + weight+"\n";
    }
}

class DisjointSet{
	int [] rank;
	int [] parent;
	int n;
	public DisjointSet(int n){
		rank = new int[n];
		parent = new int[n];
		this.n = n;
		makeSet();
	}
	// Creates n sets with single item in each
	public void makeSet()
	{
		for (int i = 0; i < n; i++)
		{
			parent[i] = i;
		}
	}
	//path compression
	public int find(int x)
	{
		if (parent[x] != x)
		{
			parent[x] = find(parent[x]);
		}
		return parent[x];
	}
	//union by rank
	public void union(int x, int y)
	{
		int xRoot = find(x), yRoot = find(y);
		if (xRoot == yRoot)
		return;
		if (rank[xRoot] < rank[yRoot])
		parent[xRoot] = yRoot;
		else if (rank[yRoot] < rank[xRoot])
		parent[yRoot] = xRoot;
		else
		{
			parent[yRoot] = xRoot;
			rank[xRoot] = rank[xRoot] + 1;
		}
	}
}


public class Railroad {
	int numT;
	ArrayList<Edge> storeEdge = new ArrayList<>();
	HashMap<String, Integer>  vertex = new HashMap<>();
	int index = 0;
	
	public Railroad(int numvertices, String file) {
		this.numT = numvertices;
		// scan and store the inputs into the character array attribute
		try {
            String filePath = file;
            Scanner scanner = new Scanner(new File(filePath));

            // Read each line, create edges, and store it in array list
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(" ");
                if (split.length == 3) {
                    String src = split[0];
                    String dest = split[1];
                    int weight = Integer.parseInt(split[2]);
                    if(!vertex.containsKey(src)) {
                    	vertex.put(src, index++);
                    }
                    if(!vertex.containsKey(dest)) {
                    	vertex.put(dest, index++);
                    }
                    storeEdge.add(new Edge(src, dest, weight, vertex.get(src), vertex.get(dest)));
                    //System.out.println( src + "\t" + dest+ "\t$" + weight+"\n");
                } else {
                    System.out.println("Error reading file");
                }
            }
		}  
		catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public String buildRailroad() {
		int cost = 0;
		String res = "";
		ArrayList<Edge> resEdge = new ArrayList<>();
		DisjointSet dis = new DisjointSet(storeEdge.size() * 2); //Making the set and 
		Collections.sort(storeEdge, Comparator.comparingInt(edge -> edge.weight));
		for(Edge edge : storeEdge ) {
			if( dis.find(edge.srcNum) !=  dis.find(edge.destNum)) {
				resEdge.add(edge);
                dis.union(edge.srcNum,  dis.find(edge.destNum));
			}
		}
		for(Edge edge : resEdge) {
			res += edge.toString();
			cost += edge.weight;
		}
		res += "The cost of the raild road is $" + cost;
		return res;
	}
}
