package com.producerConsumerUsingThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 1. there are some problems with creating threads manually
 *
 * 2. The problem here is that we’ve created them “manually."
 * That’s the bad practice.
 * If we create them manually, besides the creation’s cost,
 * another problem is that we don’t have control over how many of them are running at the same time.
 * For example, if millions of requests are reaching a server app, and for each request, a new thread is created,
 * then millions of threads will run in parallel and this could lead to a thread starvation.
 *
 *
 *
 * 3. Thread pools handle the threads' life cycle based on a selected strategy.
 * It holds a limited number of idle threads and reuses them when it needs to solve tasks
 */
public class ProducerConsumerUsingThreadPool {
    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>(2);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        //producer
        Runnable producerTask = () -> {
            try {
                int value = 0;
                while (true) {
                    blockingQueue.put(value);
                    System.out.println("Produced " + value);
                    value++;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        //consumer
        Runnable consumerTask = () -> {
            try {
                while (true) {
                    int value = blockingQueue.take();
                    System.out.println("Consume " + value);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        executor.execute(producerTask);
        executor.execute(consumerTask);

        executor.shutdown();
    }
}
