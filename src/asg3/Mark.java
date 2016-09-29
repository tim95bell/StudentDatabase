/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asg3;

/**
 * Holds the data for a mark.
 * @author timbell
 */

public class Mark {
    private int studentId;
    private String subject;
    private int session;
    private int asg1, asg2, asg3, exam;

    public Mark(int studentId, String subject, int session, int asg1, int asg2, int asg3, int exam) {
        this.studentId = studentId;
        this.subject = subject;
        this.session = session;
        this.asg1 = asg1;
        this.asg2 = asg2;
        this.asg3 = asg3;
        this.exam = exam;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getSubject() {
        return subject;
    }

    public int getSession() {
        return session;
    }

    public int getAsg1() {
        return asg1;
    }

    public int getAsg2() {
        return asg2;
    }

    public int getAsg3() {
        return asg3;
    }

    public int getExam() {
        return exam;
    }
    
    public int[] getAllMarks(){
        return new int[]{ asg1, asg2, asg3, exam};
    }
    
    public String[] getAllMarksAsStrings(){
        return new String[]{ Integer.toString(asg1), Integer.toString(asg2), Integer.toString(asg3), Integer.toString(exam) };
    }
    
    public String toString(){
        return "studentID: " + studentId + "\nsubject: " + subject + "\nsession: " + session + "\nasg1: " + asg1 + 
                "\nasg2: " + asg2 + "\nasg3: " + asg3 + "\nexam: " + exam;
    }
    
}
