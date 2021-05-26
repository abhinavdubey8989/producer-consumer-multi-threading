package com.producerConsumerUsingBlockingQueue;


import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * 1. BlockingQueue is a thread safe queue to put into and take instances from
 *
 * 2. ie we don't need to implement logic to wait, notify
 *
 * 3. put and take methods are thread safe
 *
 */


class Producer extends Thread {
    private BlockingQueue<Integer> sharedResource;
    private Random random;

    Producer(BlockingQueue sharedResource) {
        this.sharedResource = sharedResource;
        random = new Random();
    }

    @Override
    public void run() {
        try {
            while (true) {
                sharedResource.put(random.nextInt(10));
                System.out.println("After " + Thread.currentThread().getName() + " produced");
                for (int i : sharedResource) {
                    System.out.print(i + " ");
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class Consumer extends Thread {

    private BlockingQueue<Integer> sharedResource;

    Consumer(BlockingQueue sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        try {
            while (true) {
                sharedResource.take();
                System.out.println("After " + Thread.currentThread().getName() + " consumed");
                for (int i : sharedResource) {
                    System.out.print(i + " ");
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

public class ProducerConsumerUsingBlockingQueue {

    public static void main(String[] args) {

        BlockingQueue<Integer> sharedResource = new LinkedBlockingQueue(3);
        Producer producer = new Producer(sharedResource);
        Consumer consumer = new Consumer(sharedResource);

        producer.setName("producer");
        consumer.setName("consumer");


        producer.start();
        consumer.start();
    }
}
