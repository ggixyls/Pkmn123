package gevorgyan.pkmn;

import java.io.Serializable;
import java.util.UUID;

public class Student implements Serializable {
    public static final long serialVersionUID = 1L;

    private String firstName;
    private String surName;
    private String familyName;
    private String group;

    public Student(String surName, String firstName, String familyName, String group) {
        this.firstName = firstName;
        this.surName = surName;
        this.familyName = familyName;
        this.group = group;
    }

    public Student() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return
                surName + " " +
                firstName + " " +
                familyName + ", " +
                group;
    }
}
