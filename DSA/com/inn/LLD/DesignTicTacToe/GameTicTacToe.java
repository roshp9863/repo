package com.inn.LLD.DesignTicTacToe;

import java.util.List;
import java.util.Scanner;

public class GameTicTacToe {

    List<WinStrategy> winStrategy;
    private boolean isWin = false;
    private boolean isDraw = false;
    private Board board ;
    private List<Player> players;

    GameTicTacToe(Admin admin) {
        this.board = admin.getBoard();
        this.players = admin.getPlayers();
        this.winStrategy = admin.getWinStrategy();
    }     
    
    public void play() {
        Scanner sc = new Scanner(System.in);
        int currentPlayerIndex = 0;
        
        while (!isDraw && !isWin) {
            Player currentPlayer = players.get(currentPlayerIndex);
            board.displayBoard();
            System.out.println(currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ")'s turn.");
            
            int row, col;
            while (true) {
                System.out.print("Enter row and column: ");
                row = sc.nextInt();
                col = sc.nextInt();
                
                // Bounds check
                if (row < 0 || row >= board.getSize() || col < 0 || col >= board.getSize()) {
                    System.out.println("Invalid position! Out of bounds.");
                    continue;
                }
                
                // ✅ Use isEmpty() for single cell check
                if (!board.isEmpty(row, col)) {
                    System.out.println("Cell already occupied! Try again.");
                    continue;
                }
                break;
            }
            
            board.setBoard(row, col, currentPlayer.getSymbol());
            
            // Check win
            for (WinStrategy strategy : winStrategy) {
                if (strategy.checkWin(board, currentPlayer)) {
                    isWin = true;
                    System.out.println(currentPlayer.getName() + " wins!");
                    board.displayBoard();
                    return;
                }
            }
            
            // ✅ Use isBoardFull() for draw detection
            if (board.isBoardFull()) {
                isDraw = true;
                System.out.println("It's a draw!");
                board.displayBoard();
                return;
            }
            
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }
}
