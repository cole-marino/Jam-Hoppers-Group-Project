package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import java.util.*;

/**
 * This is a config for the hoppers game
 * @author JAY PRUITT
 */
public class HoppersConfig implements Configuration{
    /** a cell that does not have a frog in it */
    private final static String EMPTY = ".";
    /** a blank cell */
    private final static String BLANK = "*";
    /** a green cell */
    private final static String GREEN = "G";
    /** a red cell */
    private final static String RED = "R";
    private Collection<Configuration> neighbors = new LinkedList<>();
    private String[][] board;
    private int rows,cols;
    private List<Frog> allFrogs;

    /**
     * This is the constructor for the config, it stores the board, and the rows and columns of the board.
     * @param board the board of values
     * @param rows the number of rows in th board
     * @param cols the number of columns in the board
     */
    public HoppersConfig(String[][] board,int rows,int cols){
        this.rows=rows;
        this.cols=cols;
        this.board= new String[rows][cols];
        List<Frog> allFrogs = new LinkedList<>();
        for (int r=0; r<this.rows; r++) {
            System.arraycopy(board[r], 0, this.board[r],
                    0, this.cols);
        }
        for(int i=0;i<rows;i++) {
            for (int i2 = 0; i2 < cols; i2++) {
                if (board[i][i2].equals("G") || board[i][i2].equals("R")) {
                    allFrogs.add(new Frog(board[i][i2], i, i2));
                }
            }
        }
        this.allFrogs=allFrogs;
        Solver.totalConfigs++;
        }

    /**
     * this gets the number of rows in the grid
     * @return the number of rows in the grid
     */
    public int getRows() {
        return rows;
    }

    /**
     * this gets the number of columns in the grid
     * @return the number of columns in the grid
     */
    public int getCols() {
        return cols;
    }

    /**
     * This gets the value of a certain cell in the grid for a row and a column
     * @return the value of the cell at the coordinates specified
     */
    public String getVal(int row, int col) {
        return board[row][col];
    }

    /**
     * This gets the whole board double array
     * @return the double array board
     */
    public String[][] getBoard(){
        return board;
    }

    /**
     * This checks the solution by making sure that only one frog is left and it is red.
     * @return true, if the current config is the solution
     */
    @Override
    public boolean isSolution() {
        if(allFrogs.size()==1&&allFrogs.get(0).toString().equals(RED)){
            return true;
        }
        return false;
    }

    /**
     * this creates the list of neighbors by populating it with every possible move.
     * @return the list of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        for (int i=0;i<this.allFrogs.size();i++) {
            Frog frog=allFrogs.get(i);
            if(frog.getCol()%2==0&&frog.getRow()%2==0){
                checkIfCrossed(frog,false,false,0); //down right
                checkIfCrossed(frog,false,true,0); //down left
                checkIfCrossed(frog,true,false,0); // up right
                checkIfCrossed(frog,true,true,0); // up left
                checkIfCrossed(frog,true,false,1); // straight up
                checkIfCrossed(frog,false,true,3); // straight left
                checkIfCrossed(frog,false,false,2); // straight down
                checkIfCrossed(frog,false,false,4); // straight right
                allFrogs=makeNewFrogs(board);
            } else if(frog.getCol()%2==1&&frog.getRow()%2==1){
                checkIfCrossed(frog,false,false,0); //down right
                checkIfCrossed(frog,false,true,0); // down left
                checkIfCrossed(frog,true,false,0); // up right
                checkIfCrossed(frog,true,true,0); // up left
                allFrogs=makeNewFrogs(board);

            }
        } return this.neighbors;
    }

    /**
     * This makes new list of frogs based on the board
     * @param board the board to check for frogs
     * @return the new list of frogs
     */
    private List<Frog> makeNewFrogs(String[][] board){
        List<Frog> newFrogs = new LinkedList<>();
        for(int i=0;i<rows;i++) {
            for (int i2 = 0; i2 < cols; i2++) {
                if (board[i][i2].equals("G") || board[i][i2].equals("R")) {
                    newFrogs.add(new Frog(board[i][i2], i, i2));
                }
            }
        }
        return newFrogs;
    }

    /**
     * This checks if a frog is crossed when creating neighbors, and moves the frog if it passes all the checks.
     * @param frog This is the frog to be checked against
     * @param up if the frog is traveling up
     * @param left if the frog is traveling left
     * @param straight if the frog is going straight, 1 is up, 2 left, 3 down, 4 right
     * @return True if successful crossing, false otherwise
     */
    public boolean checkIfCrossed(Frog frog,Boolean up, Boolean left, int straight){
        int dircol;
        int dirrow;
        String[][]grid=new String[rows][cols];
        for (int r=0; r<this.rows; r++) {
            System.arraycopy(this.board[r], 0, grid[r],
                    0, this.cols);
        }
        if(up){dirrow=-2; if(straight==1) dirrow-=2;} else{dirrow=2; if(straight==2)dirrow+=2;}
        if(straight>2) dirrow=0;
        if(left){ dircol=-2; if(straight==3) dircol-=2;} else{dircol=2; if(straight==4)dircol+=2;}
        if(straight<3&&straight!=0) dircol=0;
        try {
            if (grid[frog.getRow() + (dirrow/2)][frog.getCol() + (dircol/2)].equals("G")) {
                if(grid[frog.getRow()+dirrow][frog.getCol()+dircol].equals(EMPTY)){
                    if(!grid[frog.getRow()+(dirrow/2)][frog.getCol()+(dircol/2)].equals(RED)) {
                        cross(frog,grid,dirrow,dircol);
                        return true;
                    }
                }
            }
        }catch(IndexOutOfBoundsException e){
            return false;
        }
        return false;
    }

    /**
     * This moves the frog by altering the double array, board
     * @param frog the frog that is being changed
     * @param grid this is the grid that is new and being changed
     * @param dirrow this is the number of rows the frog is moving
     * @param dircol this is the number of cols the frog is moving
     */
    public void cross(Frog frog, String[][]grid, int dirrow, int dircol){
        grid[frog.getRow() + dirrow][frog.getCol() + dircol] = grid[frog.getRow()][frog.getCol()];
        grid[frog.getRow()][frog.getCol()] = EMPTY;
        grid[frog.getRow() + (dirrow/2)][frog.getCol() + (dircol/2)] = EMPTY;
        this.neighbors.add(new HoppersConfig(grid,rows,cols));
    }

    /**
     * This returns the current config as a string
     * @return the current config as a string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n");

        for (int row = 0; row < getRows(); ++row) {
            for (int col = 0; col < getCols(); ++col) {
                result.append(getVal(row, col));
                if (col < getCols() - 1) {
                    result.append(" ");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     *  Two Nodes are equal if they have the same toString
     *  @param other The other object to check equality with
     *  @return true if equal; false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other.toString().equals(this.toString())) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return the hash code for the current config
     */
    public int hashCode() {
        return this.toString().hashCode();
    }



}
