package asg3;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private final static String DB_CONNECTION_STR = "jdbc:mysql://localhost";
    private final static String USER = "scott";
    private final static String PASSWORD = "tiger";
    
    private Connection connection;
    private Statement statement;
    
    private PreparedStatement insertStudentStatement;
    private PreparedStatement insertMarkStatement;
    private PreparedStatement testLegalMarkInsertStatement;
    private PreparedStatement getNameFromIdStatement;
    private PreparedStatement getStudentFromNameStatement;
    private PreparedStatement getMarkFromIdStatement;
    private PreparedStatement deleteStudentStatement;
    private PreparedStatement deleteStudentFromMarkStatement;
    private PreparedStatement deleteMarkFromMarkStatement;
    private PreparedStatement changeNameStatement;
    private PreparedStatement changeMarkStatement;
    
    private int nextId = 1001;
    

    public void initDatabase(){
//         TODO: do i need to do something to say that both tables share studentID ?
        String createDatabase ="CREATE DATABASE IF NOT EXISTS assignment3;";
        String useDatabase = "USE assignment3;";
        String createStudentTable = "CREATE TABLE IF NOT EXISTS Student( " +
            "StudentID INT UNSIGNED NOT NULL PRIMARY KEY, " +
            "Name VARCHAR(10) NOT NULL" + 
        ");";
        String createMarkTable = "CREATE TABLE IF NOT EXISTS Mark( " +
            "StudentID INT UNSIGNED, " +
            "Subject CHAR(6) NOT NULL, " +
            "Session INT UNSIGNED NOT NULL, " +
            "Assignment1 INT UNSIGNED DEFAULT 0, " +
            "Assignment2 INT UNSIGNED DEFAULT 0," +
            "Assignment3 INT UNSIGNED DEFAULT 0, " +
            "FinalExam INT UNSIGNED DEFAULT 0," +
            "PRIMARY KEY(StudentID, Subject, Session)" + 
        ");";
        String getMaxId = "SELECT StudentID FROM Student WHERE StudentID=( SELECT max(StudentID) FROM Student);";
        
        try{
            this.connection = DriverManager.getConnection(DB_CONNECTION_STR, USER, PASSWORD);
            this.statement = connection.createStatement();
            
            statement.execute(createDatabase);
            statement.execute(useDatabase);
            statement.execute(createStudentTable);
            statement.execute(createMarkTable);
            
            // set nextId to the highest in the Student table plus one, or leave at default if there are none
            ResultSet resultSet = statement.executeQuery(getMaxId);
            if(resultSet.next())
                this.nextId = resultSet.getInt(1) + 1;
            
            // prepare statements
            insertStudentStatement = connection.prepareStatement("INSERT INTO Student VALUES (?,?);");
            insertMarkStatement = connection.prepareStatement("INSERT INTO Mark VALUES (?,?,?,?,?,?,?);");
            testLegalMarkInsertStatement = connection.prepareStatement("SELECT COUNT(StudentID) FROM Mark WHERE "+
                    "StudentId=? && Subject=? && Session=?;");
            getNameFromIdStatement = connection.prepareStatement("SELECT Name FROM Student WHERE StudentID=?;");
            getStudentFromNameStatement = connection.prepareStatement("SELECT StudentID, Name FROM Student WHERE Name=?;");
            getMarkFromIdStatement = connection.prepareStatement(
                    "SELECT Subject, Session, Assignment1, Assignment2, Assignment3, FinalExam FROM Mark WHERE StudentID=?;");
            deleteStudentStatement = connection.prepareStatement("DELETE FROM Student WHERE StudentID=?;");
            deleteStudentFromMarkStatement = connection.prepareStatement("DELETE FROM Mark WHERE StudentID=?;");
            deleteMarkFromMarkStatement = connection.prepareStatement("DELETE FROM Mark WHERE StudentID=? && Subject=? && Session=?;");
            changeNameStatement = connection.prepareStatement("UPDATE Student SET Name=? WHERE StudentID=?");
            changeMarkStatement = connection.prepareStatement("UPDATE Mark SET Assignment1=?, Assignment2=?, Assignment3=?, FinalExam=? WHERE StudentID=? && Subject=? && Session=?");
            
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void printStudents(){
        try{
            ResultSet resultSet = statement.executeQuery("Select StudentID, Name FROM Student");
            while(resultSet.next()){
                System.out.println(resultSet.getInt(1) + resultSet.getString(2));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void printMarks(){
        try{
            ResultSet resultSet = statement.executeQuery("Select StudentID, Subject, Session, Assignment1, Assignment2, Assignment3, FinalExam FROM Mark");
            while(resultSet.next()){
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getInt(3) + " " + resultSet.getInt(4) + " " + resultSet.getInt(5) + " " + resultSet.getInt(6) + " " + resultSet.getInt(7));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public Student insertStudent(String name){
        if( name == null || name.length() == 0 )
            return null;
        
        int id = nextId++;
        try{
            insertStudentStatement.setInt(1, id);
            insertStudentStatement.setString(2, name);
            insertStudentStatement.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return new Student(id, name);
    }
    
    // take a mark object ?
    public boolean insertMark(Mark mark){
        // student exist
        if( !studentIdExists(mark.getStudentId()) )
            return false;
//        // student isnt already taking that subject this session
        boolean inserted = false;
        try{
            testLegalMarkInsertStatement.setInt( 1, mark.getStudentId() );
            testLegalMarkInsertStatement.setString( 2, mark.getSubject() );
            testLegalMarkInsertStatement.setInt( 3, mark.getSession() );
            ResultSet resultSet = testLegalMarkInsertStatement.executeQuery();

            if(resultSet.next())
                if(resultSet.getInt(1) > 0)
                    return false;
            // else insert it
            insertMarkStatement.setInt(1, mark.getStudentId());
            insertMarkStatement.setString(2, mark.getSubject());
            insertMarkStatement.setInt(3, mark.getSession());
            insertMarkStatement.setInt(4, mark.getAsg1());
            insertMarkStatement.setInt(5, mark.getAsg2());
            insertMarkStatement.setInt(6, mark.getAsg3());
            insertMarkStatement.setInt(7, mark.getExam());
            insertMarkStatement.execute();
            inserted = true;
            // TODO: are we supposed to add these results now, or add without them and then they are modified later?
           
        }
        catch(SQLException e){
            inserted = false;
        }
        return inserted;
    }
    
    public boolean studentIdExists(int studentId){
        return getNameFromId(studentId) != null;
    }
    
    // returns assosiated name, 
    // or null if no such student id exists
    public String getNameFromId(int studentId){
        String name = null;
        try{
            getNameFromIdStatement.setInt(1, studentId);
            ResultSet resultSet = getNameFromIdStatement.executeQuery();
            if(resultSet.next())
                name = resultSet.getString(1);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return name;
    }
    
     //returns assosiated ids 
    //Could be multiple names,
     //and therefore arraylist of ids returned, which will be empty if there are none
    public ArrayList<Student> getStudentFromName(String studentName){
        ArrayList<Student> students = new ArrayList<>();
        
        try{
            getStudentFromNameStatement.setString(1, studentName);
            ResultSet resultSet = getStudentFromNameStatement.executeQuery();
            while(resultSet.next())
                students.add( new Student( resultSet.getInt(1), resultSet.getString(2) ) );
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        return students;
    }
    
    // returns an arraylist of Marks
    // the list will be empty if the studentId specified does not exist or is not in any subjects
    public Mark[] getMarksFromId(int studentId){
        // check id exists
//        if( getNameFromId(studentId) == null )
//            return null;

       
        Mark[] marks = null;
        Mark mark;
        //retrieve marks

        try{
            getMarkFromIdStatement.setInt(1, studentId);
            ResultSet resultSet = getMarkFromIdStatement.executeQuery();
            
            int numMarks = 0;
            if(resultSet.last()){
                numMarks = resultSet.getRow();
                resultSet.beforeFirst();
            }
            
            marks = new Mark[numMarks];
            for(int i = 0; resultSet.next(); ++i){
                mark = new Mark(
                        studentId,
                        resultSet.getString(1), // subject
                        resultSet.getInt(2),    // session
                        resultSet.getInt(3),    // asg1
                        resultSet.getInt(4),    // asg2
                        resultSet.getInt(5),    // asg3
                        resultSet.getInt(6)    // exam
                );
                marks[i] = mark;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        return marks;
    }
    
    // returns an int array containing all StudentIds
    // returns null if there are none
    public int[] getAllIds(){
        // get ids
        int[] ids = null;
        
        try{
            ResultSet resultSet = statement.executeQuery("SELECT StudentID FROM Student;");
            int numIds = 0;
            if(resultSet.last()){
                numIds = resultSet.getRow();
                resultSet.beforeFirst();
            }
            
            if(numIds == 0)
                return null;
            
            ids = new int[numIds];
            for(int i = 0; resultSet.next(); ++i){
               ids[i] = resultSet.getInt(1);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
                    
        return ids;
    }
    
    public Student[] getAllStudents(){
        Student[] students = null;
        
        try{
            ResultSet resultSet = statement.executeQuery("SELECT StudentID, Name FROM Student ORDER BY StudentID;");
            int numStudents = 0;
            if(resultSet.last()){
                numStudents = resultSet.getRow();
                resultSet.beforeFirst();
            }
            
            students = new Student[numStudents];
            for(int i = 0; resultSet.next(); ++i){
               students[i] = new Student( resultSet.getInt(1), resultSet.getString(2) );
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        return students;
    }
    
    // update methods
    
    public void deleteStudent(int studentId){
        try{
            deleteStudentStatement.setInt(1, studentId);
            deleteStudentFromMarkStatement.setInt(1, studentId);
            deleteStudentStatement.execute();
            deleteStudentFromMarkStatement.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void deleteMark(EditMark editMark){
        try{
            deleteMarkFromMarkStatement.setInt(1, editMark.getStudentId());
            deleteMarkFromMarkStatement.setString(2, editMark.getSubject());
            deleteMarkFromMarkStatement.setInt(3, editMark.getSession());
            deleteMarkFromMarkStatement.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void changeName(int studentId, String name){
        try{
            changeNameStatement.setString(1, name);
            changeNameStatement.setInt(2, studentId);
            changeNameStatement.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public boolean changeMark(int studentId, String subject, int session, int asg1, int asg2, int asg3, int exam){
        try{//Subject=? && Session=?
            changeMarkStatement.setInt(1, asg1);
            changeMarkStatement.setInt(2, asg2);
            changeMarkStatement.setInt(3, asg3);
            changeMarkStatement.setInt(4, exam);
            changeMarkStatement.setInt(5, studentId);
            changeMarkStatement.setString(6, subject);
            changeMarkStatement.setInt(7, session);
            changeMarkStatement.execute();
        }
        catch(SQLException e){
            return false;
        }
        return true;
    }
    
    
    
    
} // END Class Database
