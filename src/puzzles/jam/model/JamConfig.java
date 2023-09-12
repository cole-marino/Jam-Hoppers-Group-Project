package puzzles.jam.model;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.*;

/**
 * basically the configuration for the entire jam game
 *
 * @author Cole Marino
 * @author cvm4043
 */
public class JamConfig implements Configuration{

    // what is EMPTY?? [in board]
    public static final String EMPTY = ".";

    private String[][] board;
    //private List<Car> allCars = new LinkedList<>();

    private int row, endCol;
    protected int currentCol;
    private Map<String, Car> allCars = new HashMap<>();
    private Collection<Configuration> neighbors = new LinkedList<>();

    // basically red car needs to be in last col and is always length 2
    /**
     * creates a new configuration of the board
     *
     * @param board the current board configuration
     * @param currentCol current front (eCol) of the "X" car
     * @param endCol basically just the last column always
     */
    public JamConfig(Map<String, Car> allCars, String[][] board, int currentCol, int endCol){
        this.allCars = allCars;
        this.board = board;
        this.currentCol = currentCol;
        this.endCol = endCol;
        Solver.totalConfigs++;
        //System.out.println(this.toString());
    }

    @Override
    public boolean isSolution() {
        if(allCars.get("X").getTiles()[1][0] == endCol)
            return true;
        else
            return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        int count=0;
        Car newCar1 = null, newCar2 = null;
        // loops through all the cars and checks for possible moves
        for(Car car: allCars.values()){
            JamConfig jc1 = null, jc2 = null;

            if(car.getIfCollide(this.board, true)){
                newCar1 = car.getNewMovedCar(true);
                Map<String, Car> newAllCars = initAllCars();
                newAllCars.replace(newCar1.getName(), newCar1);
                if(car.getName().equals("X"))
                    jc1 = new JamConfig(newAllCars, newBoard(newAllCars), this.currentCol+1, this.endCol);
                else
                    jc1 = new JamConfig(newAllCars, newBoard(newAllCars), this.currentCol, this.endCol);
                neighbors.add(jc1);
            }
            if(car.getIfCollide(this.board, false)){
                newCar2 = car.getNewMovedCar(false);
                Map<String, Car> newAllCars = initAllCars();
                newAllCars.replace(newCar2.getName(), newCar2);
                if(car.getName().equals("X"))
                    jc2 = new JamConfig(newAllCars, newBoard(newAllCars), this.currentCol+1, this.endCol);
                else
                    jc2 = new JamConfig(newAllCars, newBoard(newAllCars), this.currentCol, this.endCol);
                neighbors.add(jc2);
            }
            count++;
        }
        return this.neighbors;
    }

    /**
     * initializes a new map for the cars
     *
     * @return a new map of the cars!!
     */
    protected Map<String, Car> initAllCars(){
        Map<String, Car> list = new HashMap<>();
        for(Car c: allCars.values())
            list.put(c.getName(), c);
        return list;
    }

    /**
     * creates a new board with the given map of cars
     *      > uses the coordinates unique to each car to plot
     *          their location with it's coordinates
     *
     * @param cars a map of all the cars and their locations
     * @return a 2D string of all the cars being represented by their NAMEEE
     */
    protected String[][] newBoard(Map<String, Car> cars){
        int cols = board.length, rows = board[0].length;
        String[][] newBoard = createDefaultBoard(rows, cols);

        for(Car car: cars.values()){
            //System.out.println("h   " + car.toString());

            for(int[] t: car.getTiles()){
                //System.out.println(car.getName());
                //System.out.print(t[0]);
                //System.out.println(t[1]);
                int col = t[0], row = t[1];
                newBoard[col][row] = car.getName();
            }
        }

        return newBoard;
    }

    /**
     * basically fills the board with empty spaces
     */
    private String[][] createDefaultBoard(int rows, int cols){
        String[][] b = new String[cols][rows];
        for(int i=0; i<cols; i++)
            for(int j=0; j<rows; j++)
                b[i][j] = ".";
        return b;
    }

    /**
     * returns the board for this current stage of the game!!
     *
     * @return a 2D string array which represents the CARS!
     */
    public String[][] getBoard(){
        return this.board;
    }

    /**
     * returns the map of all the cars on the board
     *
     * @return the map of all the cars!
     */
    public Map<String, Car> getCars(){
        return this.allCars;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof JamConfig){
            JamConfig jc = (JamConfig)(o);

            if((jc.toString()).equals(this.toString()))
                return true;
            else
                return false;
        }

        return false;
    }

    @Override
    public int hashCode(){
        return this.toString().hashCode();
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


    /**
     * doin' your mom
     *  doin' -- doin' your mom
     */
}
