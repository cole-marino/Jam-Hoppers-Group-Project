package puzzles.jam.model;

import puzzles.common.solver.Solver;
import puzzles.jam.solver.Jam;

/**
 * a node which represents a car in the jam game
 *
 * @author Cole Marino
 * @author cvm4043
 */
public class Car {

    private String name;
    private int[][] tiles;  // in [x, y] format where x is either 0 or 1 || 0 --> col
    private int sRow, sCol, eRow, eCol;
    private boolean facing; // true for horizontal, false for vertical


    /**
     * constructor, initializes all the global stuff!
     *
     * @param name NAME CAR OF CAR NAME
     * @param sRow starting row
     * @param sCol starting column
     * @param eRow ending row
     * @param eCol ending column
     * @param facing the direction the car is facing
     */
    public Car(String name, int sRow, int sCol, int eRow, int eCol, boolean facing){
        this.name = name;
        this.facing = facing;   // true for horizontal, false for vertical
        this.sRow = sRow;
        this.sCol = sCol;
        this.eRow = eRow;
        this.eCol = eCol;
        this.makeTilesList(sRow, sCol, eRow, eCol);
        //System.out.println("    " + this.toString());
    }

    /**
     * returns whether the car is able to move in a specific direction
     *
     * @param board the current board of the game
     *               > used to determine if the car is able to move
     * @param dir the direction which the car is moving
     *               > true
     *                  +> up
     *                  +> right
     *               > false
     *                  +> down
     *                  +> left
     *
     * @return a boolean representing whether the car can move and where
     *           > true ---> car can move
     *           > false --> car cannot move
     */
    public boolean getIfCollide(String[][] board, boolean dir){
        boolean out = false;
        if(!facing){
            if(dir){    // go up
                for(int[] t: tiles){
                    if(t[1] <= 0)
                        return false;
                    else if (board[t[0]][t[1]-1].equals(JamConfig.EMPTY))
                        return true;
                }
            }else{      // go down
                for(int[] t: tiles){
                    if ((t[1]+1 >= board[0].length))
                        return false;
                    else if (board[t[0]][t[1]+1].equals(JamConfig.EMPTY))
                        return true;
                }
            }
        }else{
            if(dir){    // go right
                for(int[] t: tiles){
                    if((t[0]+1 >= board.length))
                        return false;
                    else if (board[t[0] + 1][t[1]].equals(JamConfig.EMPTY))
                        return true;
                }
            }else{      // go left
                for(int[] t: tiles){
                    if(t[0] <= 0)
                        return false;
                    else if (board[t[0] - 1][t[1]].equals(JamConfig.EMPTY))
                        return true;
                }
            }
        }
        return out;
    }

    /**
     * makes the unique list of occupied tiles by this car!
     *
     * @pre start coords are always to the left of the end positions
     * @pre this.facing is already initialized
     * @pre this.tiles is in [col, row] format
     *
     * @param sRow start row
     * @param sCol start column
     * @param eRow ending row
     * @param eCol ending column
     */
    private void makeTilesList(int sRow, int sCol, int eRow, int eCol){
        if(sRow+1 == eRow || sCol+1 == eCol || sRow == eRow+1 || sCol == eCol+1){   // if car is of length 2
            this.tiles = new int[2][2];
            this.tiles[0][0] = sCol;
            this.tiles[0][1] = sRow;

            this.tiles[1][0] = eCol;
            this.tiles[1][1] = eRow;

        }else{  // car is of length 3 (or more even though that's impossible dummy)
            int width = Math.abs(eCol-sCol)+1, height = Math.abs(eRow-sRow)+1;
            //this.tiles = new int[2][height];
            if(facing){ // horizontal
                this.tiles = new int[width][2];

                for(int i=0; i<width; i++){// row
                    this.tiles[i][0] = sCol+i;
                    this.tiles[i][1] = sRow;
                }
            }else{      // vertical
                this.tiles = new int[height][2];

                for(int i=0; i<height; i++){// row
                    this.tiles[i][0] = sCol;
                    this.tiles[i][1] = sRow+i;
                }
            }
        }
    }

    /**
     * gets an updated version of this but moved in the provided direction
     *
     * @pre the @dir param is a valid location
     *
     * @param dir direction car is moving.
     *            true ---> up/right
     *            false --> down/left
     * @return a new car representing this car, but moved
     */
    public Car getNewMovedCar(boolean dir){  // true is up/right
        if(facing){ // horizontal
            if(dir)         // right, col increases
                return new Car(name, tiles[0][1], (tiles[0][0])+1,
                            tiles[tiles.length-1][1], (tiles[tiles.length-1][0])+1, true);
            else {            // left, col decreases
                return new Car(name, tiles[0][1], (tiles[0][0]-1),
                        tiles[tiles.length-1][1], (tiles[tiles.length-1][0]-1), true);
            }
        }else{  // vertical
            if(dir)     // up, row decreases
                return new Car(name, ((tiles[0][1])-1), tiles[0][0],
                        (tiles[tiles.length-1][1]-1), tiles[tiles.length-1][0], false);
            else        // down, row increases
                return new Car(name, ((tiles[0][1])+1), tiles[0][0],
                        (tiles[tiles.length-1][1]+1), tiles[tiles.length-1][0], false);
        }
    }

    /**
     * gets the coordinates which this car occupies
     *
     * @return integer list of this cars coordinatess
     */
    public int[][] getTiles(){
        return this.tiles;
    }

    /**
     * gets the name of this car
     *
     * @return string which is the name of this EPIC CAR
     */
    public String getName(){
        return this.name;
    }

    /**
     * gets the direction which this car is facing
     *   > true --> horizontal
     *   > false --> vertical
     *
     * @return boolean of the direction being faced!!
     */
    public boolean getFacing(){
        return this.facing;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Car){
            Car car = ((Car)obj);
            if(car.getFacing() == this.facing && car.getName() == this.name && car.getTiles() == this.tiles)
                return true;
        }
        return false;
    }

    @Override
    public String toString(){
        String out = name + "\n";
        for(int i=0; i<tiles.length; i++)
            out+= "(" + tiles[i][0] + ", " + tiles[i][1] + ")\n";
        return out;
    }
}
