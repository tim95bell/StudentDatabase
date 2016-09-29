package asg3;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ChoiceBox;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import javafx.scene.Scene;

/**
 * A Screen to search for a student. Contains two scenes, one for the search,
 * and another to choose an option if there are multiple results. 
 * Then it changes screens to the SimpleDisplayScreen, specifying to only display the specified student.
 * @author timbell
 */
public class SearchStudentScreen {
    
    private Asg3 owner;
    private Database db;
    
    private Scene scene;
    
    private BorderPane searchScreenPane;
    private GridPane multipleResultsScreenPane;
    private TextField nameTf;
    private Button searchBtn;
    private Button cancelBtn;
    
    private ChoiceBox multipleResCb;
    private Button acceptMultipleChoiceBtn;
    private ObservableList<Student> multipleResultsList;
    
    public SearchStudentScreen(Asg3 owner){
        this.owner = owner;
        this.db = owner.getDb();
        
        // search screen
        this.nameTf = new TextField();
        nameTf.setPromptText("Name");
        this.searchScreenPane = new BorderPane();
        
        // multiple results screen
        this.multipleResultsScreenPane = new GridPane();
        this.searchBtn = new Button("Search");
        searchBtn.setOnAction(e->searchBtnPress());
        this.cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e->cancelBtnPress());
        this.acceptMultipleChoiceBtn = new Button("Select");
        acceptMultipleChoiceBtn.setOnAction(e->acceptMultipleChoiceBtnPress());
        multipleResCb = new ChoiceBox();
        
        init();
    }
    
    public void init(){
        // search screen
        FlowPane descFPane = new FlowPane();
        Label descLabel = new Label("Search Student By Name");
        descLabel.setFont(new Font(50));
        descFPane.getChildren().add(descLabel);
        descFPane.setAlignment(javafx.geometry.Pos.CENTER);
        FlowPane searchFPane = new FlowPane();
        searchFPane.getChildren().addAll( new Label("Name: "), nameTf, searchBtn, cancelBtn );
        
        Button searchHomeBtn = new Button("Home");
        searchHomeBtn.setOnAction(e->cancelBtnPress());
        GridPane searchGrid = new GridPane();
        searchGrid.add(descFPane, 0, 0);
        searchGrid.add(searchFPane, 0, 1);
        searchScreenPane.setCenter(searchGrid);        
        searchScreenPane.setTop(searchHomeBtn);
        
        // multiple results screen
        FlowPane multipleResultsTitle = new FlowPane();
        Label multResTitleLabel = new Label("Multiple results found. Select one: ");
        multipleResultsTitle.getChildren().add(multResTitleLabel);
        multipleResultsScreenPane.add(multipleResultsTitle, 0, 0);
        HBox choice = new HBox();
        choice.getChildren().addAll( new Label("Student Id: "), multipleResCb, acceptMultipleChoiceBtn, cancelBtn );
        multipleResultsScreenPane.add(choice, 0, 1);
        multipleResultsList = FXCollections.observableArrayList(  );
        multipleResCb.setItems(multipleResultsList); 
        
        this.scene = new Scene(searchScreenPane);
    }
    
    /**
     * Performs the search. Changing the scene to the multiple options scene if multiple options are found.
     * Or changing to the SimpleDisplayScreen and displaying the student found, if one single student is found.
     */
    public void searchBtnPress(){
        String name = nameTf.getCharacters().toString();
        nameTf.clear();
        if(name == null || name.trim().equals("")){
            alert("Name cannot be empty");
            return;
        }
        
        ArrayList<Student> results = db.getStudentFromName(name);
        
        if(results.size() == 0){
            alert("No students with the name " + name + ".");
            return;
        }
        else if(results.size() == 1){
            this.scene.setRoot(searchScreenPane);
            owner.displayStudent(results.get(0));
        }
        else{
            multipleResultsList.clear();
            multipleResultsList.addAll(results);
            this.scene.setRoot(multipleResultsScreenPane);
        }
    }
    
    public void alert(String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
    
    public void cancelBtnPress(){
        this.scene.setRoot(searchScreenPane);
        nameTf.clear();
        owner.homeBtnPress();
    }
    
    /**
     * accepts the choice from the multipleResCb in the multiple options scene.
     */
    public void acceptMultipleChoiceBtnPress(){
        if(multipleResCb.getValue() == null){
            alert("Must select an id");
            return;
        }
        
        this.scene.setRoot(searchScreenPane);
        Student student = (Student)multipleResCb.getValue();
        owner.displayStudent(student);
    }
    
    public Scene getScene(){
        return scene;
    }
    
}
