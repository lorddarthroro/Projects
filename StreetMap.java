//Rohaan Ahmad
//CSC 172
//Project 3
//12 December 2021

//online source used (for graph, nodes, edges): https://www.softwaretestinghelp.com/java-graph-tutorial/


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.JFrame;

 

class Edge {
    double weight;
    int source;
	int destination;
    //edge constructor
    //source represents node it comes from, destination is where it goes. Weight will be determined by finding the literal distance between points
    Edge(int source, int destination, double weight) {
    		//MAYBE CHANGE SOURCE AND DESTINATION TO STRINGS
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
}
class Graph {
	//holds dijskstra distances to each vertex from source
	double distances[];
	int pathHolder[];
	URArrayList<Integer> visitedNodes; //Holds visited nods
	private PriorityQueue<Node> pq;
	//adjacency list holds nodes of edges
	 //URArrayList is a data structure I made for lab 04, I use it in this project
	URArrayList<List<Node>> adj_list = new URArrayList<List<Node>>();
	private int numberOfVertices = 0;
	//nodes are added to the adjacency list with the destination and weight being stored, 
	//needs comparator so PQ can sort
    static class Node implements Comparator<Node>{
        int value;
        double weight;
        Node(int value, double weight)  {
            this.value = value;
            this.weight = weight;
        }
        public Node() {}
	
		public int compare(Node node1, Node node2) 
        { 
            if (node1.weight < node2.weight) 
                return -1; 
            if (node1.weight > node2.weight) 
                return 1; 
            return 0; 
        } 
    }
 
 
//takes list of edges and construct graph
public Graph(List<Edge> edges, int size){
        //intializes an array list of arraylists
        for (int i = 0; i < edges.size(); i++) {
            adj_list.add(i, new ArrayList<>());
        }
        numberOfVertices = size;
    	pq = new PriorityQueue<Node>(size, new Node());
    	distances = new double[size];
    	pathHolder = new int[size];
    	visitedNodes = new URArrayList<Integer>();
    	
 
        // add edges to the graph
        for (Edge e : edges)
        {
            //adds new node to adjacency list with value of destination and weight to it, and does it both ways since its not a directed graph
            adj_list.get(e.source).add(new Node(e.destination, e.weight));
            adj_list.get((int)e.destination).add(new Node(e.source, e.weight));
        }
    }
// print adjacency list for the graph
    public static void printGraph(Graph graph)  {
        int source_vertex = 0;
        int list_size = graph.adj_list.size();
 
        while (source_vertex < list_size) {
            //goes through each source vertex (0,1,2,3,etc) and checks each edge in the nested arraylist
            for (Node edge : graph.adj_list.get(source_vertex)) {
                System.out.print("Vertex:" + source_vertex + " ==> " + edge.value + 
                                " (" + edge.weight + ")\t");
            }
 
            System.out.println();
            source_vertex++;
        }
    }
    public static void printAdjList(Graph graph) {
    	int source_vertex = 0;
        int list_size = graph.adj_list.size();
 
        while (source_vertex < list_size) {
            //goes through each source vertex (0,1,2,3,etc) and checks each edge in the nested arraylist
            for (Node edge : graph.adj_list.get(source_vertex)) {
                System.out.print(edge.weight + " ");
            }
 
            System.out.println();
            source_vertex++;
    }
    }
    public static void drawmap(Graph graph, URArrayList<Double> longitude, URArrayList<Double> latitude, URArrayList<Double> redLong, URArrayList<Double> redLat) {
    	//find max and min of long and lat. Important for calc to convert to coordinates in frame (see MyFrame)
    	double longmax = 0;
    	double longmin = 10000;
    	double latmax = 0;
    	double latmin = 10000;
    	for(int i = 0; i<longitude.size(); i++) {
    		if (Math.abs(longitude.get(i)) > longmax) {
    			longmax = Math.abs(longitude.get(i));
    		}
    		if(Math.abs(longitude.get(i)) < longmin) {
    			longmin = Math.abs(longitude.get(i));
    		}
    		if (Math.abs(latitude.get(i)) > latmax) {
    			latmax = Math.abs(latitude.get(i));
    		}
    		if(Math.abs(latitude.get(i)) < latmin) {
    			latmin = Math.abs(latitude.get(i));
    		}
    		
    	}
    	int source_vertex = 0;
        int list_size = graph.adj_list.size();
        int destination_vertex;
        double source_xcoord;
        double source_ycoord;
        double destination_xcoord;
        double destination_ycoord;
        MyFrame f = new MyFrame(); //JFRAME (seperate file for class)
        while (source_vertex < list_size) {
            for (Node edge : graph.adj_list.get(source_vertex)) {
                destination_vertex = edge.value;
                //take abs val because the way I convert to graphics needs positie numbers
                //get coordinates of vertexes from arraylists, vertexes obtained from traversing graph
                source_xcoord = Math.abs(longitude.get(source_vertex));
                source_ycoord = latitude.get(source_vertex);
                destination_xcoord = Math.abs(longitude.get(destination_vertex));
                destination_ycoord = latitude.get(destination_vertex);
                //have getnumbers read raw coordinates
                //System.out.println(source_xcoord +" " + source_ycoord + " " + destination_xcoord + " " + destination_ycoord);
                f.getNumbers(source_xcoord,source_ycoord,destination_xcoord,destination_ycoord, longmax, longmin, latmax, latmin);
            }
            source_vertex++;
	}
        f.getSolutionPath(redLong, redLat);
    }
	
