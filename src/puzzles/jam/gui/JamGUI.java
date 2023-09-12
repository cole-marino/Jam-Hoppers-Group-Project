package puzzles.jam.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.jam.model.JamModel;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

/**
 * the GUI runner for the Jam game
 *
 * @author Cole Marino
 * @author cvm4043
 */
public class JamGUI extends Application  implements Observer<JamModel, String>  {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private final static String X_CAR_COLOR = "#DF0101";
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;

    private String[][] board;
    private Button[][] gameBoard;

    private GridPane gp;

    private Stage stage;
    private Scene scene;
    private JamModel model;
    private boolean initialized;
    private PrintWriter out;
    protected String filename;

    private Map<String, String> colors = new HashMap<>();


    /**
     * initializes stuff I guess
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);

        this.model = new JamModel();
        model.load(filename);
        this.board = model.getBoard();
        this.gameBoard = new Button[model.getCols()][model.getRows()];
        model.addObserver(this);
    }

    /**
     * makes the grid of buttons!!!
     *
     * @return a gridpane full of the game board buttons
     */
    private GridPane makeGrid() {
        GridPane result = new GridPane();
        this.gameBoard = new Button[model.getRows()][model.getCols()];
        for(int r=0;r<model.getRows();r++) {
            for(int c=0;c<model.getCols();c++) {
                this.gameBoard[r][c] = new Button();
                if(board[c][r] != ".")
                    this.gameBoard[r][c].setText(board[c][r]);
                else
                    this.gameBoard[r][c].setText(" ");

                this.gameBoard[r][c].setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                        "-fx-background-color: " + colors.get(board[c][r]) + ";" +
                        "-fx-font-weight: bold;");
                int finalR = r;
                int finalC = c;
                this.gameBoard[r][c].setOnAction(e -> model.select(finalR,finalC));
                this.gameBoard[r][c].setMinSize(ICON_SIZE, ICON_SIZE);
                this.gameBoard[r][c].setMaxSize(ICON_SIZE, ICON_SIZE);
                result.add( this.gameBoard[r][c], c, r );
            }
        }
        result.setAlignment(Pos.CENTER);
        return result;
    }

    /**
     * creates the filechooser
     *  >> **given in the discussion post by a professor**
     *
     * @param fileChooser
     */
    private void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select File");
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "hoppers";
        fileChooser.setInitialDirectory(new File(currentPath));
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Jam GUI");
        for(int r=0; r < model.getRows(); r++)
            for(int c=0; c< model.getCols(); c++)
                createCarColors(board[c][r]);
        this.stage = stage;
        this.stage = renderScreen(this.stage, "Loaded");
    }

    /**
     * basically renders the WHOLE screen (quite epic!)
     *
     * @param stage the stage of the stuff
     * @param msg the message which the top is being changed to
     * @return the game stage
     */
    private Stage renderScreen(Stage stage, String msg){
        for(int r=0; r < model.getRows(); r++)
            for(int c=0; c< model.getCols(); c++)
                createCarColors(board[c][r]);

        BorderPane bp = new BorderPane();
        HBox hb = new HBox();

        bp.setTop(new Text(msg));
        this.gp = this.makeGrid();
        bp.setCenter(gp);

        Button load = new Button("Load");
        load.setOnAction( e -> {
            FileChooser fileChooser = new FileChooser();
            configureFileChooser(fileChooser);
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                model.load(file.getPath());
                BorderPane bb = new BorderPane();
                bb.setCenter(makeGrid());
                Scene newScene = new Scene(bb);
                stage.setScene(newScene);
                stage.show();
                model.reset();
                stage.sizeToScene();
            }
        });
        hb.getChildren().add(load);

        Button reset = new Button("Reset");
        reset.setOnAction( e -> {model.reset();this.board = model.getBoard();});
        hb.getChildren().add(reset);

        Button hint = new Button("Hint");

        hb.getChildren().add(hint);
        hint.setOnAction( e -> {model.hint();this.board = model.getBoard();});
        bp.setBottom(hb);

        Scene scene = new Scene(bp);
        this.stage.setScene(scene);
        this.stage.show();
        this.stage = stage;

        return stage;
    }

    @Override
    public void update(JamModel jamModel, String msg) {
        this.board = jamModel.getBoard();
        this.renderScreen(stage, msg);
        //this.gp = this.makeGrid();
    }

    /**
     * basically creates unique colors for all of the cars!
     *
     * @param name the name of the car which is being colored
     */
    private void createCarColors(String name){
        if(name.equals("X")){
            colors.put(name, X_CAR_COLOR);
        }else if(name.equals(".")){
            colors.put(name, "#D3D3D3");
        }else if(!colors.containsKey(name)){
            Random obj = new Random();
            int rand_num = obj.nextInt(0xffffff + 1);
            String code = String.format("#%06x", rand_num);
            colors.put(name, code);
        }
    }

    /**
     * it's the main!
     *
     * @param args argggg
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
