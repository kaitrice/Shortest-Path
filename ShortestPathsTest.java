package graph;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URL;
import java.io.FileNotFoundException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathsTest {

    /* Performs the necessary gradle-related incantation to get the
       filename of a graph text file in the src/test/resources directory at
       test time.*/
    private String getGraphResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return resource.getPath();
    }

    /** Placeholer test case. Write your own tests here.  Be sure to include
     * the @Test annotation above each test method, or JUnit will ignore it and
     * not run it as a test case. */
    @Test
    public void test00inPath() {
        /*String fn = getGraphResource("Simple1.txt");
        Graph simple1;
        try {
          simple1 = ShortestPaths.parseGraph("basic", fn);
        } catch (FileNotFoundException e) {
          fail("Could not find graph Simple1.txt");
          return;
        }*/

        Graph g = new Graph();
	ShortestPaths sp = new ShortestPaths();

        Node a = g.getNode("A");
        Node b = g.getNode("B");
	Node c = g.getNode("C");

	g.addEdge(a, b, 1);
	g.addEdge(a, c, 2);
	g.addEdge(c, b, 2);

	sp.compute(a);

	assertTrue(sp.getPaths().containsKey(a));
	assertTrue(sp.getPaths().containsKey(b));
	assertTrue(sp.getPaths().containsKey(c));
    }

    @Test
    public void test00Values() {
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();

        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");

        g.addEdge(a, b, 1);
        g.addEdge(a, c, 2);
        g.addEdge(c, b, 2);

        sp.compute(a);

        assertEquals(0.0, sp.shortestPathLength(a), 0);
        assertEquals(1.0, sp.shortestPathLength(b), 0);
	assertEquals(2.0, sp.shortestPathLength(c), 0);

    }

    @Test
    public void test00backpointer() {
	Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();

        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");

        g.addEdge(a, b, 1);
        g.addEdge(a, c, 2);
        g.addEdge(c, b, 2);

        sp.compute(a);

	assertEquals(null, sp.getPaths().get(a).previous);
        assertEquals(a, sp.getPaths().get(b).previous);
	assertEquals(a, sp.getPaths().get(c).previous);
    }

    @Test
    public void test00path() {
	Graph g = new Graph();
	ShortestPaths sp = new ShortestPaths();

	Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");

        g.addEdge(a, b, 1);
	g.addEdge(a, c, 2);
        g.addEdge(c, b, 2);

        sp.compute(a);

	LinkedList<Node> pathB = new LinkedList<Node>();
	pathB.add(a);
	pathB.add(b);

	LinkedList<Node> pathC = new LinkedList<Node>();
	pathC.add(a);
	pathC.add(c);


	assertEquals(pathB, sp.shortestPath(b));
	assertEquals(pathC, sp.shortestPath(c));

    }


    @Test
    public void test01() {
	Graph g = new Graph();
 	ShortestPaths sp = new ShortestPaths();

	Node s = g.getNode("S");
	Node a = g.getNode("A");
	Node b = g.getNode("B");
	Node c = g.getNode("C");
	Node d = g.getNode("D");

	g.addEdge(s, a, 10);
	g.addEdge(s, c, 5);
	g.addEdge(a, b, 1);
	g.addEdge(a, c, 2);
	g.addEdge(b, d, 4);
	g.addEdge(c, a, 3);
	g.addEdge(c, b, 9);
	g.addEdge(c, d, 2);
	g.addEdge(d, s, 1);
	g.addEdge(d, b, 7);

	sp.compute(s);

	assertEquals(0.0, sp.shortestPathLength(s), 0);
	assertEquals(8.0, sp.shortestPathLength(a), 0);
	assertEquals(9.0, sp.shortestPathLength(b), 0);
	assertEquals(5.0, sp.shortestPathLength(c), 0);
	assertEquals(7.0, sp.shortestPathLength(d), 0);

	LinkedList<Node> path = new LinkedList<Node>();
	path.add(s);
	path.add(c);
	path.add(a);
	path.add(b);

	assertEquals(path, sp.shortestPath(b));

    }

    @Test
    public void test02() {
	Graph g = new Graph();
	ShortestPaths sp = new ShortestPaths();

	Node a = g.getNode("A");
	Node b = g.getNode("B");
	Node c = g.getNode("C");
	Node d = g.getNode("D");
	Node e = g.getNode("E");
	Node f = g.getNode("F");
	Node gN = g.getNode("G");
	Node h = g.getNode("H");
	Node i = g.getNode("I");
	Node j = g.getNode("J");

	g.addEdge(d, a, 4);
	g.addEdge(d, h, 1);
	g.addEdge(a, h, 10);
	g.addEdge(a, e, 1);
	g.addEdge(h, e, 5);
	g.addEdge(h, i, 9);
	g.addEdge(e, f, 3);
	g.addEdge(f, b, 1);
	g.addEdge(f, i, 1);
	g.addEdge(f, c, 3);
	g.addEdge(f, gN, 7);
	g.addEdge(b, c, 2);
	g.addEdge(i, j, 2);
	g.addEdge(j, gN, 1);

	sp.compute(d);

	assertEquals(0.0, sp.shortestPathLength(d), 0);
	assertEquals(4.0, sp.shortestPathLength(a), 0);
	assertEquals(9.0, sp.shortestPathLength(b), 0);
	assertEquals(11.0, sp.shortestPathLength(c), 0);
	assertEquals(5.0, sp.shortestPathLength(e), 0);
	assertEquals(8.0, sp.shortestPathLength(f), 0);
	assertEquals(12.0, sp.shortestPathLength(gN), 0);
	assertEquals(1.0, sp.shortestPathLength(h), 0);
	assertEquals(9.0, sp.shortestPathLength(i), 0);
	assertEquals(11.0, sp.shortestPathLength(j), 0);

	LinkedList<Node> path = new LinkedList<Node>();
	path.add(d);
	path.add(a);
	path.add(e);
	path.add(f);
	path.add(i);
	path.add(j);

	assertEquals(path, sp.shortestPath(j));

    }

}
Footer
© 2022 GitHub, Inc.
Footer navigation
Terms
Privacy
Security
Status
Docs
Contact GitHub
Pricing
API
Training
Blog
About
