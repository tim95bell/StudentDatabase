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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ChoiceBox;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import javafx.scene.Scene;


public class SearchStudentScreen {
    
    private Asg3 owner;
    private Database db;
    
    private Scene scene;
    
    private GridPane searchScreenPane;
    private GridPane multipleResultsScreenPane;
    private BorderPane resultScreenPane;
    private TextField nameTf;
    private Button searchBtn;
    private Button cancelBtn;
    
    private ChoiceBox multipleResCb;
    private Button acceptMultipleChoiceBtn;
    private ObservableList<Student> multipleResultsList;
    
    private ObservableList<Student> result;
    
    public SearchStudentScreen(Asg3 owner){
        this.owner = owner;
        this.db = owner.getDb();
        this.nameTf = new TextField();
        nameTf.setPromptText("Name");
        this.searchScreenPane = new GridPane();
        this.multipleResultsScreenPane = new GridPane();
        this.resultScreenPane = new BorderPane();
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
        searchScreenPane.add(descFPane, 0, 0);
        searchScreenPane.add(searchFPane, 0, 1);
        
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
        
        // result screen
        FlowPane resultScreenHeader = new FlowPane();
        Button homeBtn = new Button("Home");
        homeBtn.setOnAction(e->cancelBtnPress());
        resultScreenHeader.getChildren().add(homeBtn);
        resultScreenPane.setTop(resultScreenHeader);
        result = FXCollections.observableArrayList(  );
        TableView<Student> resultTable  = new TableView();
        TableColumn<Student, Integer> idCol = new TableColumn<>("StudentID");
        idCol.setMinWidth(200);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        resultTable.setItems(result);
        resultTable.getColumns().addAll(idCol, nameCol);     
        resultScreenPane.setCenter(resultTable);
        
        this.scene = new Scene(searchScreenPane);
    }
    
    public void searchBtnPress(){
        String name = nameTf.getCharacters().toString();
        if(name == null || name.trim().equals("")){
            alert("Name cannot be empty");
            return;
        }
        
        ArrayList<Student> results = db.getStudentFromName(name);
        
        if(results.size() == 0){
            alert("No students with the name + " + name + ".");
            return;
        }
        else if(results.size() == 1){
            result.clear();
            result.add(results.get(0));
            this.scene.setRoot(resultScreenPane);
        }
        else{
            multipleResultsList.clear();
            multipleResultsList.addAll(results);
//            multipleResCb.setItems(multipleResultsList);
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
        owner.homeBtnPress();
    }
    
    public void acceptMultipleChoiceBtnPress(){
        Student student = (Student)multipleResCb.getValue();
        result.clear();
        result.add(student);
        this.scene.setRoot(resultScreenPane);
    }
    
    public Scene getScene(){
        return scene;
    }
    
}
