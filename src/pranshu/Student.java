package pranshu;
/**
 * Created by Pranshu on 20/02/18.
 */

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {
    private final SimpleIntegerProperty enroll_id = new SimpleIntegerProperty();
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleDoubleProperty cgpa = new SimpleDoubleProperty();

    public Student() {
        this(0, "", 0.0);
    }

    public Student(int enroll_id, String name, double cgpa) {
        setEnroll_id(enroll_id);
        setName(name);
        setCgpa(cgpa);
    }

    public int getEnroll_id() {
        return enroll_id.get();
    }

    public void setEnroll_id(int enrollId) {
        enroll_id.set(enrollId);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String Name) {
        name.set(Name);
    }

    public double getCgpa() {
        return cgpa.get();
    }

    public void setCgpa(double CGPA) {
        cgpa.set(CGPA);
    }
}
