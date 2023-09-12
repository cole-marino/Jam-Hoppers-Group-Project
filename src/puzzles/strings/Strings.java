package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Strings {


    /**
     * If args is given then a new strings is started but if not then game is EXITED
     *
     * @param args given inputs (optional)
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
            System.exit(-1);
        }else{
            // solves the stuff
            Configuration start = new StringsConfig(args[0], args[1]);
            Configuration end = new StringsConfig(args[1], args[1]);
            Solver solver = new Solver(start, end);
            List<Configuration> path = solver.solve(start, end);
            // prints all the info
            System.out.println("Start: " + args[0] + ", Finish: " + args[1]);
            System.out.println("Total configs: " + Solver.totalConfigs);
            System.out.println("Unique configs: " + (solver.getTotalUniqueConfigs()-1));

            // prints the path
            if(path != null && (path.size() > 1 && args[0] != args[1]))
                for(int i=0; i<path.size(); i++){
                    if(!(path.get(i) == null))
                        System.out.println("Step " + (i) + ": " + path.get(i).toString());
                }

            else
                System.out.println("No solution");
        }
    }

}
