package asg3;

import javafx.scene.control.Button;

/**
 * Holds the information for a row of data in the EditMarkScreen table.
 */
public class EditMark {
    private EditMarkScreen owner;
    private Mark mark;
    private Button editBtn;
    private Button deleteBtn;
    
    public EditMark(EditMarkScreen owner, Mark mark){
        this.owner = owner;
        this.mark = mark;
        this.editBtn = new Button("Edit");
        this.editBtn.setOnAction(e->edit());
        this.deleteBtn = new Button("Delete");
        this.deleteBtn.setOnAction(e->delete());      
    }
    
    public void edit(){
        owner.editMark(this);
    }
    
    public void delete(){
        owner.deleteMark(this);
    }
    
    //        int studentId, String subject, int session, int asg1, int asg2, int asg3, int exam
    
    public int getStudentId(){
        return mark.getStudentId();
    }
    
    public String getSubject(){
        return mark.getSubject();
    }
    
    public int getSession(){
        return mark.getSession();
    }
    
    public int getAsg1(){
        return mark.getAsg1();
    }
    
    public int getAsg2(){
        return mark.getAsg2();
    }
    
    public int getAsg3(){
        return mark.getAsg3();
    }
    
    public int getExam(){
        return mark.getExam();
    }
    
    public Button getEditBtn() {
        return editBtn;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }
}
