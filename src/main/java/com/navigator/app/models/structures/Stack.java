package com.navigator.app.models.structures;

import java.util.LinkedList;

public class Stack {
    private final LinkedList<Integer> list;

    public Stack() {
        list = new LinkedList<>();
    }

    public void push(int value) {
        list.addFirst(value);
    }

    public int pop() throws NullPointerException {
        if (list.isEmpty()) {
            throw new NullPointerException("Stack is empty");
        }
        return list.removeFirst();
    }

    public int top() throws NullPointerException {
        if (list.isEmpty()) {
            throw new NullPointerException("Stack is empty");
        }
        return list.getFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
