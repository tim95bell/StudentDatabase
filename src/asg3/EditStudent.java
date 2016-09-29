package asg3;

import javafx.scene.control.Button;

/**
 * Holds the data for a row of data in the table in the edit student screen.
 * @author timbell
 */
public class EditStudent {
    private EditStudentScreen owner;
    private Student student;
    private Button editBtn;
    private Button deleteBtn;

    public EditStudent(EditStudentScreen owner, Student student) {
        this.owner = owner;
        this.student = student;
        this.editBtn = new Button("Edit");
        this.editBtn.setOnAction(e->edit());
        this.deleteBtn = new Button("Delete");
        this.deleteBtn.setOnAction(e->delete());           
    }
    
    public void edit(){
        owner.editStudent(this);
    }
    
    public void delete(){
        owner.deleteStudent(this);
    }

    public int getId() {
        return student.getId();
    }

    public String getName() {
        return student.getName();
    }

    public Button getEditBtn() {
        return editBtn;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }
    
}
