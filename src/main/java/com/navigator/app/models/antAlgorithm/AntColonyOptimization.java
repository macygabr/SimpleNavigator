package com.navigator.app.models.antAlgorithm;

import com.navigator.app.s21_graph.Graph;
import lombok.Builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Implements Ant Colony Optimization (ACO) algorithm to solve the Traveling Salesman Problem (TSP).
 * Uses a graph represented by an adjacency matrix for computation.
 * <p>
 * Default values:
 * <ul>
 * <li>numAnts: Number of ants used in the algorithm. Default is 10.</li>
 * <li>maxIterations: Maximum number of iterations for the algorithm. Default is 100.</li>
 * <li>evaporationRate: Pheromone evaporation rate used in the algorithm. Default is 0.5.</li>
 * <li>alpha: Alpha parameter for pheromone influence. Default is 1.0.</li>
 * <li>beta: Beta parameter for heuristic influence. Default is 2.0.</li>
 * </ul>
 */
@Builder
public class AntColonyOptimization {
    private final Graph graph;
    private double[][] pheromones;
    private List<Ant> ants;
    private Ant bestAnt;

    @Builder.Default
    private final int numAnts = 10;
    @Builder.Default
    private final int maxIterations = 100;
    @Builder.Default
    private final double evaporationRate = 0.5;
    @Builder.Default
    private final double alpha = 1.0;
    @Builder.Default
    private final double beta = 2.0;

    /**
     * Finds the optimal tour using the Ant Colony Optimization (ACO) algorithm.
     *
     * @return TsmResult representing the optimal route and distance.
     * @throws IOException If graph loading fails.
     */
    public TsmResult findOptimalTour() throws IOException {
        checkGraph(graph);
        initializePheromones();

        for (int iter = 0; iter < maxIterations; iter++) {
            createAnts();
            simulateAnts();
            updatePheromones();

            Ant currentBestAnt = findBestAnt();
            if (bestAnt == null || currentBestAnt.getTourLength() < bestAnt.getTourLength()) {
                bestAnt = currentBestAnt;
            }
        }

        List<Integer> bestTour = bestAnt.getTour();
        double bestTourLength = bestAnt.getTourLength();

        return new TsmResult(bestTour, bestTourLength);
    }

    private void initializePheromones() {
        int numVertices = graph.getNumVertices();
        pheromones = new double[numVertices][numVertices];

        IntStream.range(0, numVertices).forEach(i ->
                IntStream.range(0, numVertices).forEach(j ->
                        pheromones[i][j] = 1.0));
    }

    private void createAnts() {
        ants = new ArrayList<>();
        for (int i = 0; i < numAnts; i++) {
            ants.add(new Ant(graph, pheromones, alpha, beta));
        }
    }

    private void simulateAnts() throws IOException {
        for (Ant ant : ants) {
            ant.constructSolution();
        }
    }

    private void updatePheromones() {
        evaporatePheromones();

        for (Ant ant : ants) {
            List<Integer> tour = ant.getTour();
            double tourLength = ant.getTourLength();

            for (int i = 0; i < tour.size() - 1; i++) {
                int from = tour.get(i);
                int to = tour.get(i + 1);
                pheromones[from][to] += 1.0 / tourLength;
                pheromones[to][from] += 1.0 / tourLength;
            }
        }
    }

    private void evaporatePheromones() {
        for (int i = 0; i < graph.getNumVertices(); i++) {
            for (int j = 0; j < graph.getNumVertices(); j++) {
                pheromones[i][j] *= (1.0 - evaporationRate);
            }
        }
    }

    private Ant findBestAnt() {
        Ant best = ants.get(0);
        for (Ant ant : ants) {
            if (ant.getTourLength() < best.getTourLength()) {
                best = ant;
            }
        }
        return best;
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
