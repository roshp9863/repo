package com.inn.LLD.DesignTicTacToe;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter board size: ");
        int size = sc.nextInt();
        System.out.print("Enter number of players: ");
        int playerCount = sc.nextInt();
        
        Admin admin = new Admin(size, playerCount);
        GameTicTacToe game = new GameTicTacToe(admin);
        game.play();
    }
}
