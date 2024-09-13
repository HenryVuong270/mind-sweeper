//Written by Asher Spilane-Nelson and Henry Vuong
import java.util.Random;
import java.util.Scanner;



public class main {

    public static void main(String[] args) {
        boolean debugMode;
        int totalMines;
        int mapSize;
        int turns = 0;
        Minefield minefield;
        Scanner scanner = new Scanner(System.in);
        String scannerInput = "";

        //Code explanation: Due to how the gameOver method works, we need to execute all setup in a separate case, outside the !gameOver loop.

        System.out.println("Do you wish to run the game in debug mode? Y/N (case sensitive).");
        scannerInput = scanner.nextLine();
        while (!(scannerInput.equals("Y") || scannerInput.equals("N"))) {
            System.out.println("Invalid selection! The available options are 'Y', 'N' (case sensitive).");
            scannerInput = scanner.nextLine();
        }
        debugMode = scannerInput.equals("Y"); //At this point, because of the while loop, there are two options— either "Y" or "N". This means we can simplify a possible if/else.

        System.out.println("Select a difficulty! The available options are 'Easy', 'Medium', 'Hard' (case sensitive).");
        scannerInput = scanner.nextLine();
        while (!(scannerInput.equals("Easy") || scannerInput.equals("Medium") || scannerInput.equals("Hard"))) {
            System.out.println("Invalid selection! The available options are: 'Easy', 'Medium', 'Hard' (case sensitive).");
            scannerInput = scanner.nextLine();
        }

        if(scannerInput.equals("Easy")){
            minefield = new Minefield(5, 5, 5);
            mapSize = 5;
            totalMines = 5;
        } else if(scannerInput.equals("Medium")){
            minefield = new Minefield(9, 9, 12);
            mapSize = 9;
            totalMines = 12;
        } else { //At this point, due to the while loop, we can assume that this will always be the 'Hard' difficulty selection.
            minefield = new Minefield(20, 20, 40);
            mapSize = 20;
            totalMines = 40;
        }

        System.out.println(minefield);

        int xPos = 0;
        int yPos = 0;
        boolean flag = false;
        boolean setup = false;

        while(!setup){
            System.out.println("Select a row (side numbers). This can be from 0 to " + (mapSize-1) + ".");
            xPos = scanner.nextInt();
            while(xPos < 0 || xPos > mapSize){ //Covers invalid guesses. We will assume that players only put in integer guesses.
                System.out.println("Invalid selection! Ensure that it is in the range of 0 to " + (mapSize-1) + ".");
                xPos = scanner.nextInt();
            }
            System.out.println("Select a column (top numbers). This can be from 0 to " + (mapSize-1) + ".");
            yPos = scanner.nextInt();
            while(yPos < 0 || yPos > mapSize){ //Covers invalid guesses. We will assume that players only put in integer guesses.
                System.out.println("Invalid selection! Ensure that it is in the range of 0 to " + (mapSize-1) + ".");
                yPos = scanner.nextInt();
            }
            setup = true;
        }

        minefield.createMines(xPos, yPos, totalMines);
        minefield.evaluateField();
        minefield.revealStartingArea(xPos, yPos);

        if (debugMode){
            minefield.debug();
        }
        System.out.println(minefield);

        //Begin actual game loop
        while(!minefield.gameOver()) {
            boolean validGuess = false;
            while(!validGuess){
                System.out.println("Select a row (side numbers). This can be from 0 to " + (mapSize-1) + ".");
                xPos = scanner.nextInt();
                while(xPos < 0 || xPos > mapSize){ //Covers invalid guesses. We will assume that players only put in integer guesses.
                    System.out.println("Invalid selection! Ensure that it is in the range of 0 to " + (mapSize-1) + ".");
                    xPos = scanner.nextInt();
                }
                System.out.println("Select a column (top numbers). This can be from 0 to " + (mapSize-1) + ".");
                yPos = scanner.nextInt();
                while(yPos < 0 || yPos > mapSize){ //Covers invalid guesses. We will assume that players only put in integer guesses.
                    System.out.println("Invalid selection! Ensure that it is in the range of 0 to " + (mapSize-1) + ".");
                    yPos = scanner.nextInt();
                }
                System.out.println("Type 'true' to toggle the flag status of a cell, 'false' otherwise.");
                flag = scanner.nextBoolean();

                if(!(minefield.board[xPos][yPos].getRevealed() && !flag)){ //Checks if the cell being guessed is already revealed. If it isn't, it's a valid guess!
                    validGuess = true;
                    turns += 1;
                } else {
                    System.out.println("That cell is already revealed! Please make another guess.");
                }

            }
            minefield.guess(xPos, yPos, flag);
            if (debugMode) {
                minefield.debug();
            }
            System.out.println(minefield);
        }
        System.out.println("The game ended in " + turns + " turns!");
    }
}