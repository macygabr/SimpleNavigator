package com.navigator.app.models.structures;

import java.util.LinkedList;

public class Queue {
    private final LinkedList<Integer> list;

    public Queue() {
        list = new LinkedList<>();
    }

    public void push(int value) {
        list.addLast(value);
    }

    public int pop() throws NullPointerException {
        if (list.isEmpty()) {
            throw new NullPointerException("Queue is empty");
        }
        return list.removeFirst();
    }

    public int front() throws NullPointerException {
        if (list.isEmpty()) {
            throw new NullPointerException("Queue is empty");
        }
        return list.getFirst();
    }

    public int back() throws NullPointerException {
        if (list.isEmpty()) {
            throw new NullPointerException("Queue is empty");
        }
        return list.getLast();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
