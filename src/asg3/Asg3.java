package asg3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Asg3 extends Application {
   
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    
    private Stage stage;
    // the scene for the main menu
    private Scene menuScene;
    // the other screens, these will provide a scene
    private EditMarkScreen editMarkScreen;
    private SimpleDisplayScreen simpleDisplayScreen;
    private EditStudentScreen editStudentScreen;
    private SearchStudentScreen searchStudentScreen;
    
    private Database db;
    
    @Override
    /**
     * Init everything, and show menu scene
     * @param stage the provided stage
     */
    public void start(Stage stage) {
        db = new Database();
        db.initDatabase();
        
        this.stage = stage;
        stage.setTitle("Student Marks");
        stage.setWidth(WIDTH);
	stage.setHeight(HEIGHT);
        
        this.menuScene = createMenuScene();
        
        this.simpleDisplayScreen = new SimpleDisplayScreen(this);
        this.editStudentScreen = new EditStudentScreen(this);
        this.editMarkScreen = new EditMarkScreen(this);
        this.searchStudentScreen = new SearchStudentScreen(this);
        
	stage.setScene(menuScene);
	stage.show();
    }
    
    //--------------------- INIT SCREENS ---------------------//
    /**
     * Creates the menuScene
     * 
     */
    public Scene createMenuScene(){
        int numBtns = 5;
        Button simpleDisplayBtn = new Button("Simple Display");
        simpleDisplayBtn.setOnAction(e->simpleDisplayBtnPress());
        
        Button scoreDisplayBtn = new Button("Score Display");
        scoreDisplayBtn.setOnAction(e->scoreDisplayBtnPress());
        
        Button searchStudentBtn = new Button("Search Student");
        searchStudentBtn.setOnAction(e->searchStudentBtnPress());
        
        Button editStudentsBtn = new Button("Edit Students");
        editStudentsBtn.setOnAction(e->editStudentsBtnPress());
        
        Button insertMarkBtn = new Button("Edit Marks");
        insertMarkBtn.setOnAction(e->insertMarkBtnPress());

        // menu scene
        BorderPane bPane = new BorderPane();
        double btnHeight = insertMarkBtn.getHeight();
        double vGap = (stage.getHeight() - (btnHeight*numBtns)) / (numBtns*2);
        VBox vBox = new VBox(vGap);
        vBox.setAlignment(javafx.geometry.Pos.CENTER);
        vBox.getChildren().addAll( simpleDisplayBtn, scoreDisplayBtn, searchStudentBtn, editStudentsBtn, insertMarkBtn );
        bPane.setCenter(vBox);
        bPane.setAlignment(vBox, javafx.geometry.Pos.CENTER);
        
        return new Scene(bPane);
    }
   
    /**
     * Displays the student provided, in a simple view
     *  @param student the student to display
     */
    public void displayStudent(Student student){
        stage.setScene( simpleDisplayScreen.getScene(true, student) );
    }
    
    //--------------------- BUTTON PRESS ACTIONS ---------------------//
    
    public void homeBtnPress(){
        stage.setScene(menuScene);
    }
    
    public void simpleDisplayBtnPress(){
        stage.setScene( simpleDisplayScreen.getScene(true, null) );
    }
    
    public void scoreDisplayBtnPress(){
        stage.setScene( simpleDisplayScreen.getScene(false, null) );
    }
    
    public void searchStudentBtnPress(){
        stage.setScene(searchStudentScreen.getScene());
    }
    
    public void editStudentsBtnPress(){
        stage.setScene(editStudentScreen.getScene());
    }
    
    public void insertMarkBtnPress(){
        stage.setScene(editMarkScreen.getScene());
    }
        
    //------------------------------------------------------------------------
    public static void main(String[] args) {
        launch(args);
    }
    
    public Database getDb(){
        return this.db;
    }
    
}
