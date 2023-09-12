package puzzles.crossing;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;
import java.util.LinkedList;

public class CrossingConfig implements Configuration {

    private int[] startCount, endCount;
    private boolean boatPos;    // false = left; true = right
    private Collection<Configuration> neighbors = new LinkedList<>();

    /**
     * constructs shit
     *
     * @param startCount 1x2 list of [pups, wolves] on the left side
     * @param endCount 1x2 list of [pups, wolves] on the right side
     */
    public CrossingConfig(int[] startCount, int[] endCount, boolean boatPos){
        this.startCount = startCount;
        this.endCount = endCount;
        this.boatPos = boatPos;

        Solver.totalConfigs++;
    }

    @Override
    public boolean isSolution() {
        if(startCount[0] == 0 && startCount[1] == 0){
            return true;
        }
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        // boat on left going to right
        if(!boatPos){
            // move 2 pups to right
            if(startCount[0] >= 2){
                this.neighbors.add(new CrossingConfig(
                        new int[]{startCount[0]-2, startCount[1]}, new int[]{endCount[0]+2, endCount[1]}, true));
            }
            if(startCount[0] >= 1){
                this.neighbors.add(new CrossingConfig(
                        new int[]{startCount[0]-1, startCount[1]}, new int[]{endCount[0]+1, endCount[1]}, true));
            }
            if(startCount[1] > 0){
                this.neighbors.add(new CrossingConfig(
                        new int[]{startCount[0], startCount[1]-1}, new int[]{endCount[0], endCount[1]+1}, true));
            }


        }else if(boatPos){

            if(endCount[0] >= 2){
                this.neighbors.add(new CrossingConfig(
                        new int[]{startCount[0]+2, startCount[1]}, new int[]{endCount[0]-2, endCount[1]}, false));
            }
            if(endCount[0] >= 1){
                this.neighbors.add(new CrossingConfig(
                        new int[]{startCount[0]+1, startCount[1]}, new int[]{endCount[0]-1, endCount[1]}, false));
            }
            if(endCount[1] > 0){
                this.neighbors.add(new CrossingConfig(
                        new int[]{startCount[0], startCount[1]+1}, new int[]{endCount[0], endCount[1]-1}, false));
            }

        }


        return this.neighbors;
    }


    @Override
    public String toString(){
        if(!this.boatPos)
            return ( ("(BOAT) left=[" + startCount[0] + ", " + startCount[1]) +
                        ("], right=[" + endCount[0] + ", " + endCount[1] + "]") );
        else
            return ( ("       left=[" + startCount[0] + ", " + startCount[1]) +
                        ("], right=[" + endCount[0] + ", " + endCount[1] + "]  (BOAT)") );

    }
}
