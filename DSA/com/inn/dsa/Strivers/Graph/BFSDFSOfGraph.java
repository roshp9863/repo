package com.inn.dsa.Strivers.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSDFSOfGraph {
    public static void main(String[] args) {
        List<List<Integer>> adjList = new ArrayList<>();
        adjList.add(0, Arrays.asList(2,3,1));
        adjList.add(1, Arrays.asList(0));
        adjList.add(2, Arrays.asList(0,4));
        adjList.add(3, Arrays.asList(0));
        adjList.add(4, Arrays.asList(2));
        boolean[] visited = new boolean[adjList.size()];
        ArrayList<Integer> rs = new ArrayList<>();
        bfs(adjList);
        dfs(0,adjList, visited,rs);
        System.out.println("DFS : " + rs);
    }

    private static void dfs(int node, List<List<Integer>> adjList, boolean[] visited, ArrayList<Integer> rs) {
        visited[node] = true;
        rs.add(node);
        for(int i : adjList.get(node)) {
            if(visited[i]==false) {
                dfs(i,adjList, visited,rs);
            }
        }
    }

    private static void bfs(List<List<Integer>> adjList) {
        ArrayList<Integer> bfs = new ArrayList<>();
        boolean[] visited = new boolean[adjList.size()];

        Queue<Integer> q = new LinkedList<>();
        q.add(0);
        visited[0]=true;

        while (!q.isEmpty()) {
            int node = q.poll();
            bfs.add(node);

            for(int i : adjList.get(node)) {
                if(visited[i]==false) {
                    visited[i] = true;
                    q.add(i);
                }
            }
        }

        System.out.println("BFS : " + bfs);
    }


}
