package asg3;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class EditMarkScreen {
    
    private BorderPane bPane;
    private TableView table;
    private ScrollPane sPane;
    private Scene scene;
    private ObservableList<EditMark> oList;
    private Asg3 owner;
    private Database db;
    
    // edit
    private FlowPane editPane;
    private Label editIdLabel;
    private Label editSubjectLabel;
    private Label editSessionLabel;
    private TextField editAsg1Tf;
    private TextField editAsg2Tf;
    private TextField editAsg3Tf;
    private TextField editExamTf;
    private Button acceptChangeBtn;
    private Button cancelChangeBtn;
    
    // TODO: a id drop selection
    private TextField subjectTf, sessionTf, asg1Tf, asg2Tf, asg3Tf, examTf;
    private ChoiceBox<Integer> idCb;
    
    public EditMarkScreen(Asg3 owner){
        this.owner = owner;
        this.db = owner.getDb();
        this.oList = FXCollections.observableArrayList(  );
        this.bPane = new BorderPane();
        this.table = new TableView();
        this.sPane = new ScrollPane();
        
        this.subjectTf = new TextField();
        this.sessionTf = new TextField();
        this.asg1Tf = new TextField();
        this.asg2Tf = new TextField();
        this.asg3Tf = new TextField();
        this.examTf = new TextField();
        
        this.editPane = new FlowPane();
        this.editIdLabel = new Label();
        this.editSubjectLabel = new Label();
        this.editSessionLabel = new Label();
        this.editAsg1Tf = new TextField();
        this.editAsg2Tf = new TextField();
        this.editAsg3Tf = new TextField();
        this.editExamTf = new TextField();
        int editTfWidth = 80;
        editAsg1Tf.setMaxWidth(editTfWidth);
        editAsg2Tf.setMaxWidth(editTfWidth);
        editAsg3Tf.setMaxWidth(editTfWidth);
        editExamTf.setMaxWidth(editTfWidth);
        this.acceptChangeBtn = new Button("OK");
        acceptChangeBtn.setOnAction(e->acceptChangeBtn());
        this.cancelChangeBtn = new Button("Cancel");
        cancelChangeBtn.setOnAction(e->cancelChangeBtn());
        HBox id = new HBox();
        id.getChildren().addAll( new Label("Student Id: "), editIdLabel );
        HBox subject = new HBox();
        subject.getChildren().addAll( new Label("Subject: "), editSubjectLabel );
        HBox session = new HBox();
        session.getChildren().addAll( new Label("Session: "), editSessionLabel );
        HBox asg1 = new HBox();
        asg1.getChildren().addAll(new Label("Assignment 1: "), editAsg1Tf);
        HBox asg2 = new HBox();
        asg2.getChildren().addAll(new Label("Assignment 2: "), editAsg2Tf);
        HBox asg3 = new HBox();
        asg3.getChildren().addAll(new Label("Assignment 3: "), editAsg3Tf);
        HBox exam = new HBox();
        exam.getChildren().addAll(new Label("Exam: "), editExamTf);
        editPane.getChildren().addAll( id, subject, session, asg1, asg2, asg3, exam, acceptChangeBtn, cancelChangeBtn);
        editPane.setAlignment(javafx.geometry.Pos.CENTER);
        editPane.setHgap(20);
        
        this.idCb = new ChoiceBox<>();
        
        init();
    }
    
    public void init(){
        // create columns
        double colWidth = Asg3.WIDTH/8;
        // id col
        TableColumn<EditMark, Integer> idCol = new TableColumn<>("StudentID");
        idCol.setMinWidth(colWidth);
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        // subject col
        TableColumn<EditMark, Integer> subjectCol = new TableColumn<>("Subject");
        subjectCol.setMinWidth(colWidth);
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        // session col
        TableColumn<EditMark, Integer> sessionCol = new TableColumn<>("Session");
        sessionCol.setMinWidth(colWidth);
        sessionCol.setCellValueFactory(new PropertyValueFactory<>("session"));
        // asg1 col
        TableColumn<EditMark, String> asg1Col = new TableColumn<>("Asg 1");
        asg1Col.setMinWidth(colWidth);
        asg1Col.setCellValueFactory(new PropertyValueFactory<>("asg1"));
        // asg2 col
        TableColumn<EditMark, String> asg2Col = new TableColumn<>("Asg 2");
        asg2Col.setMinWidth(colWidth);
        asg2Col.setCellValueFactory(new PropertyValueFactory<>("asg2"));
        // asg3 col
        TableColumn<EditMark, String> asg3Col = new TableColumn<>("Asg 3");
        asg3Col.setMinWidth(colWidth);
        asg3Col.setCellValueFactory(new PropertyValueFactory<>("asg3"));
        // exam col
        TableColumn<EditMark, String> examCol = new TableColumn<>("Exam");
        examCol.setMinWidth(colWidth);
        examCol.setCellValueFactory(new PropertyValueFactory<>("exam"));
        // edit col
        TableColumn<EditMark, Button> editCol = new TableColumn<>("Edit");
        editCol.setMinWidth(colWidth/3);
        editCol.setCellValueFactory(new PropertyValueFactory<>("editBtn"));
        // delete col
        TableColumn<EditMark, Button> deleteCol = new TableColumn<>("Delete");
        deleteCol.setMinWidth(colWidth/3*2);
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
        
        // add data to table
        table.setItems(oList);
        table.getColumns().addAll(idCol, subjectCol, sessionCol, asg1Col, asg2Col, asg3Col, examCol , editCol, deleteCol);     
        for(int i = 0; i < table.getColumns().size(); ++i){
            ((TableColumn)table.getColumns().get(i)).setStyle("-fx-alignment: CENTER;");
        }
        sPane.setPrefSize(Asg3.HEIGHT, Asg3.WIDTH);
        sPane.setContent(table);
        
        FlowPane topFPane = new FlowPane();
        Button homeBtn = new Button("Home");
        homeBtn.setOnAction( e->homeBtnPress() );
        topFPane.getChildren().add(homeBtn);
        
        FlowPane bottomFPaneTitle = new FlowPane();
        FlowPane bottomFPaneAction = new FlowPane();
        GridPane gPane = new GridPane();
        Label addLabel = new Label("Add a Mark: ");
        addLabel.setFont(new Font(20d));
        Label idLabel = new Label("Student ID: ");
        idLabel.setFont(new Font(13d));
        
        double addButtonWidth = new Button("Add").getWidth();
        double padding = 10;
        double tfWidth = (Asg3.WIDTH - (idLabel.getWidth() + addButtonWidth + idCb.getWidth() + padding*6)) / 8;
//        subjectTf, sessionTf, asg1Tf, asg2Tf, asg3Tf, examTf;
        subjectTf = new TextField();
        subjectTf.setPromptText("Subject");
        subjectTf.setMaxWidth(tfWidth);
//        Label subjectLabel = new Label("Subject: ");
        subjectTf.setOnAction( e->addMark() );
        sessionTf = new TextField();
        sessionTf.setPromptText("Session");
        sessionTf.setMaxWidth(tfWidth);
//        Label sessionLabel = new Label("Session: ");
        sessionTf.setOnAction( e->addMark() );
        asg1Tf = new TextField();
        asg1Tf.setPromptText("Asg 1");
        asg1Tf.setMaxWidth(tfWidth);
//        Label asg1Label = new Label("Asg 1: ");
        asg1Tf.setOnAction( e->addMark() );
        asg2Tf = new TextField();
        asg2Tf.setPromptText("Asg 2");
        asg2Tf.setMaxWidth(tfWidth);
//        Label asg2Label = new Label
        asg2Tf.setOnAction( e->addMark() );
        asg3Tf = new TextField();
        asg3Tf.setPromptText("Asg 3");
        asg3Tf.setMaxWidth(tfWidth);
        asg3Tf.setOnAction( e->addMark() );
        examTf = new TextField();
        examTf.setPromptText("Exam");
        examTf.setOnAction( e->addMark() );
        examTf.setMaxWidth(tfWidth);
        
        HBox idSelection = new HBox();
        idSelection.getChildren().addAll(idLabel, idCb);
        
        Button addBtn = new Button("Add");
        addBtn.setOnAction( e->addMark() );
        bottomFPaneAction.getChildren().addAll(idSelection, subjectTf, sessionTf, asg1Tf, asg2Tf, asg3Tf, examTf, addBtn);
        bottomFPaneAction.setAlignment(javafx.geometry.Pos.CENTER);
        bottomFPaneAction.setMinWidth(Asg3.WIDTH);
        bottomFPaneAction.setHgap(padding);
        
        bottomFPaneTitle.getChildren().add(addLabel);
        bottomFPaneTitle.setMinWidth(Asg3.WIDTH);
        bottomFPaneTitle.setAlignment(javafx.geometry.Pos.CENTER);
        
        gPane.add(bottomFPaneTitle, 0, 0);
        gPane.add(bottomFPaneAction, 0, 1);
        gPane.setMaxWidth(Asg3.WIDTH);
        
        bPane.setCenter(sPane);
        bPane.setTop(topFPane);
        bPane.setBottom(gPane);

        this.scene = new Scene(bPane);
    }
    
    public void addMark(){
        if(idCb.getValue() == null){
            alert("Select a Student Id");
            return;
        }
            
             
        int session, asg1, asg2, asg3, exam;
        
        // get id
        int id = idCb.getValue();
        // get subject
        String subject = subjectTf.getCharacters().toString();
        if(subject.trim().isEmpty()){
            alert("Subject input cannot be empty");
            subjectTf.clear();
            return;
        }
        
        // get session and mark values and check they are valid
        try{
            if( sessionTf.getCharacters().toString().trim().isEmpty() ){
                alert("Session input cannot be empty");
                sessionTf.clear();
                return;
            }
            else{
                session = Integer.parseInt( sessionTf.getCharacters().toString() );
            }
            
            if( asg1Tf.getCharacters().toString().trim().isEmpty() ){
                alert("Assignment 1 input cannot be empty");
                asg1Tf.clear();
                return;
            }
            else{
                asg1 = Integer.parseInt( asg1Tf.getCharacters().toString() );
                if(asg1 < 0 || asg1 > 100){
                    alert("Assignment 1 input must be between 0 and 100 inclusive");
                    return;
                }
            }
            if( asg2Tf.getCharacters().toString().trim().isEmpty() ){
                alert("Assignment 2 input cannot be empty");
                asg2Tf.clear();
                return;
            }
            else{
                asg2 = Integer.parseInt( asg2Tf.getCharacters().toString() );
                if(asg2 < 0 || asg2 > 100){
                    alert("Assignment 2 input must be between 0 and 100 inclusive");
                    return;
                }
            }
            if( asg3Tf.getCharacters().toString().trim().isEmpty() ){
                alert("Assignment 3 input cannot be empty");
                asg3Tf.clear();
                return;
            }
            else{
                asg3 = Integer.parseInt( asg3Tf.getCharacters().toString() );
                if(asg3 < 0 || asg3 > 100){
                    alert("Assignment 3 input must be between 0 and 100 inclusive");
                    return;
                }
            }
            if( examTf.getCharacters().toString().trim().isEmpty() ){
                alert("Exam input cannot be empty");
                examTf.clear();
                return;
            }
            else{
                exam = Integer.parseInt( examTf.getCharacters().toString() );
                if(exam < 0 || asg1 > exam){
                    alert("Exam input must be between 0 and 100 inclusive");
                    return;
                }
            }
        }
        catch(NumberFormatException e){
            alert("Only numbers are aloud for the following inputs: Session, Assignment 1, Assignment 2, Assignment 3, Exam");
            return;
        }
        
        //TODO : check combo of id + subject + session, is new/unique

        
        // create mark
        Mark mark = new Mark(id, subject, session, asg1, asg2, asg3, exam);
        // add mark to list
        oList.add(new EditMark(this, mark));
        // add mark to db
        boolean inserted = db.insertMark(mark);
        
        if(!inserted){
            alert("Error: Combinatino of (StudentID + Subject + Session) must be unique/new");
            return;
        }
        
        // reset inputs
        idCb.setValue(null);
        subjectTf.clear();
        sessionTf.clear();
        asg1Tf.clear();
        asg2Tf.clear();
        asg3Tf.clear();
        examTf.clear();
    }
    
    public void deleteMark(EditMark editMark){
        db.deleteMark(editMark);
        oList.remove(editMark);
    }
    
    public void editMark(EditMark editMark){
        editIdLabel.setText(Integer.toString(editMark.getStudentId()));
        editSubjectLabel.setText(editMark.getSubject());
        editSessionLabel.setText(Integer.toString(editMark.getSession()));
        editAsg1Tf.setText( Integer.toString(editMark.getAsg1()) );
        editAsg2Tf.setText( Integer.toString(editMark.getAsg2()) );
        editAsg3Tf.setText( Integer.toString(editMark.getAsg3()) );
        editExamTf.setText( Integer.toString(editMark.getExam()) );
        scene.setRoot(editPane);
    }
    
    public void calcData(){
        oList.clear();
        this.idCb.getItems().clear();
        
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
                EditMark editMark = new EditMark(this, mark);
                oList.add(editMark);
            }
        }
        
        
        
        for(Student s : students)
            this.idCb.getItems().add(s.getId());
    }
    
    public Scene getScene(){
        calcData();
        return scene;
    }
    
    public void acceptChangeBtn(){
        String studentId = editIdLabel.getText();
        String asg1String = editAsg1Tf.getCharacters().toString();
        String asg2String = editAsg2Tf.getCharacters().toString();
        String asg3String = editAsg3Tf.getCharacters().toString();
        String examString = editExamTf.getCharacters().toString();
        int asg1, asg2, asg3, exam;
        
        try{ 
            asg1 = Integer.parseInt(asg1String); 
            if(asg1 > 100 || asg1 < 0){
                alert("Assignment 1 input must be between 0 and 100 inclusive");
                return;
            }
        } catch(NumberFormatException nfe) {
            alert("Assignment 1 input is not a number");
            return;
        }  
        try{ 
            asg2 = Integer.parseInt(asg2String); 
            if(asg2 > 100 || asg2 < 0){
                alert("Assignment 2 input must be between 0 and 100 inclusive");
                return;
            }
        } catch(NumberFormatException nfe) {
            alert("Assignment 2 input is not a number");
            return;
        }  
        try{ 
            asg3 = Integer.parseInt(asg3String); 
            if(asg3 > 100 || asg3 < 0){
                alert("Assignment 3 input must be between 0 and 100 inclusive");
                return;
            }
        } catch(NumberFormatException nfe) {
            alert("Assignment 3 input is not a number");
            return;
        }  
        try{ 
            exam = Integer.parseInt(examString); 
            if(exam > 100 || exam < 0){
                alert("Exam input must be between 0 and 100 inclusive");
                return;
            }
        } catch(NumberFormatException nfe) {
            alert("Exam input is not a number");
            return;
        }  
        
        db.changeMark(Integer.parseInt(editIdLabel.getText()), editSubjectLabel.getText(), Integer.parseInt(editSessionLabel.getText()), asg1, asg2, asg3, exam);
        calcData();
        scene.setRoot(bPane);   
    }
    
    public void alert(String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
    
    public void cancelChangeBtn(){
        scene.setRoot(bPane);
    }
    
    public void homeBtnPress(){
        // reset inputs
        idCb.setValue(null);
        subjectTf.clear();
        sessionTf.clear();
        asg1Tf.clear();
        asg2Tf.clear();
        asg3Tf.clear();
        examTf.clear();
        owner.homeBtnPress();
    }
    
}
