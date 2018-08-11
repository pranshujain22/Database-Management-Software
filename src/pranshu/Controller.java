package pranshu;
/**
 * Created by Pranshu on 20/02/18.
 */

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class Controller {

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private MenuItem addNewRecord;
    @FXML
    private MenuItem addBatchRecord;
    @FXML
    private MenuItem updateBatchRecord;
    @FXML
    private MenuItem updateRecord;
    @FXML
    private MenuItem deleteRecord;
    @FXML
    private MenuItem exit;
    @FXML
    private TextField search_keyword;
    @FXML
    private Button search_button;
    @FXML
    private Label search_Label;
    @FXML
    private RadioButton by_id;
    @FXML
    private RadioButton by_name;
    @FXML
    private TableView<String> status_table;
    @FXML
    private TableView<Student> tableView;

    private TableColumn<Student, Integer> enrollIdCol;
    private TableColumn<Student, String> nameCol;
    private TableColumn<Student, Double> cgpaCol;
    private TableColumn<String, String> statusCol;

    Database db = new Database();
    private ObservableList<String> status_data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        search_button.setDisable(true);
        enrollIdCol = new TableColumn<>("Enroll_ID");
        enrollIdCol.setMinWidth(200);
        enrollIdCol.setCellValueFactory(new PropertyValueFactory<>("enroll_id"));
        nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cgpaCol = new TableColumn<>("CGPA");
        cgpaCol.setMinWidth(200);
        cgpaCol.setCellValueFactory(new PropertyValueFactory<>("cgpa"));
        tableView.getColumns().addAll(enrollIdCol, nameCol, cgpaCol);

        statusCol = new TableColumn<>("Console");
        statusCol.setMinWidth(200);
        statusCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue()));
        status_table.getColumns().add(statusCol);
        status_table.setItems(status_data);
    }

    @FXML
    public void addNewDialog(ActionEvent e) throws FileNotFoundException {
        if (e.getSource() == addNewRecord)
            addNewDialogBox("addNewRecord.fxml", "Add New Student Record", "Use this dialog to create a new Student Record");
        else if (e.getSource() == addBatchRecord)
            addBatch();
        else if (e.getSource() == updateRecord)
            updateRecordDialogBox("updateRecord.fxml", "Update Student Record", "Use this dialog to update a Student Record");
        else if (e.getSource() == updateBatchRecord)
            updateBatch();
        else if (e.getSource() == deleteRecord)
            deleteRecordDialogBox("deleteRecord.fxml", "Delete Student Record", "Use this dialog to delete a Student Record");
        else if (e.getSource() == exit)
            System.exit(2);
    }

    @FXML
    public void addNewDialogBox(String fxml_name, String title, String header) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxml_name));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        int status = 0;
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewRecordDialog controller = fxmlLoader.getController();
            status = controller.addNewStudent();
        }
        if (status == 0) {
            status_data.add("You must click on: \"I Agree\"");
        } else if (status == 1) {
            status_data.add("Record Added Successfully");
        } else if (status == 2) {
            status_data.add("Cannot add to Database");
        } else if (status == 3) {
            status_data.add("Record Already Exists");
        }
        status_table.refresh();
        refreshRecordTable();
    }

    @FXML
    public void updateRecordDialogBox(String fxml_name, String title, String header) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxml_name));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        int status = 0;
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updateRecordDialog controller = fxmlLoader.getController();
            status = controller.updateStudent();
        }
        if (status == 0) {
            status_data.add("You must click on: \"I Agree\"");
        } else if (status == 1) {
            status_data.add("Record Updated Successfully");
        } else if (status == 2) {
            status_data.add("Cannot update Record");
        } else if (status == 3) {
            status_data.add("Record does not exists");
        }
        status_table.refresh();
        refreshRecordTable();
    }

    @FXML
    public void deleteRecordDialogBox(String fxml_name, String title, String header) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxml_name));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        int status = 0;
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteRecordDialog controller = fxmlLoader.getController();
            status = controller.deleteStudent();
        }
        if (status == 0) {
            status_data.add("You must click on: \"I Agree\"");
        } else if (status == 1) {
            status_data.add("Record Deleted Successfully");
        } else if (status == 2) {
            status_data.add("Cannot Delete Record");
        } else if (status == 3) {
            status_data.add("Record does not exists");
        }
        status_table.refresh();
        refreshRecordTable();
    }

    public void refreshRecordTable(){
        db.open();
        search_keyword.setText(null);
        tableView.setItems(db.getStudents());
        db.close();
    }

    @FXML
    public void showTable() {
        search_button.setDisable(true);

        status_data.add("Retrieving Records From Database ....");
        status_table.refresh();

        db.open();
        search_keyword.setText(null);
        tableView.setItems(db.getStudents());
        db.close();

        status_data.add("Data Retrieved Successfully ....");
        status_table.refresh();
    }

    @FXML
    public void onTextField() {
        search_button.setDisable(search_keyword.getText().trim().isEmpty());
    }

    @FXML
    public void searchRecord() {
        status_data.add("Retrieving Records From Database ....");
        status_table.refresh();
        db.open();

        try {
            if (by_id.isSelected()) {
                int enroll_id = Integer.parseInt(search_keyword.getText().trim());
                if (!db.queryStudent(enroll_id)) {
                    search_Label.setText("No Match Found...!");
                    search_Label.setTextFill(Color.RED);
                    tableView.setItems(null);
                    return;
                }
                tableView.setItems(db.getStudentsSet(enroll_id));
                search_Label.setText("Student Record Found...");
                search_Label.setTextFill(Color.GREEN);
            } else if (by_name.isSelected()) {
                String name = search_keyword.getText().split(";")[0].trim().toUpperCase();
                if (!db.queryStudentSet(name)) {
                    search_Label.setText("No Match Found...!");
                    search_Label.setTextFill(Color.RED);
                    tableView.setItems(null);
                    return;
                }
                tableView.setItems(db.getStudentsSet(name));
                search_Label.setText("Student Record Found...");
                search_Label.setTextFill(Color.GREEN);
            }
        } catch (Exception e) {
            System.out.println("Can not resolve data type: " + e.getMessage());
            search_Label.setText("Invalid Student info..!");
            search_Label.setTextFill(Color.RED);
            tableView.setItems(null);
        }
        db.close();
        status_data.add("Data Retrieved Successfully ....");
        status_table.refresh();
    }

    @FXML
    protected void addBatch() throws FileNotFoundException {
        db.open();
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text Documents (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(extensionFilter);
        chooser.setTitle("Open File");
        try {
            File file = chooser.showOpenDialog(new Stage());
            System.out.println(file);

            Scanner sc = new Scanner(file);

            String[] list;
            int status = 0;

            status_data.clear();
            status_table.refresh();
            while (sc.hasNextLine()) {
                list = sc.nextLine().split("\\t");
                status = db.addNewRecord(Integer.parseInt(list[0].trim()), list[1].trim().toUpperCase(), Double.parseDouble(list[2].trim()));
                if (status == 1) {
                    status_data.add(list[0] + ": Added to Successfully");
                } else if (status == 2) {
                    status_data.add(list[0] + ": Cannot add to Database");
                } else if (status == 3) {
                    status_data.add(list[0] + ": Already Exists");
                }
            }

            status_data.add("Data Added Successfully ....");
            status_table.refresh();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        db.close();
        refreshRecordTable();
    }

    @FXML
    protected void updateBatch() throws FileNotFoundException {

        db.open();
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text Documents (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(extensionFilter);
        chooser.setTitle("Open File");
        try {
            File file = chooser.showOpenDialog(new Stage());
            System.out.println(file);

            Scanner sc = new Scanner(file);

            String[] list;
            int status = 0;

            status_data.clear();
            status_table.refresh();
            while (sc.hasNextLine()) {
                list = sc.nextLine().split("\\t");
                status = db.updateRecord(Integer.parseInt(list[0].trim()), list[1].trim().toUpperCase(), Double.parseDouble(list[2].trim()));
                if (status == 1) {
                    status_data.add(list[0] + ": Updated to Successfully");
                } else if (status == 2) {
                    status_data.add(list[0] + ": Cannot update to Database");
                } else if (status == 3) {
                    status_data.add(list[0] + ": Does not Exists");
                }
            }

            status_data.add("Data Updated Successfully ....");
            status_table.refresh();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        db.close();
        refreshRecordTable();
    }

    @FXML
    public void openDB() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open File");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("SQLite database files (*.db *.sqlite *.sqlite3)", "*.db");
            chooser.getExtensionFilters().add(extensionFilter);
            File file = chooser.showOpenDialog(new Stage());
            System.out.println(file);
            if (db.setDB(file)) {
                status_data.clear();
                status_table.refresh();
                status_data.add("Database Connected Successfully ....");
                status_table.refresh();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void createDB() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Create File");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("SQLite database files (*.db *.sqlite *.sqlite3)", "*.db");
            chooser.getExtensionFilters().add(extensionFilter);
            File file = chooser.showSaveDialog(new Stage());
            saveFile(file);
            System.out.println(file);
            if (db.setDB(file)){
                status_data.clear();
                status_table.refresh();
                status_data.add("Database Created Successfully ....");
                status_table.refresh();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveFile(File file) {
        try {
            FileWriter fileWriter;
            fileWriter = new FileWriter(file);
//            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}