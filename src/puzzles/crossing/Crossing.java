package puzzles.crossing;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

public class Crossing {

    /**
     * runs the crossing game woooop
     *
     * @param args
     */
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println(("Usage: java Crossing pups wolves"));
            System.exit(-1);
        }else{
            int pups = Integer.valueOf(args[0]), wolves = Integer.valueOf(args[1]);
            int[] leftCount = new int[2];
            int[] rightCount = new int[2];
            leftCount[0] = pups;
            leftCount[1] = wolves;

            Configuration start = new CrossingConfig(leftCount, rightCount, false);
            Configuration end = new CrossingConfig(rightCount, leftCount, true);
            Solver solver = new Solver(start, end);
            List<Configuration> path = solver.solve(start, end);

            // prints all the info
            System.out.println("Start: " + args[0] + ", Finish: " + args[1]);
            System.out.println("Total configs: " + Solver.totalConfigs);
            System.out.println("Unique configs: " + solver.getTotalUniqueConfigs());

            // prints the path
            if(path != null){
                for(int i=0; i<path.size(); i++)
                    if(!(path.get(i) == null))
                        System.out.println("Step " + (i) + ": " + path.get(i).toString());
            }
            else
                System.out.println("No solution");
        }
    }
}
