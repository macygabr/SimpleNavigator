package com.navigator.app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.navigator.app.models.structures.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestQueue {
    private Queue queue;

    @BeforeEach
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testPushAndPop() {
        queue.push(1);
        queue.push(2);
        queue.push(3);

        assertEquals(1, queue.pop());
        assertEquals(2, queue.pop());
        assertEquals(3, queue.pop());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testFront() {
        queue.push(1);
        queue.push(2);
        queue.push(3);

        assertEquals(1, queue.front());
        assertEquals(1, queue.front());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testBack() {
        queue.push(1);
        queue.push(2);
        queue.push(3);

        assertEquals(3, queue.back());
        assertEquals(3, queue.back());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEmptyQueue() {
        assertTrue(queue.isEmpty());

        NullPointerException exception = assertThrows(NullPointerException.class, () -> queue.pop());
        assertEquals("Queue is empty", exception.getMessage());
                
        exception = assertThrows(NullPointerException.class, () -> queue.front());
        assertEquals("Queue is empty", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> queue.back());
        assertEquals("Queue is empty", exception.getMessage());
    }
}
