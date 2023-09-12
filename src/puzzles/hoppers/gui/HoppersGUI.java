package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Paths;

/**
 * This is the GUI version of hoppers
 *
 * @author Jay Pruitt
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private HoppersModel model; // the hoppers model
    private Button[][] buttons; // the double array of buttons representing the board
    private Label top = new Label(); // the message at the top
    private boolean ready; // determines if the GUI is ready or not yet
    private String mss; // the message relayed by the observers
    private static final int ICON_SIZE=75; // the size of icons
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png")); // red frog
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png")); // green frog
    private Image lily = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png")); // lily pad
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png")); // water

    /**
     * This initializes the gui by making a new hoppers model and loading the file.
     * Then it makes new buttons and adds an observer
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.model=new HoppersModel();
        model.load(filename);
        this.buttons = new Button[model.getRows()][model.getCols()];
        model.addObserver(this);
    }

    /**
     * This creates all the buttons and text on the window then displays them. This also handles the actions of
     * most of the buttons.
     * @param stage the stage to display on
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane=new BorderPane();
        borderPane.setTop(top);
        BorderPane.setAlignment(top, Pos.CENTER);;
        GridPane gridPane = new GridPane();
        gridPane = makeGrid();
        borderPane.setCenter(gridPane);
        HBox bottom2 = new HBox();
        Button load = new Button("Load");
        load.setOnAction( e -> {
                    FileChooser fileChooser = new FileChooser();
                    configureFileChooser(fileChooser);
                    fileChooser.setTitle("Open Resource File");
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        model.load(file.getPath());
                        borderPane.setCenter(makeGrid());
                        stage.sizeToScene();
                    }
                }
        );
        Button reset = new Button("Reset");
        reset.setOnAction(e -> this.reset());
        Button hint = new Button("Hint");
        hint.setOnAction(event -> model.getHint());
        bottom2.getChildren().add(load);
        bottom2.getChildren().add(reset);
        bottom2.getChildren().add(hint);
        BorderPane.setAlignment(bottom2, Pos.CENTER);
        borderPane.setBottom(bottom2);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setTitle("Hoppers GUI");
        stage.show();
        this.ready=true;
    }

    /**
     * This sets the default directory to the hoppers folder.
     * @param fileChooser the filechooser object
     */
    private void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select File");
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "hoppers";
        fileChooser.setInitialDirectory(new File(currentPath));
    }

    /**
     * This updates the GUI with the new model, and remakes the board.
     * @param hoppersModel the updated model
     * @param msg the message recieved from the observers
     */
    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        if ( !this.ready ) return;
        for(int r=0;r<model.getRows();r++) {
            for(int c=0;c<model.getCols();c++) {
                setButtons(r,c, model.getVal(r,c));
            }
        }
        this.mss=msg;
        top.setText(mss);
    }

    /**
     * This is the main method that starts the application
     * @param args the arguments for the program
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);

        }
    }
    /***
     *  this makes the grid which populates the pond with frogs, water, and lilys
     * @return the grid of empty letters
     */
    private GridPane makeGrid() {
        GridPane result = new GridPane();
        this.buttons = new Button[model.getRows()][model.getCols()];
        for(int r=0;r<model.getRows();r++) {
            for(int c=0;c<model.getCols();c++) {
                this.buttons[r][c] = new Button();
                if(model.getVal(r,c).equals("R")) this.buttons[r][c].setGraphic(new ImageView(redFrog));
                if(model.getVal(r,c).equals("G")) this.buttons[r][c].setGraphic(new ImageView(greenFrog));
                if(model.getVal(r,c).equals(".")) this.buttons[r][c].setGraphic(new ImageView(lily));
                if(model.getVal(r,c).equals("*")) this.buttons[r][c].setGraphic(new ImageView(water));
                this.buttons[r][c].setMinSize(ICON_SIZE, ICON_SIZE);
                this.buttons[r][c].setMaxSize(ICON_SIZE, ICON_SIZE);
                int finalR = r;
                int finalC = c;
                this.buttons[r][c].setOnAction(e -> model.select(finalR,finalC));
                result.add( this.buttons[r][c], c, r );
            }
        }
        result.setAlignment(Pos.CENTER);
        return result;
    }

    /**
     * This resets the board to the loaded model
     */
    private void reset(){
        model.reset();
        for ( int r = 0; r < model.getRows(); ++r ) {
            for ( int c = 0; c < model.getCols(); ++c ) {
                setButtons(r,c, model.getVal(r,c));
            }
        }
        top.setText("Reset!");
    }

    /**
     *
     * @param row the row to change
     * @param col the column to change
     * @param val the value to change to, R,G,*,.
     */
    private void setButtons(int row,int col,String val) {
        if(val.equals("R")) this.buttons[row][col].setGraphic(new ImageView(redFrog));
        if(val.equals("G")) this.buttons[row][col].setGraphic(new ImageView(greenFrog));
        if(val.equals(".")) this.buttons[row][col].setGraphic(new ImageView(lily));
        if(val.equals("*")) this.buttons[row][col].setGraphic(new ImageView(water));
    }

}
