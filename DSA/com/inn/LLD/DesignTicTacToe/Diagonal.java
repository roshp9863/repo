package com.inn.LLD.DesignTicTacToe;

public class Diagonal implements WinStrategy{

    private static Diagonal instance=null;

    private Diagonal () {
    }

    public static Diagonal getInstance() {
        if(instance == null) {
            instance = new Diagonal();
        }
        return instance;
    }

    @Override
    public boolean checkWin(Board board, Player player) {
        char symbol = player.getSymbol();
        char[][] ticTacToeBoard = board.getBoard();
        int size = board.getSize();
        
        // Check main diagonal
        boolean mainDiag = true;
        for (int i = 0; i < size; i++) {
            if (ticTacToeBoard[i][i] != symbol) {
                mainDiag = false;
                break;
            }
        }
        if (mainDiag) return true;
        
        // Check anti-diagonal
        boolean antiDiag = true;
        for (int i = 0; i < size; i++) {
            if (ticTacToeBoard[i][size - 1 - i] != symbol) {
                antiDiag = false;
                break;
            }
        }
        return antiDiag;
    }

}
