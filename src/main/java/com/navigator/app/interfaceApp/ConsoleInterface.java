package com.navigator.app.interfaceApp;

import com.navigator.app.models.interfaceModels.Menu;
import com.navigator.app.models.antAlgorithm.TsmResult;
import com.navigator.app.s21_graph.Graph;
import com.navigator.app.s21_graph_algorithms.GraphAlgorithms;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ConsoleInterface {
    private final Graph graph = new Graph();
    private final GraphAlgorithms graphAlgorithms = new GraphAlgorithms();
    private Menu userInput = Menu.INVALID_OPERATION;

    public void run() throws IOException {
        while (userInput != Menu.EXIT) {
            try {
                outputMenu();
                readInput();
                handleMenuOption();
            } catch (IOException e) {
                customPrint(e.getMessage(), Color.RED);
            } catch (NoSuchElementException | IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void outputMenu() {
        customPrint("Menu:", Color.YELLOW);
        System.out.println("1. Loading a graph from a file in the adjacency matrix format");
        System.out.println("2. Exporting a graph to a dot file");
        System.out.println("3. Non-recursive depth-first search");
        System.out.println("4. Breadth-first search");
        System.out.println("5. Searching for the shortest path between two vertices");
        System.out.println("6. Searching for the shortest paths between all pairs of vertices");
        System.out.println("7. Searching for the minimal spanning tree in a graph");
        System.out.println("8. Solving the traveling salesman's problem using the ant colony algorithm");
        System.out.println("9. Exit");
    }

    private void readInput() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int userInputInt;
        try {
            userInputInt = scanner.nextInt();
        } catch (NoSuchElementException | IllegalStateException e) {
            throw new IOException("Incorrect data");
        }


        if (userInputInt == 1) userInput = Menu.LOAD_GRAPH;
        else if (userInputInt == 2) userInput = Menu.EXPOTPT_GRAPH;
        else if (userInputInt == 3) userInput = Menu.DEPTH_FIRST_SEARCH;
        else if (userInputInt == 4) userInput = Menu.BREADTH_FIRST_SEARCH;
        else if (userInputInt == 5) userInput = Menu.SHORTEST_PATH;
        else if (userInputInt == 6) userInput = Menu.ALL_PAIRS_SHORTEST_PATHS;
        else if (userInputInt == 7) userInput = Menu.MINIMUM_SPANNING_TREE;
        else if (userInputInt == 8) userInput = Menu.TRAVELING_SALESMAN;
        else if (userInputInt == 9) userInput = Menu.EXIT;
        else throw new IOException("Incorrect data");
    }

    private void handleMenuOption() throws IOException {
        if (userInput == Menu.INVALID_OPERATION) return;
        if (userInput == Menu.EXIT) return;

        if (userInput == Menu.LOAD_GRAPH) LoadGraph();
        else if (userInput == Menu.EXPOTPT_GRAPH) ExportGraph();
        else if (userInput == Menu.DEPTH_FIRST_SEARCH) DepthFirstSearch();
        else if (userInput == Menu.BREADTH_FIRST_SEARCH) BreadthFirstSearch();
        else if (userInput == Menu.SHORTEST_PATH) ShortestPath();
        else if (userInput == Menu.ALL_PAIRS_SHORTEST_PATHS) AllPairsShortestPath();
        else if (userInput == Menu.MINIMUM_SPANNING_TREE) MinimumSpanningTree();
        else if (userInput == Menu.TRAVELING_SALESMAN) TravelingSalesman();
    }

    private void LoadGraph() throws IOException, NoSuchElementException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the path");
        String path = scanner.nextLine();

        graph.loadGraphFromFile(path);
        customPrint("Successful read!", Color.BLUE);
    }

    private void ExportGraph() throws IOException, NoSuchElementException {
        Scanner scanner = new Scanner(System.in);
        customPrint("Path+Name?", Color.YELLOW);
        String path = scanner.nextLine();

        graph.exportGraphToDot(path);
        customPrint("Successful write!", Color.BLUE);
    }

    private void DepthFirstSearch() throws IOException {
        while (true) {
            try {
                int num = getNumberAndSendMessage();
                ArrayList<Integer> res = graphAlgorithms.DepthFirstSearch(graph, num);
                printArray(res);
            } catch (NumberFormatException e) {
                customPrint("Input number or 'Exit'", Color.RED);
            } catch (UnsupportedOperationException e) {
                return;
            }
        }
    }

    private void BreadthFirstSearch() throws IOException {
        while (true) {
            try {
                int num = getNumberAndSendMessage();
                ArrayList<Integer> res = graphAlgorithms.BreadthFirstSearch(graph, num);
                printArray(res);
            } catch (NumberFormatException e) {
                customPrint("Input number or 'Exit'", Color.RED);
            } catch (UnsupportedOperationException e) {
                return;
            }
        }
    }


    private void ShortestPath() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                customPrint("Input first and second vertexes [x y] or 'Exit'", Color.YELLOW);
                String input;
                if ((input = scanner.nextLine()).equals("Exit")) return;
                String[] parts = input.split(" ");
                if (parts.length != 2) {
                    throw new NumberFormatException();
                }
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int res = graphAlgorithms.GetShortestPathBetweenVertices(graph, x, y);
                customPrint(String.valueOf(res), Color.WHITE);
            } catch (NumberFormatException e) {
                customPrint("Input correct numbers or 'Exit'", Color.RED);
            }
        }
    }

    private void AllPairsShortestPath() throws IOException {
        ArrayList<ArrayList<Integer>> shortestPaths = graphAlgorithms.GetShortestPathsBetweenAllVertices(graph);

        System.out.println("Shortest Paths Between All Pairs of Vertices:");
        printMatrix(shortestPaths);
    }

    private void MinimumSpanningTree() throws IOException {
        ArrayList<ArrayList<Integer>> minSpanningTree = graphAlgorithms.GetLeastSpanningTree(graph);
        System.out.println("Minimum Spanning Tree:");
        printMatrix(minSpanningTree);
    }

    private void TravelingSalesman() throws IOException {
        TsmResult tsmResult = graphAlgorithms.SolveTravelingSalesmanProblem(graph);

        System.out.println("Optimal Tour: " + tsmResult.getVertices());
        System.out.println("Tour Length: " + tsmResult.getDistance());
    }

    private int getNumberAndSendMessage() throws NumberFormatException, UnsupportedOperationException {
        Scanner scanner = new Scanner(System.in);
        customPrint("Input starting vertex or 'Exit'", Color.YELLOW);
        String input;
        if ((input = scanner.nextLine()).equals("Exit")) throw new UnsupportedOperationException();
        return Integer.parseInt(input);
    }

    private void printArray(@NotNull ArrayList<Integer> res) throws IOException {
        if (res.isEmpty()) throw new IOException("Fail search");
        res.forEach(System.out::print);
        System.out.println();
    }

    private void printMatrix(@NotNull ArrayList<ArrayList<Integer>> adjacencyMatrix) {
        for (ArrayList<Integer> matrix : adjacencyMatrix) {
            for (Integer integer : matrix) {
                System.out.print(integer + " ");
            }
            System.out.println();
        }
    }

    private void customPrint(String message, Color color) {
        int code;
        if (color == Color.RED) code = 31;
        else if (color == Color.YELLOW) code = 33;
        else if (color == Color.BLUE) code = 34;
        else if (color == Color.WHITE) code = 0;
        else code = 0;

        System.out.print("\033[" + code + "m");
        System.out.println(message);
        System.out.print("\033[0m");
    }
}
