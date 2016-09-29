/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asg3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;

/**
 *
 * @author timbell
 */
public class EditStudentScreen {
    
    private BorderPane bPane;
    private TableView table;
    private ScrollPane sPane;
    private Scene scene;
    private ObservableList<EditStudent> oList;
    private Asg3 owner;
    private Database db;
    
    // edit
    private FlowPane editPane;
    private Label editIdLabel;
    private TextField editNameTf;
    private Button acceptChangeBtn;
    private Button cancelChangeBtn;
    
    private TextField addStudentTf;
    
    public EditStudentScreen(Asg3 owner){
        this.owner = owner;
        this.db = owner.getDb();
        this.oList = FXCollections.observableArrayList(  );
        this.bPane = new BorderPane();
        this.table = new TableView();
        this.sPane = new ScrollPane();
        
        this.editPane = new FlowPane();
        this.editIdLabel = new Label();
        this.editNameTf = new TextField();
        this.acceptChangeBtn = new Button("OK");
        acceptChangeBtn.setOnAction(e->acceptChangeBtn());
        this.cancelChangeBtn = new Button("Cancel");
        cancelChangeBtn.setOnAction(e->cancelChangeBtn());
        HBox id = new HBox();
        id.getChildren().addAll( new Label("Student Id: "), editIdLabel );
        HBox name = new HBox();
        name.getChildren().addAll( new Label("Name: "), editNameTf );
        editPane.getChildren().addAll( id, name, acceptChangeBtn, cancelChangeBtn);
        editPane.setAlignment(javafx.geometry.Pos.CENTER);
        editPane.setHgap(20);
        
        init();
    }
    
    public void init(){        
        // create columns
        double colWidth = Asg3.WIDTH/3;
        // name col
        TableColumn<EditStudent, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(colWidth);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        // id col
        TableColumn<EditStudent, Integer> idCol = new TableColumn<>("StudentID");
        idCol.setMinWidth(colWidth);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        // edit col
        TableColumn<EditStudent, Button> editCol = new TableColumn<>("Edit");
        editCol.setMinWidth(colWidth/2);
        editCol.setCellValueFactory(new PropertyValueFactory<>("editBtn"));
        // delete col
        TableColumn<EditStudent, Button> deleteCol = new TableColumn<>("Delete");
        deleteCol.setMinWidth(colWidth/2);
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
        
        // add data to table
        table.setItems(oList);
        table.getColumns().addAll(nameCol, idCol, editCol, deleteCol);     
        for(int i = 0; i < table.getColumns().size(); ++i){
            ((TableColumn)table.getColumns().get(i)).setStyle("-fx-alignment: CENTER;");
        }
        sPane.setPrefSize(Asg3.HEIGHT, Asg3.WIDTH);
        sPane.setContent(table);
        
        FlowPane topFPane = new FlowPane();
        Button homeBtn = new Button("Home");
        homeBtn.setOnAction( e->owner.homeBtnPress() );
        topFPane.getChildren().add(homeBtn);
        
        FlowPane bottomFPane = new FlowPane();
        Label addLabel = new Label("Add a Student: ");
        this.addStudentTf = new TextField();
        this.addStudentTf.setPromptText("Name");
        this.addStudentTf.setOnAction( e->addStudent() );
        Button addBtn = new Button("Add");
        addBtn.setOnAction( e->addStudent() );
        bottomFPane.getChildren().addAll(addLabel, this.addStudentTf, addBtn);
        bottomFPane.setAlignment(javafx.geometry.Pos.CENTER);
        bottomFPane.setPadding(new javafx.geometry.Insets(20));
        
        bPane.setCenter(sPane);
        bPane.setTop(topFPane);
        bPane.setBottom(bottomFPane);
        
        // EDIT STUD
        
        
        this.scene = new Scene(bPane);
    }
    
    public void calcData(){
        // get data
        oList.clear();
        Student[] students = db.getAllStudents();
        for(Student s : students){
            EditStudent editStudent = new EditStudent( this, s );
            oList.add(editStudent);
        }
    }
    
    public Scene getScene(){
        calcData();
        return scene;
    }
    
    public void deleteStudent(EditStudent editStudent){
        db.deleteStudent(editStudent.getId());
        oList.remove(editStudent);
    }
    
    public void editStudent(EditStudent editStudent){
       editIdLabel.setText(Integer.toString(editStudent.getId()));
       editNameTf.setText(editStudent.getName());
       scene.setRoot(editPane);
    }
    
    public void addStudent(){
        String name = this.addStudentTf.getCharacters().toString();
        this.addStudentTf.clear();
        if(name == null || name.length() == 0){
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Em");
//            alert.setHeaderText(null);
//            alert.setContentText("The file \"" + fileName + "\" could not be found.");
//            alert.showAndWait();
            return;
        }
     
        Student student = db.insertStudent(name);
        oList.add(new EditStudent(this, student));
    }
    
    public void acceptChangeBtn(){
        String name = editNameTf.getCharacters().toString();
        if(name == null || name.length() == 0 || name.trim().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Cannot set empty name");
            alert.showAndWait();
        }
        else{
            db.changeName(Integer.parseInt(editIdLabel.getText()), name);
            calcData();
            scene.setRoot(bPane);   
        }
    }
    
    public void cancelChangeBtn(){
        scene.setRoot(bPane);
    }

    
}
