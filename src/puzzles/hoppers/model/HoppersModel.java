package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.solver.Hoppers;
import puzzles.jam.model.JamConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is the hoppersmodel for the MVC for the program
 *
 * @author Jay Pruitt
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    private String filename;
    private List<Configuration> path;
    private Configuration end;
    private Hoppers hoppers;
    private int[] cursor;

    /**
     * The view calls this to add itself as an observer.
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
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
     * This resets the model by reloading the current file
     */
    public void reset() {
        this.load(filename);
        alertObservers("Puzzle reset!");
    }

    /**
     * This selects a single point in the board. Then on the second selection, it moves a frog on the board if
     * it is a valid move
     * @param row the row to be selected
     * @param col the column to be selected
     */
    public void select(int row, int col) {
        if(this.cursor==null){
            try {
                if (currentConfig.getVal(row,col).equals("G") || currentConfig.getVal(row,col).equals("R")) {
                    alertObservers("Selected (" + row + ", " + col + ")");
                    System.out.println(currentConfig.toString());
                    this.cursor=new int[2];
                    this.cursor[0]=row;
                    this.cursor[1]=col;
                } else{this.cursor=null; alertObservers("not a frog");}
            }catch(IndexOutOfBoundsException e){alertObservers("Out of bounds");this.cursor=null;}
        } else{
            try {
                if (!currentConfig.getVal(row,col).equals("G") && !currentConfig.getVal(row,col).equals("R")) {
                    this.moveFrog(cursor[0],cursor[1],row,col);
                    System.out.println(currentConfig.toString());
                    this.cursor=null;
                } else{ alertObservers("spot is taken");}
            }catch(IndexOutOfBoundsException e){alertObservers("out of bounds");}
        }
    }

    /**
     * This moves a frog on the board by verifying it can move to that position without breaking rules
     * @param startRow the starting row of the frog
     * @param startCol the starting column of the frog
     * @param endRow the ending row of the frog
     * @param endCol the ending column of the frog
     */
    private void moveFrog(int startRow, int startCol, int endRow, int endCol) {
        int dircol=endCol-startCol;
        int dirrow=endRow-startRow;
        int tot=Math.abs(endCol-startCol)+Math.abs(endRow-startRow);
        int rows=currentConfig.getRows();
        int cols=currentConfig.getCols();
        String[][] board=currentConfig.getBoard();
        String[][]grid=new String[rows][cols];
        if(tot==4){
        for (int r=0; r<rows; r++) {
            System.arraycopy(board[r], 0, grid[r], 0, cols);}
        try {
            if (grid[startRow + (dirrow/2)][startCol + (dircol/2)].equals("G")) {
                if(grid[startRow+dirrow][startCol+dircol].equals(".")){
                    if(!grid[startRow+(dirrow/2)][startCol+(dircol/2)].equals("R")) {
                        grid[startRow + dirrow][startCol + dircol] = grid[startRow][startCol];
                        grid[startRow][startCol] = ".";
                        grid[startRow + (dirrow/2)][startCol + (dircol/2)] = ".";
                        currentConfig = new HoppersConfig(grid,rows,cols);
                        alertObservers("Jumped from (" + startRow + ", " + startCol + ") to ("
                                + endRow + ", " + endCol+ ")");
                    }else{ alertObservers("Cannot jump the red frog"); }
                } else{ alertObservers("Spot taken"); }
            } else{ alertObservers("No frog to jump over"); }
        }catch(IndexOutOfBoundsException e){
            alertObservers("Out of bounds");
        }
    }else{alertObservers("Invalid move");}
    }

    /**
     * This loads a file and creates a new board from that level. Then it creates a new model and solves it.
     * @param arg
     */
    public void load(String arg) {
        this.filename=arg;
        try (BufferedReader bf = new BufferedReader(new FileReader(filename))) {
            String[] line = bf.readLine().split(" ");
            int rows = Integer.parseInt(line[0]);
            int cols = Integer.parseInt(line[1]);
            String[][] board = new String[rows][cols];
            for(int i=0;i<rows;i++) {
                line = bf.readLine().split(" ");
                for(int i2=0;i2<cols;i2++) {
                    board[i][i2] = line[i2];
                }
            }
            this.hoppers = new Hoppers(filename);
            this.currentConfig = new HoppersConfig(hoppers.board,hoppers.rows,hoppers.cols);
            this.end = new HoppersConfig(hoppers.board,hoppers.rows,hoppers.cols);
            Solver solver = new Solver(currentConfig, end);
            this.path=new LinkedList<>();
            this.path = solver.solve(currentConfig, end);
//            alertObservers(filename+" successfully loaded");
            System.out.println(currentConfig.toString());
        }catch(IOException e){
            System.out.println("Given file could not be read!");
            System.exit(-1);
        }
    }

    /**
     * This gets a hint updates the current gonfig to the next step in the solution
     */
    public void getHint() {
        Solver solver = new Solver(currentConfig, end);
        this.path=new LinkedList<>();
        this.path = solver.solve(currentConfig, end);
            if (path.size()>1){
            this.currentConfig = (HoppersConfig) path.get(1);
            alertObservers("Hint successful!");
            System.out.println(path.get(1).toString());
        } else{ alertObservers("Cannot give any hints!");}
        if(path.get(0).isSolution()) alertObservers("You won!");
    }

    /**
     * This prints out the help for the functions available
     */
    public void help() {
        System.out.println(
                        "    h(int)              -- hint next move\n" +
                        "    l(oad) filename     -- load new puzzle file\n" +
                        "    s(elect) r c        -- select cell at r, c\n" +
                        "    r(eset)             -- reset the current game\n"+
                        "    quit                -- quit the game"
        );
    }

    /**
     * This gets a value at a coordinate on the board
     * @param row the row of the value
     * @param col the column of the value
     * @return the value at those coordinates
     */
    public String getVal(int row,int col){
        return currentConfig.getVal(row, col);
    }

    /**
     * These return the number of  rows and the columns of the current config
     * @return the number of rows or columns
     */
    public int getRows(){ return currentConfig.getRows(); }
    public int getCols(){ return currentConfig.getCols(); }

}
