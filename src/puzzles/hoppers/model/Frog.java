package puzzles.hoppers.model;

/**
 * This class is for the frog object.
 * @author Jay Pruitt
 */
public class Frog {
    private String type;
    private int row;
    private int col;

    /**
     * This creates a frog
     * @param type the type of frog, red or green
     * @param row the row of the frog
     * @param col the column of the frog
     */
    public Frog(String type, int row, int col) {
        this.type=type;
        this.row=row;
        this.col=col;
    }

    /**
     * These get the row or the column of the frog
     * @return the row or the column of the frog
     */
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }

    /**
     * this returns the string representation of the frog, which is its type
     * @return the type of frog
     */
    @Override
    public String toString(){
        return this.type;
    }
}
