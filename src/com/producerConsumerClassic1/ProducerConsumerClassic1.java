package com.producerConsumerClassic1;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * 1. the shared resource is a queue of fixed size
 *
 * 2. in this classical problem ,
 * the producing and consumption logic are located in respective threads
 *
 * 3. the shared resources will expose the APIs to add and remove elements from queue
 * and current and MAX_ALLOWED sizes
 *
 * 4. the wait and notify methods will be called in the producer/consumer threads
 * over the sharedResource object
 *
 *
 */

class Producer extends Thread {
    private SharedResource sharedResource;
    private Random random;

    Producer(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
        random = new Random();
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (sharedResource) {
                    while (sharedResource.getCurrentSize() == sharedResource.getMaxSize()) {
                        sharedResource.wait();
                    }

                    //insert random value from 0-9
                    sharedResource.addToQueue(random.nextInt(10));

                    System.out.println("After Producer produced :");
                    sharedResource.showQueue(Thread.currentThread().getName());
                    sharedResource.notify();
                    Thread.sleep(1500);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class Consumer extends Thread {

    private SharedResource sharedResource;

    Consumer(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (sharedResource) {
                    while (sharedResource.getCurrentSize() == 0) {
                        sharedResource.wait();
                    }
                    sharedResource.removeFromQueue();
                    System.out.println("After Consumer consumed : ");
                    sharedResource.showQueue(Thread.currentThread().getName());
                    sharedResource.notify();
                    Thread.sleep(1500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

class SharedResource {

    private Queue<Integer> queue;
    private int MAX_SIZE;

    SharedResource(int size) {
        this.MAX_SIZE = size;
        queue = new LinkedList<>();
    }

    public void addToQueue(int x) {
        queue.offer(x);
    }

    public int removeFromQueue() {
        return queue.poll();
    }

    public int getCurrentSize() {
        return queue.size();
    }

    public int getMaxSize() {
        return this.MAX_SIZE;
    }

    public void showQueue(String nameOfThread) {
        System.out.println(nameOfThread);
        for (int i : queue) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

}


public class ProducerConsumerClassic1 {

    public static void main(String[] args) {

        SharedResource sharedResource = new SharedResource(3);
        Producer producer = new Producer(sharedResource);
        Consumer consumer = new Consumer(sharedResource);

        producer.setName("producer");
        consumer.setName("consumer");


        producer.start();
        consumer.start();

    }
}
