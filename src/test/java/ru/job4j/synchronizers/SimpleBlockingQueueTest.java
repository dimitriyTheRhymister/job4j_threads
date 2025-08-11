package ru.job4j.synchronizers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SimpleBlockingQueueTest {

    @Test
    void shouldAddAndPollSingleElement() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);

        queue.offer(42);
        Integer result = queue.poll();

        assertEquals(42, result);
    }

    @Test
    void pollShouldBlockWhenQueueIsEmpty() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);

        Thread poller = new Thread(() -> {
            try {
                Integer result = queue.poll();
                assertEquals(42, result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        poller.start();

        Thread.sleep(100);
        assertTrue(poller.isAlive());

        queue.offer(42);

        poller.join(1000);
        assertFalse(poller.isAlive());
    }

    @Test
    void offerShouldBlockWhenQueueIsFull() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);

        queue.offer(1);

        Thread producer = new Thread(() -> {
            try {
                queue.offer(42);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();

        Thread.sleep(100);
        assertTrue(producer.isAlive());

        queue.poll();

        producer.join(1000);
        assertFalse(producer.isAlive());
    }

    @Test
    void shouldNotAllowZeroOrNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleBlockingQueue<>(0));
        assertThrows(IllegalArgumentException.class, () -> new SimpleBlockingQueue<>(-1));
    }

    @Test
    void offerShouldBeInterruptible() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        queue.offer(42);

        Thread blockingOffer = new Thread(() -> {
            try {
                queue.offer(43);
                fail("Ожидалось InterruptedException");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        blockingOffer.start();

        Thread.sleep(100);
        blockingOffer.interrupt();
        blockingOffer.join(1000);

        assertTrue(blockingOffer.isInterrupted() || !blockingOffer.isAlive());
    }

    @Test
    void pollShouldBeInterruptible() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);

        Thread blockingPoll = new Thread(() -> {
            try {
                queue.poll();
                fail("Ожидалось InterruptedException");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        blockingPoll.start();

        Thread.sleep(100);
        blockingPoll.interrupt();
        blockingPoll.join(1000);

        assertTrue(blockingPoll.isInterrupted() || !blockingPoll.isAlive());
    }

    @Test
    void shouldPreserveFifoOrder() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);

        queue.offer(1);
        queue.offer(2);
        queue.offer(3);

        assertEquals(1, queue.poll());
        assertEquals(2, queue.poll());
        assertEquals(3, queue.poll());
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final List<Integer> buffer = new ArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);

        Thread producer = new Thread(() -> IntStream.range(0, 5).forEach(i -> {
            try {
                queue.offer(i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
    }
}
