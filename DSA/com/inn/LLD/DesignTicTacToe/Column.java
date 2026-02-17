package com.inn.LLD.DesignTicTacToe;

public class Column implements WinStrategy {

    private static Column instance=null;

    private Column () {
    }

    public static Column getInstance() {
        if(instance == null) {
            instance = new Column();
        }
        return instance;
    }

    @Override
    public boolean checkWin(Board board, Player player) {
        char symbol = player.getSymbol();
        char[][] ticTacToeBoard = board.getBoard();
        int size = board.getSize();
        for(int i=0; i<size; i++) {
            boolean isWin = true;
            for(int j=0; j<size; j++) {
                if(ticTacToeBoard[j][i] != symbol) {
                    isWin = false;
                    break;
                }
            }
            if(isWin) {
                return true;
            }
        }
        return false;
    }

}