package com.navigator.app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.navigator.app.models.structures.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestStack {
    private NullPointerException exception;
    private Stack stack = new Stack();

    @BeforeEach
    public void setUp() {
        stack = new Stack();
    }

    @Test
    public void testPushAndPop() {
        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testTop() {
        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, stack.top());
        assertEquals(3, stack.top());
        assertFalse(stack.isEmpty());
    }

    @Test
    public void testEmptyStack() {
        exception = assertThrows(NullPointerException.class, () -> stack.pop());
        assertEquals("Stack is empty", exception.getMessage());
        
        exception = assertThrows(NullPointerException.class, () -> stack.top());
        assertEquals("Stack is empty", exception.getMessage());
        assertTrue(stack.isEmpty());
    }
}
