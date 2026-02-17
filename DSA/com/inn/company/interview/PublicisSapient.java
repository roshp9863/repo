package com.inn.company.interview;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PublicisSapient {
    public static void main(String[] args) {
        
        ExecutorService e = Executors.newFixedThreadPool(4);

        for(int i=0; i<4; i++) {
            Future f =e.submit(() -> {
                System.out.println("hello");
            });

            f.isDone();
            
        }
        
        PriorityQueue<Integer> p = new PriorityQueue<>(Comparator.reverseOrder());

        p.add(1);
        p.add(4);
        p.add(2);
        p.add(3);

        System.out.println();
        

    }
}
