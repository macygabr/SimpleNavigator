package com.navigator.app.s21_graph;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import lombok.Getter;

/**
 * Represents a graph using an adjacency matrix.
 */
@Getter
public class Graph {

    private ArrayList<ArrayList<Integer>> adjacencyMatrix;
    private int numVertices;

    /**
     * Loads a graph from a file in adjacency matrix format.
     *
     * @param filename The name of the file containing the graph data.
     * @throws IOException           If an I/O error occurs while reading the file.
     * @throws NumberFormatException If the format of the data in the file is incorrect.
     */
    public void loadGraphFromFile(String filename) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            int size = Integer.parseInt(bufferedReader.readLine().trim());
            adjacencyMatrix = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.trim().split("\\s+");
                ArrayList<Integer> row = new ArrayList<>();
                for (int col = 0; col < size; col++) {
                    row.add(Integer.parseInt(values[col]));
                }
                adjacencyMatrix.add(row);
            }
            numVertices = size;
        } catch (IOException e) {
            throw new IOException("File not found");
        } catch (NumberFormatException e) {
            throw new IOException("File wrong format");
        }
    }

    /**
     * Exports the graph to a file in DOT format.
     *
     * @param filename The name of the file to which the graph will be exported.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void exportGraphToDot(String filename) throws IOException {
        if (adjacencyMatrix == null || adjacencyMatrix.isEmpty()) throw new IOException("Graph is empty");

        Path filePath = Paths.get(filename);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath.toFile()));

        bufferedWriter.write("graph " + filename + "{\n");
        for (int i = 1; i <= numVertices; i++) {
            bufferedWriter.write("\t" + i + ";\n");
        }

        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (adjacencyMatrix.get(i).get(j) != 0) {
                    bufferedWriter.write("\t" + i + " -- " + j + " [label=\"" + adjacencyMatrix.get(i).get(j) + "\"];\n");
                }
            }
        }
        bufferedWriter.write("}");
        bufferedWriter.close();
    }
}
