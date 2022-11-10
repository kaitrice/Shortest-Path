/** Name: Kaitlyn Rice
*   Date: June 5, 2020
*   Purpose: Find the shortest paths from a source node
**/

package graph;

import heap.Heap;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;

/** Provides an implementation of Dijkstra's single-source shortest paths
 * algorithm.
 * Sample usage:
 *   Graph g = // create your graph
 *   ShortestPaths sp = new ShortestPaths();
 *   Node a = g.getNode("A");
 *   sp.compute(a);
 *   Node b = g.getNode("B");
 *   LinkedList<Node> abPath = sp.getShortestPath(b);
 *   double abPathLength = sp.getShortestPathLength(b);
 *   */
public class ShortestPaths {
    // stores auxiliary data associated with each node for the shortest
    // paths computation:
    private HashMap<Node,PathData> paths;
    private Heap<Node,Double> frontier;

    /* returns paths, used for unit testing */
    public HashMap<Node,PathData> getPaths() {
	return paths;
    }

    /** Compute the shortest path to all nodes from origin using Dijkstra's
     * algorithm. Fill in the paths field, which associates each Node with its
     * PathData record, storing total distance from the source, and the
     * backpointer to the previous node on the shortest path.
     * Precondition: origin is a node in the Graph.*/
    public void compute(Node origin) {
        paths = new HashMap<Node,PathData>();

        // TODO 1: implement Dijkstra's algorithm to fill paths with
        // shortest-path data for each Node reachable from origin.
	frontier = new Heap<Node,Double>();
	Node backPointer = null;
	PathData pD = new PathData(0.0, backPointer);
	frontier.add(origin, 0.0);
	paths.put(origin, pD);
	while (frontier.size() > 0) {
	    Node f = frontier.poll();
	    for (Map.Entry<Node,Double> nEntry : f.getNeighbors().entrySet()) {
		Node w = nEntry.getKey();
		double dist = paths.get(f).distance + nEntry.getValue();
		if (!paths.containsKey(w)) {
		    backPointer = f;
		    pD = new PathData(dist, backPointer);
		    frontier.add(w, dist);
		    paths.put(w, pD);
		} else if (dist < paths.get(w).distance) {
		    backPointer = f;
		    pD = new PathData(dist, backPointer);
		    paths.remove(w);
		    paths.put(w, pD);
		}
	    }
	}
    }

    /** Returns the length  the shortest path from the origin to destination.
     * If no path exists, return Double.POSITIVE_INFINITY.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public double shortestPathLength(Node destination) {
        // TODO 2 - implement this method to fetch the shortest path length
        // from the paths data computed by Dijkstra's algorithm.
	if (!paths.containsKey(destination))
	    return Double.POSITIVE_INFINITY;
	return paths.get(destination).distance;
        //throw new UnsupportedOperationException();
    }

    /** Returns a LinkedList of the nodes along the shortest path from origin
     * to destination. This path includes the origin and destination. If origin
     * and destination are the same node, it is included only once.
     * If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public LinkedList<Node> shortestPath(Node destination) {
        // TODO 3 - implement this method to reconstruct sequence of Nodes
        // along the shortest path from the origin to destination using the
        // paths data computed by Dijkstra's algorithm.
	LinkedList<Node> d2o = new LinkedList<Node>();
	if (!paths.containsKey(destination))
	    return null;
	d2o.add(destination);
	Node temp = paths.get(destination).previous;
	while (temp != null) {
	    d2o.add(temp);
	    temp = paths.get(temp).previous;
	}
	//reverse LinkedList for orgin to destination
	LinkedList<Node> o2d = new LinkedList<Node>();
	Node temp2 = d2o.pollLast();
	while (temp2 != null) {
	    o2d.add(temp2);
	    temp2 = d2o.pollLast();
	}
	return o2d;
        //throw new UnsupportedOperationException();
    }


    /** Inner class representing data used by Dijkstra's algorithm in the
     * process of computing shortest paths from a given source node. */
    class PathData {
        double distance; // distance of the shortest path from source
        Node previous; // previous node in the path from the source

        /** constructor: initialize distance and previous node */
        public PathData(double dist, Node prev) {
            distance = dist;
            previous = prev;
        }
    }


    /** Static helper method to open and parse a file containing graph
     * information. Can parse either a basic file or a DB1B CSV file with
     * flight data. See GraphParser, BasicParser, and DB1BParser for more.*/
    protected static Graph parseGraph(String fileType, String fileName) throws
        FileNotFoundException {
        // create an appropriate parser for the given file type
        GraphParser parser;
        if (fileType.equals("basic")) {
            parser = new BasicParser();
        } else if (fileType.equals("db1b")) {
            parser = new DB1BParser();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + fileType);
        }

        // open the given file
        parser.open(new File(fileName));

        // parse the file and return the graph
        return parser.parse();
    }

    public static void main(String[] args) {
      // read command line args
      String fileType = args[0];
      String fileName = args[1];
      String origCode = args[2];

      String destCode = null;
      if (args.length == 4) {
          destCode = args[3];
      }

      // parse a graph with the given type and filename
      Graph graph;
      try {
          graph = parseGraph(fileType, fileName);
      } catch (FileNotFoundException e) {
          System.out.println("Could not open file " + fileName);
          return;
      }
      graph.report();


      // TODO 4: create a ShortestPaths object, use it to compute shortest
      // paths data from the origin node given by origCode.
      ShortestPaths sP = new ShortestPaths();
      Node origin = graph.getNode(origCode);
      sP.compute(origin);
      // TODO 5:
      // If destCode was not given, print each reachable node followed by the
      // length of the shortest path to it from the origin.
      if (destCode == null) {
	    System.out.println("Shortest paths from " + origin + ": ");
            for (Map.Entry<Node,PathData> n : sP.getPaths().entrySet())
		  System.out.println(n.getKey() + ": " + n.getValue().distance);
      }
      // TODO 6:
      // If destCode was given, print the nodes in the path from
      // origCode to destCode, followed by the total path length
      // If no path exists, print a message saying so.
      else {
            Node destination = graph.getNode(destCode);
            if (!sP.paths.containsKey(destination)){
                  System.out.println("path not avalible");
                  return;
            }
	    for (Node n : sP.shortestPath(destination))
		System.out.print(n + " ");
	    System.out.print(sP.shortestPathLength(destination));
      }

    }
}
