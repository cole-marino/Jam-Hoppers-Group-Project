package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.*;

public class StringsConfig implements Configuration {

    // starting word, the current word guess, and the correct word
    private String currentStr, end;
    private Collection<Configuration> neighbors = new LinkedList<>();

    /**
     * Basically acts as a node for the solver to create moew stringssssss
     *
     * @param start the starting string (basically what this node equals)
     * @param end the string trying to be found
     */
    public StringsConfig(String start, String end){
        this.currentStr = start;
        this.end = end;
        Solver.totalConfigs++;
    }


    @Override
    public boolean isSolution() {
        if(this.currentStr.equals(end))
            return true;
        else
            return false;
    }

    @Override
    public Collection<Configuration> getNeighbors(){
        String tempStr = currentStr;
        // adds string with value 1 above and 1 below
        for(int i=0; i<currentStr.length(); i++){
            char newChar1 = (char)(tempStr.charAt(i)+1);
            char newChar2 = (char)(tempStr.charAt(i)-1);
            switch((int)newChar1){
                case 91: newChar1 = (char)(65);  break;
                case 64: newChar1 = (char)(90);  break;
            }
            switch((int)newChar2){
                case 91: newChar2 = (char)(65);  break;
                case 64: newChar2 = (char)(90);  break;
            }

            String newStr1 = (currentStr.substring(0, i) + newChar1 + currentStr.substring(i+1));
            String newStr2 = (currentStr.substring(0, i) + newChar2 + currentStr.substring(i+1));
            //System.out.println(newStr1);
            StringsConfig s1 = new StringsConfig(newStr1, end);
            StringsConfig s2 = new StringsConfig(newStr2, end);
            neighbors.add(s1);
            neighbors.add(s2);
        }
        return neighbors;
    }

    @Override
    public String toString(){
        return this.currentStr;
    }

    @Override
    public int hashCode(){
        return currentStr.hashCode();
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof StringsConfig){
            StringsConfig sc = (StringsConfig)(other);
            if(sc.currentStr.equals(this.currentStr)){
                return true;
            }
        }else{
            if(this == other){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }


}
