package com.navigator.app.s21_graph_algorithms;

import com.navigator.app.models.antAlgorithm.TsmResult;
import com.navigator.app.s21_graph.Graph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGraphAlgorithms {
    private static GraphAlgorithms graphAlgorithms;
    private static Graph graph;

    @BeforeAll
    public static void setFilesName() throws IOException {
        graph = new Graph();
        graph.loadGraphFromFile("src/test/resources/testFiles/testGraph.txt");
        graphAlgorithms = new GraphAlgorithms();
    }

    @Test
    public void testGraphAlgorithmsDepthFirstSearch() throws IOException {
        ArrayList<Integer> res = graphAlgorithms.DepthFirstSearch(graph, 0);
        for (int i = 0; i < res.size(); i++) {
            assertEquals(res.get(i), i);
        }
    }

    @Test
    public void testExceptionGraphAlgorithmsDepthFirstSearch() {
        IOException exception = assertThrows(IOException.class, () -> graphAlgorithms.DepthFirstSearch(graph, -12));
        assertEquals("Vertex not found", exception.getMessage());

        exception = assertThrows(IOException.class, () -> graphAlgorithms.DepthFirstSearch(graph, graph.getNumVertices() + 1));
        assertEquals("Vertex not found", exception.getMessage());

        Graph testGraph = new Graph();
        exception = assertThrows(IOException.class, () -> graphAlgorithms.DepthFirstSearch(testGraph, graph.getNumVertices() - 1));
        assertEquals("Graph is empty", exception.getMessage());
    }

    @Test
    public void testGraphAlgorithmsBreadthFirstSearch() throws IOException {
        ArrayList<Integer> res = graphAlgorithms.BreadthFirstSearch(graph, 0);
        for (int i = 0; i < res.size(); i++) {
            assertEquals(res.get(i), i);
        }
    }

    @Test
    public void testExceptionGraphAlgorithmsBreadthFirstSearch() {
        IOException exception = assertThrows(IOException.class, () -> graphAlgorithms.BreadthFirstSearch(graph, -12));
        assertEquals("Vertex not found", exception.getMessage());

        exception = assertThrows(IOException.class, () -> graphAlgorithms.BreadthFirstSearch(graph, graph.getNumVertices() + 1));
        assertEquals("Vertex not found", exception.getMessage());

        Graph testGraph = new Graph();
        exception = assertThrows(IOException.class, () -> graphAlgorithms.BreadthFirstSearch(testGraph, graph.getNumVertices() - 1));
        assertEquals("Graph is empty", exception.getMessage());
    }

    @Test
    public void testGraphAlgorithmsGetShortestPathBetweenVertices() throws IOException {
        int res = graphAlgorithms.GetShortestPathBetweenVertices(graph, 0, graph.getNumVertices() - 1);
        assertEquals(res, 18);

        res = graphAlgorithms.GetShortestPathBetweenVertices(graph, 2, 1);
        assertEquals(res, 15);

        res = graphAlgorithms.GetShortestPathBetweenVertices(graph, 4, 8);
        assertEquals(res, 20);

        res = graphAlgorithms.GetShortestPathBetweenVertices(graph, 10, 6);
        assertEquals(res, 84);
    }

    @Test
    public void testExceptionGraphAlgorithmsGetShortestPathBetweenVertices() {
        IOException exception = assertThrows(IOException.class, () -> graphAlgorithms.GetShortestPathBetweenVertices(graph, -graph.getNumVertices(), graph.getNumVertices() - 1));
        assertEquals("Vertex not found", exception.getMessage());

        exception = assertThrows(IOException.class, () -> graphAlgorithms.GetShortestPathBetweenVertices(graph, graph.getNumVertices() - 1, -graph.getNumVertices()));
        assertEquals("Vertex not found", exception.getMessage());

        exception = assertThrows(IOException.class, () -> graphAlgorithms.GetShortestPathBetweenVertices(graph, graph.getNumVertices() - 1, graph.getNumVertices() + 1));
        assertEquals("Vertex not found", exception.getMessage());

        exception = assertThrows(IOException.class, () -> graphAlgorithms.GetShortestPathBetweenVertices(graph, graph.getNumVertices() + 1, graph.getNumVertices() - 1));
        assertEquals("Vertex not found", exception.getMessage());


        Graph testGraph = new Graph();
        exception = assertThrows(IOException.class, () -> graphAlgorithms.GetShortestPathBetweenVertices(testGraph, graph.getNumVertices() - 1, graph.getNumVertices() - 1));
        assertEquals("Graph is empty", exception.getMessage());
    }

    @Test
    public void testExceptionGraphAlgorithmsGetShortestPathsBetweenAllVertices() {
        Graph testGraph = new Graph();
        IOException exception = assertThrows(IOException.class, () -> graphAlgorithms.GetShortestPathsBetweenAllVertices(testGraph));
        assertEquals("Graph is empty", exception.getMessage());
    }

    @Test
    public void testGraphAlgorithmsGetLeastSpanningTree() throws IOException {
        ArrayList<ArrayList<Integer>> mst = graphAlgorithms.GetLeastSpanningTree(graph);

        assertEquals(graph.getNumVertices(), mst.size());
        assertEquals(graph.getNumVertices(), mst.get(0).size());

        int edgeCount = 0;
        for (int i = 0; i < mst.size(); i++) {
            for (int j = i + 1; j < mst.size(); j++) {
                if (mst.get(i).get(j) != 0) {
                    edgeCount++;
                }
            }
        }
        assertEquals(graph.getNumVertices() - 1, edgeCount);
    }


    @Test
    public void testExceptionGraphAlgorithmsGetLeastSpanningTree() {
        Graph testGraph = new Graph();
        IOException exception = assertThrows(IOException.class, () -> graphAlgorithms.GetLeastSpanningTree(testGraph));
        assertEquals("Graph is empty", exception.getMessage());
    }

    @Test
    public void testGraphAlgorithmsSolveTspWithAntColonyOptimization() throws IOException {
        TsmResult result = graphAlgorithms.SolveTravelingSalesmanProblem(graph);
        assertNotNull(result);
        assertEquals(result.getDistance() < 256, true);
    }

    @Test
    public void testExceptionGraphAlgorithmsSolveTspWithAntColonyOptimization() {
        Graph testGraph = new Graph();
        IOException exception = assertThrows(IOException.class, () -> graphAlgorithms.SolveTravelingSalesmanProblem(testGraph));
        assertEquals("Graph is empty", exception.getMessage());
    }

}
