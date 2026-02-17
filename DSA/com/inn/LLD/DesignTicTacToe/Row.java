package com.inn.LLD.DesignTicTacToe;

public class Row implements WinStrategy{

    private static Row instance=null;

    private Row () {
    }

    public static Row getInstance() {
        if(instance == null) {
            instance = new Row();
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
                if(ticTacToeBoard[i][j] != symbol) {
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
