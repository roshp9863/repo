package com.inn.LLD.DesignTicTacToe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Admin {

    List<WinStrategy> winStrategy= new ArrayList<>();
    private Board board;
    private List<Player> players = new ArrayList<>();
   

    Admin(int boardSize, int playerCount) {
        this.board = new Board(boardSize);

        createPlayers(playerCount, players);

        this.winStrategy = ruleMenu();
    }

    private void createPlayers(int playerCount, List<Player> players) {
        Scanner scanner = new Scanner(System.in);
        Set<Character> usedSymbols = new HashSet<>();
        for(int i=1; i<=playerCount; i++) {
            System.out.println("Enter name for Player " + i + ":");
            String name = scanner.nextLine();
            System.out.println("Available symbols: X, O, #, $, @, %, &");
            char sym;
            while (true) {
                System.out.print("Choose a symbol for " + name + ": ");
                sym = scanner.nextLine().charAt(0);
                if (usedSymbols.contains(sym)) {
                    System.out.println("Symbol already taken! Choose another.");
                } else {
                    usedSymbols.add(sym);
                    break;
                }
            }
            players.add(new Player(name, sym));           
        }
    }


    public List<WinStrategy> ruleMenu() {
        System.out.println("Welcome to Tic Tac Toe!");
        System.out.println("Rules:");
        System.out.println("You can select one or more win strategies to play the game.");  
        System.out.println("Please select a win strategy by entering the corresponding number:");
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Select Win Strategy: 1.Row 2.Column 3.Diagonal 4.Stop");
            int choice = scanner.nextInt();
            switch (choice) {
                    case 1: 
                        Row row = Row.getInstance();
                        winStrategy.add(row);
                        break;
                    case 2:
                        Column column = Column.getInstance();
                        winStrategy.add(column);
                        break;
                    case 3:
                        Diagonal diagonal = Diagonal.getInstance();
                        winStrategy.add(diagonal);
                        break;
                    case 4:
                        return winStrategy;    
                    default:
                        System.out.println("Invalid choice! Please select a valid win strategy.");
                        break;
            }
        }
    }

    public List<Player> getPlayers() {
        return players;
    }


    public Board getBoard() {
        return board;
    }

    public List<WinStrategy> getWinStrategy() {
        return winStrategy;
    }


}
