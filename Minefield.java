//Written by Asher Spilane-Nelson and Henry Vuong

import java.sql.SQLOutput;
import java.util.Random;

public class Minefield {

    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";


    public Cell[][] board;
    private int flags;
    private boolean hitMine;


    public boolean isRevealed(int x, int y) { //Gets the revealed status of cell
        return board[x][y].getRevealed();
    }

    public Minefield(int rows, int columns, int flags) { //generates an empty minefield array of cells
        this.flags = flags;
        board = new Cell[rows][columns];
        for (int i = 0; i < board.length; i ++) {
            for (int j = 0; j < board[i].length; j ++) {
                board[i][j] = new Cell(false, "0");
            }
        }
    }

    public void evaluateField() { //
        int increment = 0;
        for (int i = 0; i < board.length; i ++) {
            for (int j = 0; j < board[i].length; j ++) {
                if (board[i][j].getStatus().equals("M")) {
                    if (i + 1 < board.length && !board[i + 1][j].getStatus().equals("M")) {
                        increment = Integer.parseInt(board[i + 1][j].getStatus());
                        increment++;
                        board[i + 1][j].setStatus(String.valueOf(increment));
                    }
                    if (i + 1 < board.length && j + 1 < board[0].length && !board[i + 1][j + 1].getStatus().equals("M")) {
                        increment = Integer.parseInt(board[i + 1][j + 1].getStatus());
                        increment++;
                        board[i + 1][j + 1].setStatus(String.valueOf(increment));
                    }
                    if (j + 1 < board[0].length && !board[i][j + 1].getStatus().equals("M")) {
                        increment = Integer.parseInt(board[i][j + 1].getStatus());
                        increment++;
                        board[i][j + 1].setStatus(String.valueOf(increment));
                    }
                    if (0 <= i - 1 && j + 1 < board[0].length && !board[i - 1][j + 1].getStatus().equals("M")) {
                        increment = Integer.parseInt(board[i - 1][j + 1].getStatus());
                        increment++;
                        board[i - 1][j + 1].setStatus(String.valueOf(increment));
                    }
                    if (0 <= i - 1 && !board[i - 1][j].getStatus().equals("M")) {
                        increment = Integer.parseInt(board[i - 1][j].getStatus());
                        increment++;
                        board[i - 1][j].setStatus(String.valueOf(increment));
                    }
                    if (0 <= i - 1 && 0 <= j - 1 && !board[i - 1][j - 1].getStatus().equals("M")) {
                        increment = Integer.parseInt(board[i - 1][j - 1].getStatus());
                        increment ++;
                        board[i - 1][j - 1].setStatus(String.valueOf(increment));
                    }
                    if (0 <= j - 1 && !board[i][j - 1].getStatus().equals("M")) {
                        increment = Integer.parseInt(board[i][j - 1].getStatus());
                        increment++;
                        board[i][j - 1].setStatus(String.valueOf(increment));
                    }
                    if (i + 1 < board.length && 0 <= j - 1 && !board[i + 1][j - 1].getStatus().equals("M")) {
                        increment = Integer.parseInt(board[i + 1][j - 1].getStatus());
                        increment ++;
                        board[i + 1][j - 1].setStatus(String.valueOf(increment));
                    }
                }
            }
        }
    }


    public void createMines(int x, int y, int mines) { //Creates mines on random tiles of the board
        Random rand = new Random();
        while(mines > 0){
            int randX = rand.nextInt(0,board.length );
            int randY = rand.nextInt(0, board[0].length );
            if(randX != x && randY != y){
                if(!board[randX][randY].getStatus().equals("M")){
                    board[randX][randY].setStatus("M");
                    mines--;
                }
            }
        }
    }

