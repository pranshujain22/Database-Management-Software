package pranshu;
/**
 * Created by Pranshu on 20/02/18.
 */

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class NewRecordDialog {

    @FXML
    private TextField enrollment_no;
    @FXML
    private TextField student_name;
    @FXML
    private TextField cgpa;
    @FXML
    private CheckBox check;

    private Database db = new Database();
    private boolean confirm = false;

    public void confirm(){
        if (check.isSelected())
            confirm = true;
        else
            confirm = false;
    }

    public int addNewStudent(){
        db.open();
        int status = 0;
        if (confirm){
           try {
               int enroll_id = Integer.parseInt(enrollment_no.getText().trim());
               String name = student_name.getText().trim().toUpperCase();
               double student_cgpa = Double.parseDouble(cgpa.getText().trim());
               status = db.addNewRecord(enroll_id, name, student_cgpa);
           }catch (Exception e){
               System.out.println("Can not resolve data type: " + e.getMessage());
               status = 2;
           }
       }
       db.close();
       return status;
    }
}
