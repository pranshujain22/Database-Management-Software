package pranshu;
/**
 * Created by Pranshu on 20/02/18.
 */

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class updateRecordDialog {

    @FXML
    private TextField enrollment_no;
    @FXML
    private TextField student_name;
    @FXML
    private TextField cgpa;
    @FXML
    private Label search_label;
    @FXML
    private CheckBox check;

    private Database db = new Database();
    private boolean valid = false;
    private boolean confirm = false;

    public void confirm(){
        if (check.isSelected())
            confirm = true;
        else
            confirm = false;
    }

    @FXML
    public void onButtonClicked(){
        db.open();
        try {
            if (db.queryStudent(Integer.parseInt(enrollment_no.getText().trim()))){
                search_label.setText("Student id exists...");
                search_label.setTextFill(Color.GREEN);
                valid = true;
                enrollment_no.setDisable(true);
            }else {
                search_label.setText("Invalid student id!");
                search_label.setTextFill(Color.RED);
                valid = false;
            }
        }catch (Exception e){
            System.out.println("Can not search student id: " + e.getMessage());
            search_label.setTextFill(Color.RED);
            valid = false;
        }
        db.close();
    }

    @FXML
    public int updateStudent(){
        db.open();
        int status = 0;
        if (valid && confirm){
            try {
                int enroll_id = Integer.parseInt(enrollment_no.getText().trim());
                String name = student_name.getText().trim().toUpperCase();
                double student_cgpa = Double.parseDouble(cgpa.getText().trim());
                status = db.updateRecord(enroll_id, name, student_cgpa);
            }catch (Exception e){
                System.out.println("Can not resolve data type: " + e.getMessage());
                status = 2;
            }
        }
        db.close();
        return status;
    }
}