    public boolean guess(int x, int y, boolean flag) { //Checks the user's guess, allows user to place a flag, ends the game if user guesses a mine
        if(x >= 0 && x < board.length  && y >= 0 && y < board[0].length){
            if(flag && flags > 0 && !board[x][y].getStatus().equals("F")){
                board[x][y].setStatus("F");
                flags--;
                board[x][y].setRevealed(true);
                return false;
            }
            else if(flag && flags == 0){
                System.out.println("not enough flags");
                return false;
            }
            else if(flag && board[x][y].getStatus().equals("F")){
                System.out.println("Toggled flag status.");
                flags++;
                board[x][y].setRevealed(false);
                return false;
            }
            else{
                if(board[x][y].getStatus().equals("0")){
                    revealZeroes(x,y);
                    return false;
                }
                if(board[x][y].getStatus().equals("M")){
                    board[x][y].setRevealed(true);
                    hitMine = true;
                    return true;
                }
                board[x][y].setRevealed(true);
            }

        }
        return false;
    }


    public boolean gameOver() { //Ends the game
        int counter = 0;
        for(int i = 0; i < board.length; i ++){
            for(int j = 0; j < board[0].length; j ++){
                if(!board[i][j].getRevealed()){
                    counter ++;
                }
                if(board[i][j].getStatus().equals("M") && board[i][j].getRevealed()){ // Player has selected a mine.
                    System.out.println("You hit a mine! Game over.");
                    return true;
                }
            }
        }
        if(counter == 0){ //All mines flagged
            System.out.println("All mines have been flagged and tiles revealed. You win!");
            return true;
        }
        return false;
    }


    public void revealZeroes(int x, int y) { // Reveals zero value cells
        Stack1Gen<int[]> stack = new Stack1Gen<int[]>();
        stack.push(new int[] {x, y});
        while (stack.top() != null) {
            int[] arr = stack.pop();
            board[arr[0]][arr[1]].setRevealed(true);
            if (board[arr[0]][arr[1]].getStatus().equals("0")) {
                if ((arr[1] + 1) < board[0].length && !board[arr[0]][arr[1] + 1].getRevealed()) { //Right case
                    stack.push(new int[]{arr[0], arr[1] + 1});
                }
                if ((arr[0] - 1) >= 0 && !board[arr[0] - 1][arr[1]].getRevealed()) { //Up case
                    stack.push(new int[]{arr[0] - 1, arr[1]});
                }
                if ((arr[1] - 1) >= 0 && !board[arr[0]][arr[1] - 1].getRevealed()) { //Left case
                    stack.push(new int[]{arr[0], arr[1] - 1});
                }
                if ((arr[0] + 1) < board.length && !board[arr[0] + 1][arr[1]].getRevealed()) { //Down case
                    stack.push(new int[]{arr[0] + 1, arr[1]});
                }
            }
        }
    }


    public void revealStartingArea(int x, int y) { //Reveals starting area for the player on initial guess
        Q1Gen<int[]> queue = new Q1Gen<int[]>();
        int[] arr = new int[2];
        arr[0] = x;
        arr[1] = y;
        queue.add(arr);
        while(queue.length() > 0){
            arr = queue.remove();
            int newX = arr[0];
            int newY = arr[1];

            if(newX < board.length && newX >= 0 && newY < board.length && newY >=0 && board[newX][newY].getStatus().equals("M")){
                break;
            }

            if( newX - 1 >= 0 && !board[newX][newY].getRevealed()){
                int[] newCord = new int[2];
                newCord[0] = newX-1;
                newCord[1] = newY;
                queue.add(newCord);
            }
            if( newX + 1 < board.length && newX > 0 && !board[newX][newY].getRevealed()){
                int[] newCord = new int[2];
                newCord[0] = newX + 1;
                newCord[1] = newY;
                queue.add(newCord);
            }
            if(newY - 1 >= 0 && !board[newX][newY].getRevealed()){
                int[] newCord = new int[2];
                newCord[0] = newX;
                newCord[1] = newY -1;
                queue.add(newCord);
            }
            if(newY + 1 < board.length && newY > 0 && !board[newX][newY].getRevealed()){
                int[] newCord = new int[2];
                newCord[0] = newX;
                newCord[1] = newY + 1;
                queue.add(newCord);
            }
            board[newX][newY].setRevealed(true);
        }

    }


