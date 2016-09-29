/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asg3;

/**
 * Holds the data for a row in the SimpleDisplayScreen table.
 * @author timbell
 */
public class ViewRow {
    private String name;
    private int id;
    private String asg1;
    private String asg2;
    private String asg3;
    private String exam;

    public ViewRow(String name, int id, String[] marks) {
        this.name = name;
        this.id = id;
        if(marks != null){
            this.asg1 = marks[0];
            this.asg2 = marks[1];
            this.asg3 = marks[2];
            this.exam = marks[3];
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAsg1() {
        return asg1;
    }

    public String getAsg2() {
        return asg2;
    }

    public String getAsg3() {
        return asg3;
    }

    public String getExam() {
        return exam;
    }
    
    public double getTotal(){
        return (double)(0.1d*Integer.parseInt(asg1) + Integer.parseInt(asg2)*0.2d + Integer.parseInt(asg3)*0.2d + Integer.parseInt(exam)*0.5d);
    }
}
