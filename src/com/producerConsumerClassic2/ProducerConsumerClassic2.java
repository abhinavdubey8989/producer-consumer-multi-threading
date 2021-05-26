package com.producerConsumerClassic2;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * 1. here logic for production and consumption are inside the shared resource itself
 *
 * 2. this production and consumption logics are thread safe bcz there is synced block
 *
 * 3. wait and notify is called on this
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
                sharedResource.produce(Thread.currentThread().getName(), random.nextInt(10));
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
                sharedResource.consume(Thread.currentThread().getName());
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

    private void addToQueue(int x) {
        queue.offer(x);
    }

    private int removeFromQueue() {
        return queue.poll();
    }

    private int getCurrentSize() {
        return queue.size();
    }

    private int getMaxSize() {
        return this.MAX_SIZE;
    }

    private void showQueue(String nameOfThread) {
        System.out.println(nameOfThread);
        for (int i : queue) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public void produce(String nameOfThread, int item) throws Exception {
        synchronized (this) {
            while (this.getCurrentSize() == this.getMaxSize()) {
                this.wait();
            }

            //insert random value from 0-9
            this.addToQueue(item);
            System.out.println("After Producer produced :");
            this.showQueue(nameOfThread);
            Thread.sleep(1500);
            this.notify();
        }
    }

    public void consume(String nameOfThread) throws Exception {
        synchronized (this) {
            while (this.getCurrentSize() == 0) {
                this.wait();
            }
            this.removeFromQueue();
            System.out.println("After Consumer consumed : ");
            this.showQueue(nameOfThread);
            Thread.sleep(1500);
            this.notify();

        }
    }

}


public class ProducerConsumerClassic2 {

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