    public void debug() { //Prints out the whole field with all the mines
        StringBuilder result = new StringBuilder();
        result.append("  ");
        for(int i = 0; i < 10 && i < board.length; i++){ //If the row is less than 10, add two spaces between the numbers.
            result.append("  ");
            result.append(i);
        }
        for (int i = 10; i < board.length; i++) { //If exceeding 10, only one space is added, keeping the board aligned.
            result.append(" ");
            result.append(i);
        }
        result.append('\n');
        for(int i = 0; i < board.length; i++){
            if(i < 10) {
                result.append(" ");
            }
            result.append(i);
            for(int j = 0; j < board[i].length; j++){
                result.append("  ");
                String temp = board[i][j].getStatus();
                if(board[i][j].getStatus().equals("M")){
                    result.append(ANSI_RED_BRIGHT+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("F")){
                    result.append(ANSI_PURPLE+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("0")){
                    result.append(ANSI_YELLOW+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("1")){
                    result.append(ANSI_BLUE+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("2")){
                    result.append(ANSI_GREEN+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("3")){
                    result.append(ANSI_CYAN+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("4")){
                    result.append(ANSI_YELLOW_BRIGHT+temp+ANSI_GREY_BACKGROUND);
                }
                if (board[i][j].getStatus().equals("5")){
                    result.append(ANSI_BLUE_BRIGHT+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("6")){
                    result.append(ANSI_GREEN+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("7")){
                    result.append(ANSI_CYAN+temp+ANSI_GREY_BACKGROUND);
                }
                if (board[i][j].getStatus().equals("8")){
                    result.append(ANSI_RED+temp+ANSI_GREY_BACKGROUND);
                }
            }
            result.append("\n");
        }
        System.out.println(result);
    }

    public String toString() { //Prints out user's board
        StringBuilder result = new StringBuilder();
        result.append("  ");
        for(int i = 0; i < 10 && i < board.length; i++){
            result.append("  ");
            result.append(i);
        }
        for (int i = 10; i < board.length; i++) {
            result.append(" ");
            result.append(i);
        }
        result.append('\n');
        for(int i = 0; i < board.length; i++){
            if(i < 10) {
                result.append(" ");
            }
            result.append(i);
            for(int j = 0; j < board[i].length; j++){
                result.append("  ");
                String temp = board[i][j].getStatus();
                if(!board[i][j].getRevealed() && !board[i][j].getStatus().equals("F")){
                    result.append("-");
                }
                if(board[i][j].getStatus().equals("M") && board[i][j].getRevealed()){
                    result.append(ANSI_RED_BRIGHT+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("F")){
                    result.append(ANSI_PURPLE+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("0") && board[i][j].getRevealed()){
                    result.append(ANSI_YELLOW+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("1") && board[i][j].getRevealed()){
                    result.append(ANSI_BLUE+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("2") && board[i][j].getRevealed()){
                    result.append(ANSI_GREEN+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("3") && board[i][j].getRevealed()){
                    result.append(ANSI_CYAN+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("4") && board[i][j].getRevealed()){
                    result.append(ANSI_YELLOW_BRIGHT+temp+ANSI_GREY_BACKGROUND);
                }
                if (board[i][j].getStatus().equals("5") && board[i][j].getRevealed()){
                    result.append(ANSI_BLUE_BRIGHT+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("6") && board[i][j].getRevealed()){
                    result.append(ANSI_GREEN+temp+ANSI_GREY_BACKGROUND);
                }
                if(board[i][j].getStatus().equals("7") && board[i][j].getRevealed()){
                    result.append(ANSI_CYAN+temp+ANSI_GREY_BACKGROUND);
                }
                if (board[i][j].getStatus().equals("8") && board[i][j].getRevealed()){
                    result.append(ANSI_RED+temp+ANSI_GREY_BACKGROUND);
                }
            }
            result.append("\n");
        }
        return result.toString();
    }
}