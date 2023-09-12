package puzzles.hoppers.ptui;

import puzzles.common.ConsoleApplication;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;
import puzzles.hoppers.solver.Hoppers;

import java.io.PrintWriter;
import java.util.List;

/**
 * This is the PTUI
 * @author Jay Pruitt
 */
public class HoppersPTUI extends ConsoleApplication implements Observer<HoppersModel, String> {
    private HoppersModel model;
    private boolean initialized;

    /**
     * This creates the hoppers model, adds an observer for it, then loads the first file
     */
    @Override public void init() throws Exception {
        this.initialized = false;
        this.model = new HoppersModel();
        this.model.addObserver( this );
        List<String> paramStrings = super.getArguments();
        if(paramStrings.size() == 1){
            model.load(paramStrings.get(0));
        }
        else{}
        }

    /**
     * This updates the model then prints out the message from the observers
     * @param model the observed subject of this observer
     * @param msg the message the model wants conveyed to the user
     */
    @Override
    public void update(HoppersModel model, String msg) {
        System.out.println(msg);
        if(!initialized){ return; }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        }else{
            ConsoleApplication.launch( HoppersPTUI.class, args );
        }
    }

    /**
     * The sets the commands for the PTUI
     * @param console Where the UI should print output. It is recommended to save
     *                this object in a field in the subclass.
     * @throws Exception
     */
    @Override
    public void start(PrintWriter console) throws Exception {
        this.initialized = true;
        super.setOnCommand( "h", 0, "h(int)              -- hint next move",
                args -> this.model.getHint() );
        super.setOnCommand( "load", 1, "load filename     -- load new puzzle file",
                args -> this.model.load(args[0]) );
        super.setOnCommand( "s", 2, "s(elect) r c        -- select cell at r, c",
                args -> this.model.select(Integer.parseInt(args[0]),Integer.parseInt(args[1]) ) );
        super.setOnCommand( "reset", 0, "reset             -- reset the current game",
                args -> this.model.reset() );
        super.setOnCommand( "help", 0, "help     -- help",
                args -> this.model.help() );
    }
}
