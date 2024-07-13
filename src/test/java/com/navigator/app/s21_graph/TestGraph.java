package com.navigator.app.s21_graph;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGraph {
    private Graph graph;
    private static HashSet<Path> names;
    private static String pathToSave;

    @BeforeAll
    public static void setFilesName() throws IOException {
        names = new HashSet<>();
        pathToSave = "src/test/resources/resultFiles/";

        ClassLoader classLoader = TestGraph.class.getClassLoader();
        URL resource = classLoader.getResource("./testFiles");
        if (resource == null) {
            throw new IOException("Resources directory not found");
        }

        Path resourcePath = Paths.get(resource.getPath());

        try (Stream<Path> paths = Files.walk(resourcePath)) {
            names = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .collect(Collectors.toCollection(HashSet::new));
        }

        if (names.isEmpty()) {
            throw new IOException("No files found in resources directory");
        }
    }

    @BeforeEach
    public void setUp() {
        graph = new Graph();
    }

    @Test
    public void testLoadLoadGraphFromFile() throws IOException {
        for (Path name : names) {
            graph.loadGraphFromFile(name.toString());
        }
    }

    @Test
    public void testExceptionLoadLoadGraphFromFile() {
        IOException exception = assertThrows(IOException.class, () -> graph.loadGraphFromFile("incorrect"));
        assertEquals("File not found", exception.getMessage());
    }

    @Test
    public void testExportLoadGraphFromFile() throws IOException {
        String fileName;
        for (Path name : names) {
            graph.loadGraphFromFile(name.toString());
            fileName = name.getFileName().toString();
            fileName = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
            System.out.println(pathToSave + "result_" + fileName + ".dot");
            graph.exportGraphToDot(pathToSave + "result_" + fileName + ".dot");
        }
    }

    @Test
    public void testExceptionExportLoadGraphFromFile() {
        assertThrows(IOException.class, () -> graph.exportGraphToDot("incorrect/path/to/save"));
    }
}
