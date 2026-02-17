package com.inn.LLD.DesignTicTacToe;

public class Board {
    private char[][] board;
    private int size;
    private static final char EMPTY_CELL = '.';

    public Board(int size) {
        this.size = size;
        board = new char[size][size];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }
    public void displayBoard() {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void setBoard(int row, int col, char symbol) {
        board[row][col] = symbol;
    }

    public boolean isBoardFull() {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                if(board[i][j] == EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == EMPTY_CELL;
    }
    
}
