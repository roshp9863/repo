package com.inn.dsa.practice;
import java.util.*;

class TicTacToe {
    public static void main(String[] args) {
        char[][] maze = new char[3][3];

        for (int i = 0; i < 3; i++) {
            Arrays.fill(maze[i], '*');
        }

        Map<Character,String> playerWithSign = new LinkedHashMap<>();
        getPlayerWithSymbol(playerWithSign,1);
        getPlayerWithSymbol(playerWithSign,2);
        System.out.println("map : " + playerWithSign);
        playTheGame(maze,playerWithSign);

    }

    private static void playTheGame(char[][] maze, Map<Character, String> playerWithSign) {
        Boolean isWin=false;
        Boolean firstPlayerTurn = true;
        boolean found = Arrays.stream(maze)
        .flatMapToInt(row -> new String(row).chars())
        .anyMatch(value -> value == '*');
        while(found) {
            display(maze);
            isWin = checkWin(maze, playerWithSign, isWin);
            if(!isWin) {
                if(firstPlayerTurn) {
                    System.out.println("FIRST PLAYER TURN ....");
                    Scanner s = new Scanner(System.in);
                    int row = s.nextInt();
                    int column = s.nextInt();
                    Boolean isCorrectPosition = checkThePosition(row,column,maze);
                    if(isCorrectPosition) {
                        maze[row][column] = playerWithSign.keySet().stream().findFirst().orElse(null);
                        firstPlayerTurn=false;
                    }else {
                        System.out.println("YOU MISS YOUR TURN BECAUSE YOU SELECT WRONG POSITION : ");
                        firstPlayerTurn=false;
                    }

                }else {
                    System.out.println("SECOND PLAYER TURN ....");
                    Scanner s = new Scanner(System.in);
                    int row = s.nextInt();
                    int column = s.nextInt();
                    Boolean isCorrectPosition = checkThePosition(row,column,maze);
                    if(isCorrectPosition) {
                        maze[row][column] = playerWithSign.keySet().stream().skip(1).findFirst().orElse(null);
                        firstPlayerTurn=true;
                    }else {
                        System.out.println("YOU MISS YOUR TURN BECAUSE YOU SELECT WRONG POSITION : ");
                        firstPlayerTurn=true;
                    }


                }
            }else{
                break;
            }
    

        }

    }

    private static Boolean checkThePosition(int row, int column, char[][] maze) {
        if(maze[row][column]=='*') {
            return true;
        }else{
            return false;
        }
    }

    private static Boolean checkWin(char[][] maze, Map<Character, String> playerWithSign, Boolean isWin) {
        if(!isWin) {
            isWin = checkRow(maze,playerWithSign.keySet().stream().findFirst().orElse(null));
            if(isWin) {
                firstPlayerWin(playerWithSign);
                return isWin;
                
            }
            isWin = checkRow(maze,playerWithSign.keySet().stream().skip(1).findFirst().orElse(null));
            if(isWin) {
                secondPlayerWin(playerWithSign);
                return isWin;
                
            }
        }
        if(!isWin) {
            isWin = checkColumn(maze,playerWithSign.keySet().stream().findFirst().orElse(null));
            if(isWin) {
                firstPlayerWin(playerWithSign);
                return isWin;
                
            }
            isWin = checkColumn(maze,playerWithSign.keySet().stream().skip(1).findFirst().orElse(null));
            if(isWin) {
                secondPlayerWin(playerWithSign);
                return isWin;
                
            }
            
        }
        if(!isWin) {
            isWin = checkDiagnal(maze,playerWithSign.keySet().stream().findFirst().orElse(null));
            if(isWin) {
                firstPlayerWin(playerWithSign);
                return isWin;
                
            }
            isWin = checkDiagnal(maze,playerWithSign.keySet().stream().skip(1).findFirst().orElse(null));
            if(isWin) {
                secondPlayerWin(playerWithSign);
                return isWin;
                
            }
   
        }
        return isWin;
    }

    private static void secondPlayerWin(Map<Character, String> playerWithSign) {
        Character key = playerWithSign.keySet().stream().skip(1).findFirst().orElse(null);
        System.out.println("PLAYER : "+ playerWithSign.get(key) + " WIN HURRAY!!!....");
    }

    private static void firstPlayerWin(Map<Character, String> playerWithSign) {
        Character key = playerWithSign.keySet().stream().findFirst().orElse(null);
        System.out.println("PLAYER : "+ playerWithSign.get(key) + " WIN HURRAY!!!....");
    }

    private static void display(char[][] maze) {
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                System.out.print(maze[i][j] + " | ");
            }
            System.out.println();
        }
    }

    private static Boolean checkColumn(char[][] maze, Character character) {
        if(maze[0][0]==character && maze[1][0]==character && maze[2][0]==character) {
            return true;
        }else if(maze[0][1]==character && maze[1][1]==character && maze[2][1]==character) {
            return true;
        } else if(maze[0][2]==character && maze[1][2]==character && maze[2][2]==character) {
            return true;
        }else {
            return false;
        }
    }

    private static Boolean checkDiagnal(char[][] maze, Character character) {
        if(maze[0][0]==character && maze[1][1]==character && maze[2][2]==character) {
            return true;
        }else if(maze[0][2]==character && maze[1][1]==character && maze[2][0]==character) {
            return true;
        } else {
            return false;
        }
    }

    private static Boolean checkRow(char[][] maze, Character character) {
        if(maze[0][0]==character && maze[0][1]==character && maze[0][2]==character) {
            return true;
        }else if(maze[1][0]==character && maze[1][1]==character && maze[1][2]==character) {
            return true;
        } else if(maze[2][0]==character && maze[2][1]==character && maze[2][2]==character) {
            return true;
        }else {
            return false;
        }
    }

    private static void getPlayerWithSymbol(Map<Character, String> playerWithSign,int count) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Player name :");
        String name = sc.nextLine();
        System.out.println("Write Sign which you want to choose '0' or 'X' or any Other:");
        char sign = sc.next().charAt(0);
        playerWithSign.put(sign,name+"_"+count);
    }
}
