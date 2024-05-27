package table;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Subject {
    private String subjectName;
    private int hoursNeededPerWeek;
    private int year;
    private int hoursAssigned;

    public Subject(String subjectName, int hoursNeededPerWeek, int year) {
        this.subjectName = subjectName;
        this.hoursNeededPerWeek = hoursNeededPerWeek;
        this.year = year;
        this.hoursAssigned = 0;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getHoursNeededPerWeek() {
        return hoursNeededPerWeek;
    }

    public int getYear() {
        return year;
    }

    public int getHoursAssigned() {
        return hoursAssigned;
    }

    public void setHoursAssigned(int hoursAssigned) {
        this.hoursAssigned = hoursAssigned;
    }
}