    public void dijkstra(URArrayList<List<Graph.Node>> adj, int source, int size) {
    	this.adj_list = adj;
    	//globalizes size for later call
    	 for (int i = 0; i < size; i++)
             distances[i] = Integer.MAX_VALUE;
   
         // Add source node to the priority queue
         pq.add(new Node(source, 0));
         
         //sets patholder at source to -1
         pathHolder[source] = -1;
         
         // Distance to the source is 0
         distances[source] = 0;
		 while(visitedNodes.size() != size) {
			 if(pq.isEmpty()) {
				 return;
			 }
			 int x = pq.remove().value;
			 //if node already added, continue skips next bit which isn't necessary to run
			 if(visitedNodes.contains(x)) {
				 continue;
			 }
			 visitedNodes.add(x);
			 dijsktraOnNeightbors(x);
		 }
		 
	}
	private void dijsktraOnNeightbors(int x) {
		double edgeDistance = -1;
		double newDistance = -1;
		//goes through adjacency list for each neighbor
		for(int i = 0; i<adj_list.get(x).size(); i++) {
			Node v = adj_list.get(x).get(i);
			if(!visitedNodes.contains(v.value)) {
				//new distance holds old distance plus one from neighbor(new node)
				edgeDistance = v.weight;
				newDistance = distances[x] + edgeDistance;
				 //if new distance is less weight
				if(newDistance < distances[v.value]) {
					distances[v.value] = newDistance;
					pathHolder[v.value] = x; //holds value of vertex in position of destination
				}
				
				//adds node to priority queue
				pq.add(new Node(v.value, distances[v.value]));
			}
		
		}
		
	}
    
}

class StreetMap{
	 	String st;
		double hold;
		//creates string arraylist to hold locations
		 URArrayList<String> stringArray = new URArrayList<String>();
		//Creates double arraylist to hold latititude
		 URArrayList<Double> latitude = new URArrayList<>();
		//Creates double arraylist to hold longitude
		 URArrayList<Double> longitude = new URArrayList<>();
		//these two make it possible to find road names from lengths (will be needed later)
		 URArrayList<String> roadnames = new URArrayList<>();
		 URArrayList<Double> lengthOfRoads = new URArrayList<>();
		 //two arraylists to hold source/destination values
		 URArrayList<String> from = new URArrayList<>();
		 URArrayList<String> to = new URArrayList<>();
		 //arraylists to hold values of roads that should be highlighted
		 URArrayList<Double> highlightRoadsLong = new URArrayList<>();
		 URArrayList<Double> highlightRoadsLat = new URArrayList<>();
	public List<Edge> generateMap(Scanner scnr) {

	    	List<Edge> edges = new ArrayList<Edge>();
	    	//CREATE NEW METHOD STARTING HERE (MAYBE)
	    	
	    	while ((scnr.hasNextLine())) {
	    		st = scnr.next();
	    		if(st.equals("i")) {
	    			
	    			st = scnr.next();
	    			//if string hasn't been added yet, adds it to the arraylist. Prevents duplicates
	    			//values that should be correlated share indices in respective arraylists. Makes access easy.
	    			//arraylists almost work as a sort of hashmap, but easier to implement imo
	    			if(stringArray.indexOf(st)==-1) {
	    				stringArray.add(st);
	    			}
	    			hold = scnr.nextDouble();
	    			latitude.add(hold);
	    			hold = scnr.nextDouble();
	    			longitude.add(hold);
	    			
	    		}
	    		
	    		Edge edge;
	    		String roadname;
	    		//all of these hold values used in making the edge, names are self explanatory
	    		String st2;
	    		int index;
	    		int index2;
	    		double distance;
	    		double long1;
	    		double long2;
	    		double lat1;
	    		double lat2;
	    		//if road, checks arraylists for necessary values, and using indexes, constructs graph
	    		if(st.equals("r")) {
	    			roadname = scnr.next();
	    			roadnames.add(roadname);
	    			//st holds "source"
	    			st = scnr.next();
	    			//st2 holds "destination"
	    			st2 = scnr.next();
	    			//adds source and destination to arraylists for future access
	    			from.add(st);
	    			to.add(st2);
	    			//hold indexes of locations (used as vertices)
	    			index = stringArray.indexOf(st);
	    			index2 = stringArray.indexOf(st2);
	    			//find and hold corresponding latitudes/longitudes
	    			lat1 = latitude.get(index);
	    			lat2 = latitude.get(index2);
	    			long1 = longitude.get(index);
	    			long2 = longitude.get(index2);
	    			//uses distance formula to find distance
	    			//distance = Math.sqrt(Math.pow(long1 - long2, 2) + Math.pow(lat1 - lat2, 2));
	    			
	    			// distance between latitudes and longitudes
	    	        double dLat = Math.toRadians(lat2 - lat1);
	    	        double dLon = Math.toRadians(long2 - long1);
	    	 
	    	        // convert to radians
	    	        lat1 = Math.toRadians(lat1);
	    	        lat2 = Math.toRadians(lat2);
	    	 
	    	        // apply formulae
	    	        double a = Math.pow(Math.sin(dLat / 2), 2) +
	    	                   Math.pow(Math.sin(dLon / 2), 2) *
	    	                   Math.cos(lat1) *
	    	                   Math.cos(lat2);
	    	        double rad = 3961; //in miles
	    	        double c = 2 * Math.asin(Math.sqrt(a));
	    	        distance = rad * c;
	    			lengthOfRoads.add(distance);
	    			//constructs edge with indexes of locations, and distance to location
	    			edge = new Edge(index, index2, distance);
	    			//adds edge to array list of edges
	    			edges.add(edge);
	    		}
	    	}
	    	return edges;
	    }
	
