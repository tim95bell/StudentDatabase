package asg3;

import static asg3.Asg3.WIDTH;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class SimpleDisplayScreen {
    
    private Asg3 owner;
    private Database db;
    private BorderPane bPane;
    private TableView simpleTable;
    private TableView scoreTable;
    private Scene scene;
    private ObservableList<ViewRow> oList;
    private ScrollPane sPane;
    
    public SimpleDisplayScreen(Asg3 owner){
        this.owner = owner;
        this.db = owner.getDb();
        this.bPane = new BorderPane();
        this.scoreTable = new TableView();
        this.simpleTable = new TableView();
        this.oList = FXCollections.observableArrayList();
        this.sPane = new ScrollPane();
        init();
    }
    
    public void init(){
        double colWidth = WIDTH/7;
        // studentid col
        TableColumn<ViewRow, Integer> idCol = new TableColumn<>("StudentID");
        idCol.setMinWidth(colWidth);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        // name col
        TableColumn<ViewRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(colWidth);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        // asg1 col
        TableColumn<ViewRow, String> asg1Col = new TableColumn<>("Asg 1");
        asg1Col.setMinWidth(colWidth);
        asg1Col.setCellValueFactory(new PropertyValueFactory<>("asg1"));
        // asg2 col
        TableColumn<ViewRow, String> asg2Col = new TableColumn<>("Asg 2");
        asg2Col.setMinWidth(colWidth);
        asg2Col.setCellValueFactory(new PropertyValueFactory<>("asg2"));
        // asg3 col
        TableColumn<ViewRow, String> asg3Col = new TableColumn<>("Asg 3");
        asg3Col.setMinWidth(colWidth);
        asg3Col.setCellValueFactory(new PropertyValueFactory<>("asg3"));
        // exam col
        TableColumn<ViewRow, String> examCol = new TableColumn<>("Exam");
        examCol.setMinWidth(colWidth);
        examCol.setCellValueFactory(new PropertyValueFactory<>("exam"));
        // total col
        TableColumn<ViewRow, String> totalCol = new TableColumn<>("Total");
        totalCol.setMinWidth(colWidth);
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        simpleTable.setItems(oList);
        simpleTable.getColumns().addAll(idCol, nameCol, asg1Col, asg2Col, asg3Col, examCol);     
        scoreTable.setItems(oList);
        scoreTable.getColumns().addAll(idCol, nameCol, asg1Col, asg2Col, asg3Col, examCol, totalCol);     
        for(int i = 0; i < simpleTable.getColumns().size(); ++i){
            ((TableColumn)simpleTable.getColumns().get(i)).setStyle("-fx-alignment: CENTER;");
        }
        for(int i = 0; i < scoreTable.getColumns().size(); ++i){
            ((TableColumn)scoreTable.getColumns().get(i)).setStyle("-fx-alignment: CENTER;");
        }
        sPane.setPrefSize(Asg3.HEIGHT, Asg3.WIDTH);
        sPane.setContent(simpleTable);
         
        FlowPane fPane = new FlowPane();
        Button homeBtn = new Button("Home");
        homeBtn.setOnAction( e->owner.homeBtnPress() );
        fPane.getChildren().add(homeBtn);
        
        bPane.setCenter(sPane);
        bPane.setTop(fPane);
        
        this.scene = new Scene(bPane);
    }
    
    public void calcData(boolean simple){
        if(simple)
            sPane.setContent(simpleTable);
        else
            sPane.setContent(scoreTable);
        
        oList.clear();
        
        Student[] students = db.getAllStudents();
        ArrayList<Mark[]> marksList = new ArrayList<>();
        for(Student s : students){
            Mark[] thisStudentsMarks = db.getMarksFromId(s.getId());
            marksList.add(thisStudentsMarks);
        }
        
        for(int studentIndex = 0; studentIndex < students.length; ++studentIndex){
            Student student = students[studentIndex];
            for(int marksIndex = 0; marksIndex < marksList.get(studentIndex).length; ++marksIndex){
                Mark mark = marksList.get(studentIndex)[marksIndex];
                ViewRow row = new ViewRow( student.getName(), student.getId(), mark.getAllMarksAsStrings() );
                oList.add(row);
            }
            
        }
    }
    
    public Scene getScene(boolean simple){
        calcData(simple);
        return scene;
    }
}
