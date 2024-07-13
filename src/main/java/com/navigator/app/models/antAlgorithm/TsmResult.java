package com.navigator.app.models.antAlgorithm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Result of the Traveling Salesman Problem (TSP), comprising visited vertices
 * sequence and total route distance.
 */
@Data
@AllArgsConstructor
public class TsmResult {
    private List<Integer> vertices;
    private double distance;
}
