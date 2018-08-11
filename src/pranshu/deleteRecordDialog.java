package pranshu;
/**
 * Created by Pranshu on 20/02/18.
 */

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class deleteRecordDialog {

    @FXML
    private TextField enrollment_no;
    @FXML
    private Label search_label;
    @FXML
    private CheckBox deleteAll;
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

    public int deleteStudent(){
        int status = 0;
        db.open();
        if (deleteAll.isSelected() && confirm){
            db.deleteAll(true);
            db.close();
            return 1;
        }
        if (valid & confirm){
            try {
                int enroll_id = Integer.parseInt(enrollment_no.getText().trim());
                status = db.deleteRecord(enroll_id);
            }catch (Exception e){
                System.out.println("Can not resolve data type: " + e.getMessage());
                status = 2;
            }
        }
        db.close();
        return status;
    }
}
