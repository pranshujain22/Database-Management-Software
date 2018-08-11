package pranshu;
/**
 * Created by Pranshu on 20/02/18.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.*;

public class Database {

    public static String DB_NAME = "studentDb.db";
    public static String CONNECTION_STRING = "jdbc:sqlite:";
    public static final String QUERY_STUDENT = "SELECT count(enroll_id) FROM students WHERE enroll_id = ?";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS students (enroll_id INTEGER NOT NULL PRIMARY KEY," +
            " name TEXT NOT NULL, cgpa DOUBLE NOT NULL)";

    private Connection conn;
    private PreparedStatement preparedStatement;

    public boolean open() {
        try {
            CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;
            conn = DriverManager.getConnection(CONNECTION_STRING);
            createTable();
            preparedStatement = conn.prepareStatement(QUERY_STUDENT);

            return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public boolean setDB(File file){
//   e.g.:  C:\Users\Pranshu Jain\IdeaProjects\Desktop\batch.txt
        DB_NAME = file.toString();
        return true;
    }

    private boolean createTable(){
        try (Statement statement = conn.createStatement()){
            statement.execute(CREATE_TABLE);
            return true;
        }catch (SQLException e){
            System.out.println("Could not create table: " + e.getMessage());
            return false;
        }
    }

    public boolean queryStudent(int enroll_id){
        try {
            preparedStatement.setInt(1, enroll_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.getInt(1) == 1)
                return true;
            else
                return false;
        }catch (SQLException e){
            System.out.println("Cannot Query Table " + e.getMessage());
            return false;
        }
    }

    public boolean queryStudent(String name){
        try {
            preparedStatement = conn.prepareStatement("SELECT COUNT(name) FROM students WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.getInt(1) == 1)
                return true;
            else
                return false;
        }catch (SQLException e){
            System.out.println("Cannot Query Table " + e.getMessage());
            return false;
        }
    }

    public int addNewRecord(int enroll_id, String name, double cgpa){
        createTable();
        if (!queryStudent(enroll_id)){
            try (Statement statement = conn.createStatement()){
                statement.execute("INSERT INTO students VALUES (" + enroll_id + ", '" + name + "', " + cgpa + ")");
                return 1;
            }catch (SQLException e){
                System.out.println("Can not create new record: " + e.getMessage());
                return 2;
            }
        }
        else {
            System.out.println("Record Already Exists");
            return 3;
        }
    }

    public int updateRecord(int enroll_id, String name, double cgpa){
        createTable();
        if (queryStudent(enroll_id)){
            try (Statement statement = conn.createStatement()){
                statement.execute("UPDATE students SET cgpa = " + cgpa + ", name = '" + name + "' WHERE enroll_id = " + enroll_id);
                return 1;
            }catch (SQLException e){
                System.out.println("Cannot update record: " + e.getMessage());
                return 2;
            }
        }
        else {
            System.out.println("Record does not exists");
            return 3;
        }
    }

    public int deleteRecord(int enroll_id){
        createTable();
        if (queryStudent(enroll_id)){
            try (Statement statement = conn.createStatement()){
                statement.execute("DELETE FROM students WHERE enroll_id = " + enroll_id);
                return 1;
            }catch (SQLException e){
                System.out.println("Cannot delete record: " + e.getMessage());
                return 2;
            }
        }
        else {
            System.out.println("Record does not exists");
            return 3;
        }
    }

    public boolean deleteAll(boolean deleteAll){
        if (deleteAll){
            try (Statement statement = conn.createStatement()){
                statement.execute("DELETE  FROM students WHERE 1=1");
                return true;
            }catch (SQLException e){
                System.out.println("Cannot delete table: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public ObservableList<Student> getStudents(){
        ObservableList<Student> data = FXCollections.observableArrayList();
        try (Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt(1),resultSet.getString(2), resultSet.getDouble(3));
                data.add(student);
            }
            return data;
        }catch (SQLException e){
            System.out.println("Can not populate list: " + e.getMessage());
            return null;
        }
    }

    public ObservableList<Student> getStudentsSet(int enroll_id){
        ObservableList<Student> data = FXCollections.observableArrayList();
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM students WHERE enroll_id = ?");
            preparedStatement.setInt(1, enroll_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt(1),resultSet.getString(2), resultSet.getDouble(3));
                data.add(student);
            }
            return data;
        }catch (SQLException e){
            System.out.println("Can not populate list: " + e.getMessage());
            return null;
        }
    }

    public ObservableList<Student> getStudentsSet(String name){
        ObservableList<Student> data = FXCollections.observableArrayList();
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM students WHERE name LIKE '%" + name + "%'");
//            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt(1),resultSet.getString(2), resultSet.getDouble(3));
                data.add(student);
            }
            return data;
        }catch (SQLException e){
            System.out.println("Can not populate list: " + e.getMessage());
            return null;
        }
    }

    public boolean queryStudentSet(String name){
        try {
            preparedStatement = conn.prepareStatement("SELECT COUNT (name) FROM students WHERE name LIKE '%" + name + "%'");
//            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.getInt(1) > 0 )
                return true;
            else
                return false;
        }catch (SQLException e){
            System.out.println("Can not populate list: " + e.getMessage());
            return false;
        }
    }
}