	public void printPath(int[] pathHolder, int source, int destination, int lastRoad, double distance) {
		if(pathHolder[destination] == -1) {
			System.out.println("total distance: " + distance + " miles");
			System.out.print(stringArray.get(source) + " ");
			//adds source to solution path
			highlightRoadsLong.add(longitude.get(source));
			highlightRoadsLat.add(latitude.get(source));
			//return terminates the code if base case is reached
			return;
		}
		//NEEDS BOTH if statement chains because roads can go both ways
		for(int i = 0; i<roadnames.size(); i++) {
			if(from.get(i).equals(stringArray.get(lastRoad))) {
				if(to.get(i).equals(stringArray.get(destination))) {
					distance += lengthOfRoads.get(i);
					highlightRoadsLong.add(longitude.get(lastRoad));
					highlightRoadsLat.add(latitude.get(lastRoad));
				}
			}
			if(from.get(i).equals(stringArray.get(destination))) {
				if(to.get(i).equals(stringArray.get(lastRoad))) {
					distance+= lengthOfRoads.get(i);
					highlightRoadsLong.add(longitude.get(lastRoad));
					highlightRoadsLat.add(latitude.get(lastRoad));
				}
			}
		}
		lastRoad = destination;
		
		//catch case to make sure destination node gets added to arraylists
		if(!highlightRoadsLat.contains(latitude.get(destination))) {
			highlightRoadsLat.add(latitude.get(destination));
		}
		if(!highlightRoadsLong.contains(longitude.get(destination))) {
			highlightRoadsLong.add(longitude.get(destination));
		}
		//search for corresponding road by checking stringArray for source + destination in for + to. If proper index is found, get the distance of that road and add to the int distance that hold the value
		printPath(pathHolder, source, pathHolder[destination],lastRoad, distance);
		
		
		System.out.print(stringArray.get(destination) + " ");
	}
    public static void main (String[] args) throws IOException {
    	 //parses through args and sets booleans to true if needed to know what needs to run
    	 boolean show = false;
         boolean dijkstra = false;
         String from = "";
         String to = "";
         File file = null;
         for(int i = 0; i<args.length; i++) {
         	if(args[i].equals("--show")) {
         		show = true;
         		
         	}
         	if(args[i].equals("--directions")) {
         		dijkstra = true;
         		from = args[i+1];
         		to = args[i+2];
         		
         	}
         	if(args[i].contains(".txt")) {
         		file = new File(args[i]);
         	}
         }

    	Scanner scnr = new Scanner(file);
    	List<Edge> edges = new ArrayList<Edge>();
    	//instance of main method so I can have variables exclusive to each instance. Also, so I can call method without method being static
    	StreetMap main = new StreetMap();
    	edges = main.generateMap(scnr);
    	scnr.close();
    	//uses edges to construct graph
    	Graph graph = new Graph(edges, main.stringArray.size());
    	
    	//if directions is true, dijkstra must be called first, which is why i chose to use booleans to hold true and false as opposed to just running the code as it parsed the args. This way, the --directions and --show can be mixed in order
        
        if(dijkstra) {
	        String input1 = from;
	        String input2 = to;
	        int source = main.stringArray.indexOf(input1);
	        int destination = main.stringArray.indexOf(input2);
	        graph.dijkstra(graph.adj_list, source, main.stringArray.size());
	        System.out.println("Path from " + main.stringArray.get(source) + " to " + main.stringArray.get(destination) + ": ");
	        double distance = 0;
	        int lastRoad = source;
	        main.printPath(graph.pathHolder, source, destination,lastRoad, distance);
        }
        if(show) {
        	Graph.drawmap(graph, main.longitude, main.latitude, main.highlightRoadsLong, main.highlightRoadsLat);
        }
    }
}

class MyFrame extends JFrame{
	int intx1 = 0;
	int inty1 = 0;
	int intx2 = 0;
	int inty2 = 0;
	Double longMax;
	Double latMax;
	Double longMin;
	Double latMin;
	URArrayList<Integer> ArrayListx1 = new URArrayList<>();
	URArrayList<Integer> ArrayListx2 = new URArrayList<>();
	URArrayList<Integer> ArrayListy1 = new URArrayList<>();
	URArrayList<Integer> ArrayListy2 = new URArrayList<>();
	URArrayList<Double> SolutionLong = new URArrayList<>();
	URArrayList<Double> SolutionLat = new URArrayList<>();
	int arraysize = 0;
	MyFrame(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,800);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		for(int i = 0; i<arraysize; i++) {
			g2D.setStroke(new BasicStroke(1/10));
			intx1 = ArrayListx1.get(i);
			intx2 = ArrayListx2.get(i);
			inty1 = ArrayListy1.get(i);
			inty2 = ArrayListy2.get(i);
			g2D.draw(new Line2D.Double(intx1, inty1, intx2, inty2));
			//g2D.drawLine(intx1, inty1, intx2, inty2);
			
		}
		Color red = new Color(255,0,0);
		g.setColor(red);
		g2D.setStroke(new BasicStroke(3));
		int h = this.getHeight();
		int w = this.getWidth();

