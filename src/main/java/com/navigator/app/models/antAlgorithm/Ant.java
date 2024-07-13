package com.navigator.app.models.antAlgorithm;

import com.navigator.app.s21_graph.Graph;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents an Ant used in Ant Colony Optimization (ACO) for the Traveling Salesman Problem (TSP).
 */
class Ant {
    private final Graph graph;          // Graph object
    @Getter
    private final int numVertices;      // Number of vertices in the graph
    private final double[][] pheromones; // Pheromone matrix
    private final boolean[] visited;    // Keeps track of visited vertices
    @Getter
    private final List<Integer> tour;   // Ant's tour route
    private final Random random;        // Random number generator
    private final double alpha;         // Influence of pheromone
    private final double beta;          // Influence of heuristic information

    /**
     * Constructs an Ant object.
     *
     * @param graph      The graph on which the ant will operate.
     * @param pheromones The pheromone matrix of the graph.
     * @param alpha      Alpha parameter for pheromone influence.
     * @param beta       Beta parameter for heuristic influence.
     */
    public Ant(Graph graph, double[][] pheromones, double alpha, double beta) {
        this.graph = graph;
        this.numVertices = graph.getNumVertices();
        this.pheromones = pheromones;
        this.visited = new boolean[numVertices];
        this.tour = new ArrayList<>();
        this.random = new Random();
        this.alpha = alpha;
        this.beta = beta;
    }

    /**
     * Constructs a solution (tour) for the TSP using the ACO approach.
     *
     * @throws IOException If graph loading fails.
     */
    public void constructSolution() throws IOException {
        checkGraph(graph);

        int startVertex = random.nextInt(numVertices);
        tour.clear();
        tour.add(startVertex);
        visited[startVertex] = true;

        while (tour.size() < numVertices) {
            int nextVertex = selectNextVertex();
            tour.add(nextVertex);
            visited[nextVertex] = true;
        }

        tour.add(startVertex);
    }

    private int selectNextVertex() throws IOException {
        int currentVertex = tour.get(tour.size() - 1);
        double[] probabilities = calculateProbabilities(currentVertex);
        double r = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (int vertex = 0; vertex < numVertices; vertex++) {
            if (!visited[vertex]) {
                cumulativeProbability += probabilities[vertex];
                if (r <= cumulativeProbability) {
                    return vertex;
                }
            }
        }

        for (int vertex = 0; vertex < numVertices; vertex++) {
            if (!visited[vertex]) {
                return vertex;
            }
        }

        throw new IOException("No unvisited vertices to select");
    }

    private double[] calculateProbabilities(int currentVertex) {
        double[] probabilities = new double[numVertices];
        double total = 0.0;

        for (int vertex = 0; vertex < numVertices; vertex++) {
            if (!visited[vertex]) {
                double pheromone = Math.pow(pheromones[currentVertex][vertex], alpha);
                double heuristic = Math.pow(1.0 / graph.getAdjacencyMatrix().get(currentVertex).get(vertex), beta);
                probabilities[vertex] = pheromone * heuristic;
                total += probabilities[vertex];
            }
        }

        for (int vertex = 0; vertex < numVertices; vertex++) {
            if (!visited[vertex]) {
                probabilities[vertex] /= total;
            }
        }

        return probabilities;
    }

    /**
     * Calculates the total length (distance) of the tour found by the ant.
     *
     * @return Total length of the tour.
     */
    public double getTourLength() {
        double tourLength = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int from = tour.get(i);
            int to = tour.get(i + 1);
            tourLength += graph.getAdjacencyMatrix().get(from).get(to);
        }
        return tourLength;
    }

    private void checkGraph(Graph graph) throws IOException {
        if (graph == null) {
            throw new IOException("Graph is null");
        }
        if (graph.getAdjacencyMatrix() == null || graph.getAdjacencyMatrix().isEmpty()) {
            throw new IOException("Graph is empty");
        }
    }
}
