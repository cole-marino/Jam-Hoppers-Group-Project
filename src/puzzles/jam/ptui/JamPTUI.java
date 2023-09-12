package puzzles.jam.ptui;

import puzzles.common.ConsoleApplication;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.Car;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;
import puzzles.jam.solver.Jam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the PTUI runner for the Jam game
 *
 * @author Cole Marino
 * @author cvm4043
 */
public class JamPTUI extends ConsoleApplication implements Observer<JamModel, String> {

    private JamModel model;
    private boolean initialized;
    private PrintWriter out;
    protected String filename;


    @Override
    public void update(JamModel jamModel, String msg) {
        System.out.println(msg);

    }

    @Override
    public void init() throws Exception {
        this.initialized = true;
        this.model = new JamModel();
        this.model.addObserver( this );
        List<String> paramStrings = super.getArguments();

        if(paramStrings.size() == 1){
            model.load(paramStrings.get(0));


        }
    }

    @Override
    public void start(PrintWriter out) throws Exception {
        this.out = out;
        this.initialized = true;
        super.setOnCommand( "h", 0, "(int)              -- hint next move",
                args -> model.hint() );
        super.setOnCommand( "l", 1, "(oad) filename     -- load new puzzle file",
                args -> model.load(args[0]));
        super.setOnCommand("s", 2, "(elect) r c        -- select cell at r, c",
                args -> model.select(Integer.valueOf(args[0]), Integer.valueOf(args[1])));
        super.setOnCommand("q", 0, "(uit)              -- quit the game",
                args -> this.quit());
        super.setOnCommand("r", 0, "(eset)             -- reset the current game",
                args -> model.reset());
        this.help();

    }

    private void help(){
        System.out.println("    h(int)              -- hint next move\n" +
                "    l(oad) filename     -- load new puzzle file\n" +
                "    s(elect) r c        -- select cell at r, c\n" +
                "    q(uit)              -- quit the game\n" +
                "    r(eset)             -- reset the current game");
    }

    /**
     * quits the game with very sad!! message (rip bozo)
     */
    private void quit(){
        System.out.println("Closing application...");
        System.exit(-1);
    }

    /**
     * basically refers to the console application file to run everything else
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        }else{
            ConsoleApplication.launch(JamPTUI.class, args);
        }
    }


}
