package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.Frog;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.jam.model.Car;
import puzzles.jam.solver.Jam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the hoppers class that initializes the hoppersconfig
 * @author Jay Pruitt
 */
public class Hoppers {

    public static String[][] board;
    public static int rows, cols;
    private int startPos, startRow;     // {[startCol],[startRow]}
    private String filename;
    private List<Frog> frogs = new LinkedList<>();

    public Hoppers(String filename) {
        try (BufferedReader bf = new BufferedReader(new FileReader(filename))) {
            String[] line = bf.readLine().split(" ");
            this.rows = Integer.parseInt(line[0]);
            this.cols = Integer.parseInt(line[1]);
            this.board = new String[rows][cols];
//            for loop for each row
//            init the actual grid using the dimension in step 0
            for(int i=0;i<rows;i++) {
                line = bf.readLine().split(" ");
                for(int i2=0;i2<cols;i2++) {
                    this.board[i][i2] = line[i2];
                    if (line[i2].equals("G")|| line[i2].equals("R")) {
                        this.frogs.add(new Frog(line[i2],i,i2));
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Given file could not be read!");
            System.exit(-1);
        }
    }

/*    public Hoppers(String filename){
        this.filename = filename;
        try(BufferedReader bf = new BufferedReader(new FileReader(filename))){
            String[] size = bf.readLine().split(" ");
            rows = Integer.valueOf(size[0]);
            cols = Integer.valueOf(size[1]);
            this.board = new String[cols][rows];
            this.fillDefaultBoard();    // basically makes the board a bunch of empty dots

            *//**  Last car entry is always the red car which is length 2 and horizontal  **//*
            for(int i=0; i<rows; i++){
                String[] line = bf.readLine().split(" ");
                this.enterFrogInBoard(line[0], Integer.valueOf(line[1]), Integer.valueOf(line[2]));
                this.addNewFrog(line[0], Integer.valueOf(line[1]), Integer.valueOf(line[2]));

                if(line[0].equals("X")) { // gets start value of car
                    this.startPos = Integer.valueOf(line[4]);
                    this.startRow = Integer.valueOf(line[1]);

                }
            }

        }catch(IOException e){
            System.out.println("Given file could not be read!");
            System.exit(-1);
        }
    }*/

    public static void main(String[] args){
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        } else {

            //first need to load file

                /*

                I don't know if I am supposed to load the file and make the grid here
                or if I do it in the constructor.
                I don't know what the end config is supposed to be.
                I don't get what neighbors are.
                Are neighbors supposed to be successors or the actual grid neighbors?

                 */

//                int pups = Integer.valueOf(args[0]), wolves = Integer.valueOf(args[1]);
//                int[] leftCount = new int[2];
//                int[] rightCount = new int[2];
//                leftCount[0] = pups;
//                leftCount[1] = wolves;

            Hoppers hoppers = new Hoppers(args[0]);
            System.out.println(hoppers.toString());
            Configuration start = new HoppersConfig(hoppers.board,rows,cols);
            Configuration end = new HoppersConfig(hoppers.board,rows,cols);
            Solver solver = new Solver(start, end);
            List<Configuration> path = solver.solve(start, end);

            // prints all the info
//                System.out.println("Start: " + args[0] + ", Finish: " + args[0]);
            System.out.println("Total configs: " + Solver.totalConfigs);
            System.out.println("Unique configs: " + solver.getTotalUniqueConfigs());

            // prints the path
            if (path != null)
                for (int i = 0; i < path.size(); i++)
                    if(path.get(i)!=null)
                    System.out.println("Step " + i + ": " + path.get(i));
            else
                System.out.println("No solution");

        }
    }


    private void addNewFrog(String type, int row, int col){
        frogs.add(new Frog(type, row, col));
    }

    /**
     * enters a car into the board.
     * If the car is not 2 tiles long, then it is 3 tiles and the middle tile is added
     *
     * @param type the type of frog
     * @param row the starting row
     * @param col the starting column
     */

    private void enterFrogInBoard(String type, int row, int col){
        board[col][row] = type;
    }


    /**
     * basically fills the board with empty spaces
     */

    private void fillDefaultBoard(){
        for(int i=0; i<cols; i++)
            for(int j=0; j<rows; j++)
                board[j][i] = ".";
    }

    public int getCols(){
        return cols;
    }
    public int getRows(){
        return rows;
    }

    @Override
    public String toString(){
        String out="";
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                out+=board[i][j];
                out+=" ";
            }
            out+="\n";
        }
        return out;
    }

}