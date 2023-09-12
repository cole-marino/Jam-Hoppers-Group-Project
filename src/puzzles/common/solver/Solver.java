package puzzles.common.solver;

import puzzles.strings.StringsConfig;

import java.util.*;

/**
 * performs breadth first search and solves the shit
 *
 * @author Cole Marino
 * @author cvm4043
 */
public class Solver {

    private String currentStr;
    private Configuration start, end;

    public List<Configuration> queue = new LinkedList<>();
    private HashMap<Configuration, Configuration> predecessors = new HashMap<>();
    public static int totalConfigs=-1;  // starts at -1 because I don't think the first one is counted maybe?

    /**
     * basically just a constructor which I don't really think I need
     *
     * @param start the starting configuration
     * @param end the ending configuration
     */
    public Solver(Configuration start, Configuration end){
        this.start = start;
        this.end = end;
    }

    /**
     * performs BFS to create all possible configs of the start word and finding the shortest path from the start to the end
     *
     * @param start the starting configuration
     * @param end the ending configuration trying to be found
     * @return a list of the path followed from the start to the end
     */
    public List<Configuration> solve(Configuration start, Configuration end){
        queue.add(start);
        predecessors.put(start, start);

        while(!queue.isEmpty()){
            Configuration current = queue.remove(0);

            if(current.isSolution()){
                this.end = current;
                break;
            }
            for(Configuration config: current.getNeighbors()){
                if(!(predecessors.containsKey(config))){
                    predecessors.put(config, current);
                    queue.add(config);
                    //System.out.println(config);
                }
            }
        }
        return this.getPath();
    }

    /**
     * creates the shortest path from the start point to the end point
     *
     * @return a list of the shortest path tracked
     */
    public List<Configuration> getPath(){
        List<Configuration> path = new LinkedList<>();
        Configuration curr;


        if(predecessors.containsKey(end)){
            curr = end;
            while(curr!=start){
                path.add(0, curr);
                curr = predecessors.get(curr);
            }
            path.add(0, start);
        }


        return path;
    }

    /**
     * returns all the the unique configs made
     *
     * @return integer of all the keys in the predecessors
     */
    public int getTotalUniqueConfigs(){
        return predecessors.keySet().size();
    }

    @Override
    public String toString(){
        return this.currentStr;
    }
}
