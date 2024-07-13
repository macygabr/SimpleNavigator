package com.navigator.app;

import com.navigator.app.interfaceApp.ConsoleInterface;

import java.io.IOException;

public class Main {
    private static final ConsoleInterface consoleInterface = new ConsoleInterface();

    public static void main(String[] args) {
        try {
            consoleInterface.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//src/test/resources/testFiles/testGraph.txt
//src/test/resources/testFiles/simpleGraph.txt
