package table;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Teacher {
    private String teacherName;
    private Subject subject;
    private ArrayList<String> freeTime;

    public Teacher(String teacherName, Subject subject, String freeTimeString) {
        this.teacherName = teacherName;
        this.subject = subject;
        this.freeTime = new ArrayList<>();

        String[] timeParts = freeTimeString.split("_");
        int start = Integer.parseInt(timeParts[0]);
        int end = Integer.parseInt(timeParts[1]);

        for (int hour = start; hour < end; hour++) {
            freeTime.add(hour + "-" + (hour + 1));
        }

        if (end == 14) {
            freeTime.set(freeTime.size() - 1, "lunch");
        }
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Subject getSubject() {
        return subject;
    }

    public ArrayList<String> getFreeTime() {
        return freeTime;
    }
}