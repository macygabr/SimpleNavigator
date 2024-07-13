package com.navigator.app.s21_graph_algorithms;

import com.navigator.app.models.antAlgorithm.TsmResult;
import com.navigator.app.models.structures.Queue;
import com.navigator.app.models.structures.Stack;
import com.navigator.app.models.antAlgorithm.AntColonyOptimization;
import com.navigator.app.s21_graph.Graph;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
 * This class provides methods to find shortest paths in a graph.
 */
public class GraphAlgorithms {
    /**
     * Performs a depth-first search (DFS) starting from the specified vertex in the graph.
     *
     * @param graph       The graph object on which DFS is performed.
     * @param startVertex The starting vertex for the DFS traversal.
     * @return An ArrayList of integers representing the order of visited vertices.
     * @throws IOException If an I/O error occurs while reading the graph data.
     */
    public ArrayList<Integer> DepthFirstSearch(@NotNull Graph graph, int startVertex) throws IOException {
        checkGraph(graph);
        checkVertex(graph, startVertex);

        ArrayList<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Stack stack = new Stack();

        stack.push(startVertex);
        while (!stack.isEmpty()) {
            int currentVertex = stack.pop();
            if (!visited.contains(currentVertex)) {
                visited.add(currentVertex);
                result.add(currentVertex);

                ArrayList<Integer> neighbors = graph.getAdjacencyMatrix().get(currentVertex);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    if (neighbors.get(i) != 0 && !visited.contains(i)) {
                        stack.push(i);
                    }
                }
            }
        }
        return result;
    }


    /**
     * Performs a breadth-first search (BFS) starting from the specified vertex in the graph.
     *
     * @param graph       The graph object on which BFS is performed.
     * @param startVertex The starting vertex for the BFS traversal.
     * @return An ArrayList of integers representing the order of visited vertices.
     * @throws IOException If an I/O error occurs while reading the graph data.
     */
    public ArrayList<Integer> BreadthFirstSearch(@NotNull Graph graph, int startVertex) throws IOException {
        checkGraph(graph);
        checkVertex(graph, startVertex);

        ArrayList<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue queue = new Queue();

        queue.push(startVertex);
        visited.add(startVertex);

        while (!queue.isEmpty()) {
            int currentVertex = queue.pop();
            result.add(currentVertex);

            ArrayList<Integer> neighbors = graph.getAdjacencyMatrix().get(currentVertex);
            for (int i = 0; i < neighbors.size(); i++) {
                if (neighbors.get(i) != 0 && !visited.contains(i)) {
                    queue.push(i);
                    visited.add(i);
                }
            }
        }
        return result;
    }


    /**
     * Finds the shortest path between two vertices in the graph using Dijkstra's algorithm.
     *
     * @param graph   The graph object on which the algorithm is applied.
     * @param vertex1 The index of the first vertex.
     * @param vertex2 The index of the second vertex.
     * @return The shortest distance between the two vertices.
     */
    public int GetShortestPathBetweenVertices(@NotNull Graph graph, int vertex1, int vertex2) throws IOException {
        checkGraph(graph);
        checkVertex(graph, vertex1);
        checkVertex(graph, vertex2);

        int numVertices = graph.getNumVertices();
        int[] distances = new int[numVertices];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[vertex1] = 0;

        PriorityQueue<VertexDistance> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(v -> v.distance));
        priorityQueue.add(new VertexDistance(vertex1, 0));

        boolean[] visited = new boolean[numVertices];

        while (!priorityQueue.isEmpty()) {
            VertexDistance current = priorityQueue.poll();
            int currentVertex = current.vertex;

            if (visited[currentVertex]) continue;
            visited[currentVertex] = true;

            ArrayList<Integer> neighbors = graph.getAdjacencyMatrix().get(currentVertex);
            for (int i = 0; i < neighbors.size(); i++) {
                if (neighbors.get(i) != 0 && !visited[i]) {
                    int newDist = distances[currentVertex] + neighbors.get(i);
                    if (newDist < distances[i]) {
                        distances[i] = newDist;
                        priorityQueue.add(new VertexDistance(i, newDist));
                    }
                }
            }
        }

        return distances[vertex2];
    }

    /**
     * Finds the shortest paths between all pairs of vertices in the graph using Floyd-Warshall algorithm.
     *
     * @param graph The graph object on which the algorithm is applied.
     * @return A 2D ArrayList representing the shortest paths between all vertices.
     */
    public ArrayList<ArrayList<Integer>> GetShortestPathsBetweenAllVertices(@NotNull Graph graph) throws IOException {
        checkGraph(graph);

        int numVertices = graph.getNumVertices();
        ArrayList<ArrayList<Integer>> distances = new ArrayList<>(graph.getAdjacencyMatrix());

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i != j && distances.get(i).get(j) == 0) {
                    distances.get(i).set(j, Integer.MAX_VALUE / 2);
                }
            }
        }

        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distances.get(i).get(j) > distances.get(i).get(k) + distances.get(k).get(j)) {
                        distances.get(i).set(j, distances.get(i).get(k) + distances.get(k).get(j));
                    }
                }
            }
        }

        return distances;
    }

    /**
     * Finds the minimum spanning tree (MST) in the graph using Prim's algorithm.
     *
     * @param graph The graph object on which the algorithm is applied.
     * @return A 2D ArrayList representing the adjacency matrix of the minimum spanning tree.
     */
    public ArrayList<ArrayList<Integer>> GetLeastSpanningTree(@NotNull Graph graph) throws IOException {
        checkGraph(graph);
        int numVertices = graph.getNumVertices();
        ArrayList<ArrayList<Integer>> adjacencyMatrix = graph.getAdjacencyMatrix();

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            result.add(new ArrayList<>());
            for (int j = 0; j < numVertices; j++) {
                result.get(i).add(0);
            }
        }

        boolean[] visited = new boolean[numVertices];
        int[] minWeight = new int[numVertices];
        int[] parent = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            minWeight[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        minWeight[0] = 0;
        parent[0] = -1;

        for (int i = 0; i < numVertices - 1; i++) {
            int minVertex = findMinVertex(numVertices, visited, minWeight);
            visited[minVertex] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && adjacencyMatrix.get(minVertex).get(v) != 0 &&
                        adjacencyMatrix.get(minVertex).get(v) < minWeight[v]) {
                    parent[v] = minVertex;
                    minWeight[v] = adjacencyMatrix.get(minVertex).get(v);
                }
            }
        }

        for (int i = 1; i < numVertices; i++) {
            result.get(i).set(parent[i], minWeight[i]);
            result.get(parent[i]).set(i, minWeight[i]);
        }

        return result;
    }

    private int findMinVertex(int numVertices, boolean[] visited, int[] minWeight) {
        int minVertex = -1;
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i] && (minVertex == -1 || minWeight[i] < minWeight[minVertex])) {
                minVertex = i;
            }
        }
        return minVertex;
    }

    /**
     * Solves the Traveling Salesman Problem (TSP) using Ant Colony Optimization (ACO) algorithm.
     *
     * @param graph The graph object on which the algorithm is applied.
     * @return An instance of TsmResult representing the optimal route and distance.
     */
    public TsmResult SolveTravelingSalesmanProblem(@NotNull Graph graph) throws IOException {
        checkGraph(graph);

        AntColonyOptimization aco = AntColonyOptimization.builder().graph(graph).build();
        return aco.findOptimalTour();
    }


    private void checkGraph(@NotNull Graph graph) throws IOException {
        if (graph.getAdjacencyMatrix() == null || graph.getAdjacencyMatrix().isEmpty()) {
            throw new IOException("Graph is empty");
        }
    }

    private void checkVertex(@NotNull Graph graph, int vertex) throws IOException {
        if (vertex < 0 || vertex >= graph.getNumVertices()) {
            throw new IOException("Vertex not found");
        }
    }
}

class VertexDistance {
    int vertex;
    int distance;

    VertexDistance(int vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }
}