		//divide height and width of frame by lat/long ranges to determine a "scale" that will fit the values inside the frame
		double x_scale = w/(longMax-longMin);
		double y_scale = h/(latMax-latMin);
		for(int i = 0; i<SolutionLong.size()-1; i++) {
			int x1 = ((Double)((longMax - Math.abs(SolutionLong.get(i)))*x_scale)).intValue();
			int y1 = ((Double)((latMax - Math.abs(SolutionLat.get(i)))*y_scale)).intValue();
			int x2 = ((Double)((longMax - Math.abs(SolutionLong.get(i+1)))*x_scale)).intValue();
			int y2 = ((Double)((latMax - Math.abs(SolutionLat.get(i+1)))*y_scale)).intValue();	
			g2D.drawLine(x1,y1,x2,y2);
		}
	}
	//method to get solution path from graph method and add them to the arrays in MyFrame to be drawn red
	public void getSolutionPath(URArrayList<Double> longitude, URArrayList<Double> latitude) {
		SolutionLong = longitude;
		SolutionLat = latitude;
		
	}
	public void getNumbers(Double x1, Double y1, Double x2, Double y2, Double longmax, Double longmin, Double latmax, Double latmin) {
		//TODO: NEED TO CONVERT FROM LAT-LONG TO COORD IN FRAME
		int h = this.getHeight();
		int w = this.getWidth();
		longMax = longmax;
		latMax = latmax;
		longMin = longmin;
		latMin = latmin;
		//divide height and width of frame by lat/long ranges to determine a "scale" that will fit the values inside the frame
		double x_scale = w/(longmax-longmin);
		double y_scale = h/(latmax-latmin);
		x1 = (longmax - x1)*x_scale;
		y1 = (latmax - y1)*y_scale;
		x2 = (longmax - x2)*x_scale;
		y2 = (latmax - y2)*y_scale;
		
		
		intx1 = ((Double)x1).intValue();
		inty1 = ((Double)y1).intValue();
		intx2 = ((Double)x2).intValue();
		inty2 = ((Double)y2).intValue();;
		//store values in arraylist so paint can paint the whole thing all at once
		ArrayListx1.add(intx1);
		ArrayListx2.add(intx2);
		ArrayListy1.add(inty1);
		ArrayListy2.add(inty2);
		arraysize++;
		//repaint();
	}
}
 class URArrayList<T> implements URList<T>, Iterable<T>{
	Object[] array = new Object[0];

	public boolean add(T e) {
		Object[] arrayCopy = new Object[array.length+1];
		for(int i = 0; i<array.length; i++) {
			arrayCopy[i]=array[i];
		}
		array = arrayCopy;
		array[array.length-1] = e;
		
		return false;
	}

	public void add(int index, T element) {
		Object[] arrayCopy = new Object[array.length+1];
		for(int i = 0; i<index;i++) {
			arrayCopy[i]=array[i];
		}
		T tempHold = element;
		for(int j = index; j<array.length; j++) {
			arrayCopy[j]=tempHold;
			tempHold = (T)array[j];
		}
		arrayCopy[array.length]=tempHold;
		array = arrayCopy;
	}

	
	public boolean addAll(Collection<? extends T> c) {
		Object[] cArray= c.toArray();
		Object[] arrayCopy = new Object[array.length+cArray.length];
		for(int i = 0; i<array.length; i++) {
			arrayCopy[i] = array[i];
		}
		for(int i = array.length; i<arrayCopy.length; i++) {
			arrayCopy[i] = cArray[i-array.length];
		}
		array = arrayCopy;
		return false;
	}

	
	public boolean addAll(int index, Collection<? extends T> c) {
		int i = 0;
		Object[] cArray= c.toArray();
		Object[] arrayCopy = new Object[array.length+cArray.length+1];
		for(i = 0; i<index;i++){
			arrayCopy[i]=array[i];
		}
		int storeArrayIndex = i;
		for(int j = 0; j<cArray.length; j++) {
			arrayCopy[i] = cArray[j];
			i++;
		}
		while(true){
			arrayCopy[i] = array[storeArrayIndex];
			storeArrayIndex++;
			i++;
			if(storeArrayIndex==array.length) {
				break;
			}
			if(i==arrayCopy.length-1) {
				break;
			}
		}
		array = arrayCopy;
		return false;
	}

	
	public void clear() {
		array = new Object[0];
		
	}

	
	public boolean contains(Object o) {
		for(int i = 0; i<array.length;i++) {
			if (array[i] == o) {
				return true;
			}
		}
		return false;
	}

	
	public boolean containsAll(Collection<?> c) {
		int trueCounter = 0;
		Object[] cArray = c.toArray();
		for(int i = 0; i<cArray.length;i++) {
			if(contains(cArray[i])) {
				trueCounter++;
			}
		}
		if(trueCounter == cArray.length) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		return false;
		
	}

	
	public T get(int index) {
		return (T)array[index];
	}

	public int indexOf(Object o) {
		for(int i = 0; i<array.length;i++) {
			if(array[i].equals(o)) {
				return i;
			}
		}
		return -1;
	}

	
	public boolean isEmpty() {
		for(int i = 0; i<array.length;i++) {
			if(array[i]!=null) {
				return false;
			}
		}
		return true;
	}

	
	public Iterator<T> iterator() {
		Iterator<T> it = new Iterator<T>() {
			private int currentIndex = 0;
			public boolean hasNext() {
				if(array[currentIndex+1]!=null) {
					return true;
				}
				else {
					return false;
				}
			}

			public T next() {
				return (T)array[currentIndex++];
			}
			
		};
		
		return it;
	}

	
	public T remove(int index) {
		Object[] arrayCopy = new Object[array.length-1];
		for(int i = 0; i<index;i++) {
			arrayCopy[i] = array[i];
		}
		for(int i = index; i<arrayCopy.length;i++) {
			arrayCopy[i] = array[i+1];
		}
		array = arrayCopy;
		return null;
	}

	
	public boolean remove(Object o) {
		for(int i = 0; i<array.length; i++) {
			if(array[i] == o) {
				for(int j = i; j<array.length-1;j++) {
					array[j] = array[j+1];
				}
				return true;
			}
		}
		return false;
	}

	
	public boolean removeAll(Collection<?> c) {
		Object[] cArray = c.toArray();
		for(int i = 0; i<cArray.length;i++) {
			remove(cArray[i]);
		}
		return false;
	}

	
	public T set(int index, T element) {
		array[index] = element;
		return null;
	}


	public int size() {
		return array.length;
	}

	
	public URList<T> subList(int fromIndex, int toIndex) {
		URList<T> newList = new URArrayList<T>();
		Object[] subList = new Object[toIndex - fromIndex + 1];
		int x = 0;
		for(int i = fromIndex; i<= toIndex;i++) {
			newList.add((T)array[i]);
		}
		return newList;
	}

	
	public Object[] toArray() {
		Object[] tempArray = new Object[array.length];
		for(int i = 0; i<array.length;i++) {
			tempArray[i] = array[i];
		}
		return tempArray;
	}
	public void ensureCapacity(int minCapacity) {
		if(array.length < minCapacity) {
			Object[] copyArray = new Object[array.length*2];
			for(int i = 0; i<array.length; i++) {
				copyArray[i] = array[i];
			}
			array = copyArray;
			if(array.length<minCapacity){
				ensureCapacity(minCapacity);
			}
		}
	}
	public int getCapacity() {
		return array.length;
		//uses length as the "capacity" since i used nulls to fill the arraylist to the appropriate size
	}
		



	
}
//URList class ADT. Generalize the element type using Java Generics.
	 interface URList<E> extends Iterable<E>{ // URList class ADT
	// Appends the specified element to the end of this list
	boolean add(E e);
	// Inserts the specified element at the specified position in this list
	void add(int index, E element);
	// Appends all of the elements in the specified collection to the end of this list,
	// in the order that they are returned by the specified collection's iterator
	boolean addAll(Collection<? extends E> c);
	// Inserts all of the elements in the specified collection into this list
	// at the specified position
	boolean addAll(int index, Collection<? extends E> c);
	// Removes all of the elements from this list
	void clear();
	// Returns true if this list contains the specified element.
	boolean contains(Object o);
	// Returns true if this list contains all of the elements of the specified collection
	boolean containsAll(Collection<?> c);
	// Compares the specified object with this list for equality.
	// Returns true if both contain the same elements. Ignore capacity
	boolean equals(Object o);
	// Returns the element at the specified position in this list.
	E get(int index);
	// Returns the index of the first occurrence of the specified element in this list,
	// or -1 if this list does not contain the element.
	int indexOf(Object o);
	// Returns true if this list contains no elements.
	boolean isEmpty();
	// Returns an iterator over the elements in this list in proper sequence.
	Iterator<E> iterator();
	// Removes the element at the specified position in this list
	E remove(int index);
	// Removes the first occurrence of the specified element from this list,
	// if it is present
	boolean remove(Object o);
	// Removes from this list all of its elements that are contained
	// in the specified collection
	boolean removeAll(Collection<?> c);
	// Replaces the element at the specified position in this list
	// with the specified element
	E set(int index, E element);
	// Returns the number of elements in this list.
	int size();
	// Returns a view of the portion of this list
	// between the specified fromIndex, inclusive, and toIndex, exclusive.
	URList<E> subList(int fromIndex, int toIndex);
	// Returns an array containing all of the elements in this list
	// in proper sequence (from first to the last element).
	Object[] toArray();
	}
	 
	 

