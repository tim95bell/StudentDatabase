package asg3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import javafx.geometry.Pos;

public class Asg3 extends Application {
   
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    
    private Stage stage;
    private Scene menuScene;
    
    private EditMarkScreen editMarkScreen;
    private SimpleDisplayScreen simpleDisplayScreen;
    private EditStudentScreen editStudentScreen;
    private SearchStudentScreen searchStudentScreen;
    
    private Database db;
   
    
    @Override
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
    
    public Scene createScoreDisplayScene(){
        return new Scene(new BorderPane());
    }
   
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
