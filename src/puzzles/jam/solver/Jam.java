package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.Car;
import puzzles.jam.model.JamConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * The home for the Jam game - essentially a main director
 * 
 * @author: Cole Marino
 * 
 */

public class Jam {

    protected String[][] board;
    protected int rows, cols, numCars;
    private int startPos, startRow;     // {[startCol],[startRow]}
    protected String filename;
    private Map<String, Car>  cars = new HashMap<>();

    protected Car endCar = null;

    public Jam(String filename){
        this.filename = filename;
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

        }catch(IOException e){
            System.out.println("Given file could not be read!");
            System.exit(-1);
        }
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        }else{
            Jam jam = new Jam(args[0]);

            System.out.println("File: " + jam.filename);
            System.out.println(jam.toString());

            JamConfig start = new JamConfig(jam.cars, jam.board,0, jam.cols-1);
            JamConfig end = new JamConfig(jam.cars, jam.board, jam.cols-1, jam.cols-1);
            Solver solver = new Solver(start, end);


            List<Configuration> path = solver.solve(start, end);
            System.out.println("Total configs: " + Solver.totalConfigs);
            System.out.println("Unique configs: " + (solver.getTotalUniqueConfigs()-1));

            if(path.size()>2){
                for(int i=0; i<path.size(); i++){
                    if(path.get(i) != null){
                        System.out.println("Step " + (i) + ":\n" + path.get(i).toString());
                    }
                }
            }else{
                System.out.println("No solution");
            }

            //System.out.println(path.get(path.size()-1));

        }
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

    @Override
    public String toString(){
        String out="";
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                out+=board[j][i];
                if(j!=rows-1)
                    out+=" ";
            }
            if(i<rows-1)
                out+="\n";
        }
        return out;
    }
}