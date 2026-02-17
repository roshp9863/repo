package com.inn.company.interview;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

// Simulate traffic distribution
public class Azira {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 6; i++) {
            executor.execute(() -> {
                LoadBalancer lb = LoadBalancer.getInstance();
                Node node = lb.getNextNode();
                System.out.println(Thread.currentThread().getName() + " got " + node.getName());
            });
        }

        executor.shutdown();
    }
}

// LoadBalancer with lazy init and round-robin
class LoadBalancer {
    private static LoadBalancer instance;
    private List<Node> nodes;
    private AtomicInteger index = new AtomicInteger(0);

    private LoadBalancer() {}

    public static synchronized LoadBalancer getInstance() {
        if (instance == null) {
            instance = new LoadBalancer();
        }
        return instance;
    }

    private synchronized void initNodes() {
        if (nodes == null) {
            nodes = new ArrayList<>();
            nodes.add(new Node("Node-1"));
            nodes.add(new Node("Node-2"));
            nodes.add(new Node("Node-3"));
        }
    }

    public Node getNextNode() {
        if (nodes == null) {
            initNodes();
        }
        int i = index.getAndUpdate(n -> (n + 1) % nodes.size());
        return nodes.get(i);
    }
}

// Node class with name
class Node {
    private final String name;

    public Node(String name) {
        this.name = name;
        System.out.println(name + " initialized.");
    }

    public String getName() {
        return name;
    }
}