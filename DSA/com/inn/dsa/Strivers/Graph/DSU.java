package com.inn.dsa.Strivers.Graph;

public class DSU {
    
    private int[] id;
    private int[] size;
    
    public DSU(int n) {
        id = new int[n];
        size = new int[n];
        for(int i=0; i<n; i++) {
            id[i] = i;
        }
    }

    private int root(int i) {
     if (id[i] != i)
            id[i] = root(id[i]); // Path compression
        return id[i];
    }

    public boolean connected(int p, int q) {
        return root(p)==root(q);
    }

    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        
        if(i==j) return;

        if(size[i] < size[j]) {
            id[i] = j;
            size[j] = size[j] + size[i];
        }else {
            id[j] = i;
            size[i] = size[i] + size[j];

        }

    }

}
