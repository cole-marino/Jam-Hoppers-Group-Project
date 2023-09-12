package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * the model representing a GAMINHG GAMEEE
 *
 * @author Cole Marino
 * @author cvm4043
 */
public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig, endConfig;

    private int currentCol=1;
    private Car endCar;

    protected String filename;

    protected String[][] board;
    public static int rows, cols, numCars;
    private int startPos, startRow;     // {[startCol],[startRow]}
    private Map<String, Car> cars = new HashMap<>();

    /** boolean whether the user already made a tile selection */
    private int[] select1 = null, select2 = null;
    private Car selectedCar = null;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * selects a car to be moved
     *  > if a car was already selected then the car is moved to the new selected tile
     *
     * @pre row and col are integers
     *
     * @param row the row being selected
     * @param col the column being selected
     */
    public void select(int row, int col){
        if(this.checkIfWon()){
            alertObservers("Already won!");
            return;
        }
        // verifies that selection is valid
        if(row>=rows || col>=cols){
            System.out.println("Invalid tile on board!");
            return;
        }
        Map<String, Car> allCars = currentConfig.getCars();
        String tileName = board[col][row];
        // if a tile was not already selected
        if(select1==null){
            if(tileName.equals(JamConfig.EMPTY)){
                alertObservers("Tile is empty, try again.");
                return;
            }
            this.selectedCar = allCars.get(tileName);
            this.select1 = new int[2];
            this.select1[0] = col;
            this.select1[1] = row;

            alertObservers("Selected (" + select1[1] + ", " + select1[0] + ")");

        }else{  // a tile is already selected
            this.select2 = new int[2];
            this.select2[0] = col;
            this.select2[1] = row;
            int[][] tiles = selectedCar.getTiles();

            /** I know this looks like hell but it works and I'll fix it if I have time */
            if(selectedCar.getFacing()){    // horizontal
                if(col == tiles[tiles.length-1][0]+1 && row == tiles[0][1]){ // if moving right
                    this.moveSelectedCar(true);
                }else if(col == tiles[0][0]-1 && row == tiles[0][1]){     // moving left
                    this.moveSelectedCar(false);
                }else{
                    alertObservers("Can't move from (" + select1[1] + ", " + select1[0] + ") to ("
                            + select2[1] + ", " + select2[0] + ")");
                    select2 = null;
                    return;
                }
            }else{  // vertical
                if(row == tiles[0][1]-1 && col == tiles[0][0]){ // if moving up
                    this.moveSelectedCar(true);
                }else if(row == tiles[tiles.length-1][1]+1 && col == tiles[0][0]){     // moving down
                    this.moveSelectedCar(false);
                }else{
                    alertObservers("Can't move from (" + select1[1] + ", " + select1[0] + ") to ("
                            + select2[1] + ", " + select2[0] + ")");
                    select2 = null;
                    return;
                }
            }

            // prints board again
            System.out.println(this.toString());

            // re-initializes the values because they are no longer needed
            selectedCar = null;
            select1 = null;
            select2 = null;

            if(this.checkIfWon()){
                alertObservers("Game won!");
            }
        }


    }

    /**
     * helper function to select()
     *
     * @param dir the direction which the car is moving
     */
    private void moveSelectedCar(boolean dir){
        if(selectedCar.getIfCollide(board, dir)){
            Car newCar = selectedCar.getNewMovedCar(dir);
            this.cars = currentConfig.initAllCars();
            cars.replace(newCar.getName(), newCar);
            this.board = currentConfig.newBoard(cars);

            // changes the current column of the red car if it was moved!!!
            if(selectedCar.getName().equals("X")){
                if(dir)
                    this.currentCol++;
                else
                    this.currentCol--;
            }

            currentConfig = new JamConfig(cars, this.board, this.currentCol, cols-1);

            alertObservers("Moved from (" + select1[0] + ", " + select1[1] + ") to ("
                    + select2[0] + ", " + select2[1] + ")");
            select1 = null;
            select2 = null;
            selectedCar = null;
        }else{
            alertObservers("Can't move from (" + select1[0] + ", " + select1[1] + ") to ("
                    + select2[0] + ", " + select2[1] + ")");
            select2 = null;
            return;
        }

    }

    /**
     * progresses the game by 1 turn (gives the user a [hint] [not clickbait] [real])
     */
    public void hint(){
        if(this.checkIfWon()){
            alertObservers("Already won!");
            return;
        }
        if(endConfig == null){
            //currentConfig = new JamConfig(cars, board,currentCol, cols-1);
            endConfig = new JamConfig(cars, board, cols-1, cols-1);
        }else{
            //currentConfig = new JamConfig(cars, board, currentCol, cols-1);
        }
        Solver solver = new Solver(currentConfig, endConfig);
        List<Configuration> path = solver.solve(currentConfig, endConfig);
        if(path.size()>1){
            currentConfig = (JamConfig)(path.get(1));
            this.board = currentConfig.getBoard();
            System.out.println(currentConfig.toString());
        }else{
            if(currentConfig.isSolution()){
                System.out.println(currentConfig.toString());
            }else{
                alertObservers("No solution!");
            }
        }
        // I know it's redundant but I just want to get it to work first!
        if(this.checkIfWon()){
            alertObservers("Already won!");
            return;
        }else
            alertObservers("Next step!");

    }

    /**
     * LOADS THE FILE NAME FILE FILE NAME HLEP
     *
     * @param filename the filename of the file being used
     */
    public void load(String filename){
        this.filename = filename;
        this.board = null;
        this.cars = new HashMap<>();
        try(BufferedReader bf = new BufferedReader(new FileReader(filename))){
            String[] size = bf.readLine().split(" ");
            rows = Integer.valueOf(size[0]);
            cols = Integer.valueOf(size[1]);
            this.board = new String[cols][rows];
            this.fillDefaultBoard();    // basically makes the board a bunch of empty dots
            numCars = Integer.valueOf(bf.readLine());

            /**  Last car entry is always the red car which is length 2 and horizontal  **/
            for(int i=0; i<numCars; i++){
                String[] car = bf.readLine().split(" ");
                this.enterCarInBoard(car[0], Integer.valueOf(car[1]),
                        Integer.valueOf(car[2]), Integer.valueOf(car[3]), Integer.valueOf(car[4]));
                this.addNewCar(car[0], Integer.valueOf(car[1]),
                        Integer.valueOf(car[2]), Integer.valueOf(car[3]), Integer.valueOf(car[4]));

                if(car[0].equals("X")) { // gets start value of car
                    this.startPos = Integer.valueOf(car[4]);
                    this.startRow = Integer.valueOf(car[1]);
                    this.endCar = this.cars.get(cars.size()-1);
                }
            }

            this.currentConfig = new JamConfig(cars, board,0, cols-1);
            this.endConfig = new JamConfig(cars, board, cols-1, cols-1);

        }catch(IOException e){
            System.out.println("Given file could not be read!");
            System.exit(-1);
        }
        alertObservers("Loaded....");
    }


    /**
     * resets the video gaming!!! (board) [balls]s]s]s]]sw]
     */
    public void reset(){
        select1 = null;
        select2 = null;
        this.load(this.filename);
    }

    /**
     * checks if the game has been already won and prints if it was
     *
     * @return true/false whether the game has been won before
     */
    private boolean checkIfWon(){
        if(currentConfig.isSolution()){
            System.out.println("Already solved!");
            System.out.println(currentConfig.toString());
            return true;
        }
        return false;
    }


    /**
     * adds a new car to the list of all cars
     *
     * @param name the name of the car
     * @param sRow the starting row of the car
     * @param sCol the starting column
     * @param eRow the ending row
     * @param eCol the ending column
     */
    private void addNewCar(String name, int sRow, int sCol, int eRow, int eCol){
        boolean facing = false;
        if(name.equals('x'))
            name = "X";

        if(sCol == eCol)
            facing = false;  // vertical
        else
            facing = true;  // horizontal

        cars.put(name, new Car(name, sRow, sCol, eRow, eCol, facing));
    }


    /**
     * enters a car into the board.
     * If the car is not 2 tiles long, then it is 3 tiles and the middle tile is added
     *
     * @param name the name of the car (ex: "X" for the victory car)
     * @param sRow the starting row
     * @param sCol the starting column
     * @param eRow the ending row
     * @param eCol the ending column
     */
    private void enterCarInBoard(String name, int sRow, int sCol, int eRow, int eCol){
        board[sCol][sRow] = name;
        board[eCol][eRow] = name;

        // if the car is 2 tiles long, if it is 3 tiles then the third tile is added
        if(Math.abs(sCol-eCol) == 1 || Math.abs(sRow-eRow) == 1)
            return;
        else
            if(sCol == eCol)   // if car is vertical
                board[sCol][sRow+1] = name;
            else
                board[sCol+1][sRow] = name;
    }

    /**
     * basically fills the board with empty spaces
     */
    private void fillDefaultBoard(){
        for(int i=0; i<cols; i++)
            for(int j=0; j<rows; j++)
                board[i][j] = ".";
    }

    /**
     * gets the rows of the board
     * @return the amount of rows in the board
     */
    public int getRows(){
        return rows;
    }

    /**
     * gets the amount of columns
     * @return integer of the amount of columns
     */
    public int getCols(){
        return cols;
    }

    /**
     * gets the current game board
     * @return 2D string array representing the GAMING
     */
    public String[][] getBoard(){
        return this.board;
    }



    @Override
    public String toString(){
        String out = "   ";

        for(int i=0; i<board.length; i++)
            out+= i + " ";
        out+="\n  ";
        for(int i=0; i<board.length*2; i++)
            out+= "-";
        out+="\n";

        for(int i=0; i<board[0].length; i++){
            out+= i + "| ";
            for(int j=0; j<board.length; j++){
                out += board[j][i];
                if(!(j==board.length-1))
                    out+=" ";
            }
            out+="\n";

        }
        return out;
    }
}
