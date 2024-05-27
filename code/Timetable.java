package table;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class Timetable {
    private ArrayList<Teacher> teachers;
    private ArrayList<String> halls;
    private ArrayList<ArrayList<ArrayList<String>>> timeSlots;
    private ArrayList<ArrayList<ArrayList<String>>> view;
    private ArrayList<String> timings;

   public Timetable(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
        this.halls = new ArrayList<>();
        this.halls.add("JC-BOSE");
        this.halls.add("Janki Ammat");
        this.halls.add("Ramanujan");

        // Initialize timings before using it
        this.timings = new ArrayList<>();
        this.timings.add("9-10");
        this.timings.add("10-11");
        this.timings.add("11-12");
        this.timings.add("12-13");
        this.timings.add("14-15");
        this.timings.add("15-16");
        this.timings.add("16-17");

        this.timeSlots = new ArrayList<>();
        this.view = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ArrayList<ArrayList<String>> daySlots = new ArrayList<>();
            ArrayList<ArrayList<String>> dayView = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ArrayList<String> slots = new ArrayList<>();
                ArrayList<String> viewEntries = new ArrayList<>();
                for (int k = 0; k < 7; k++) {
                    // Use this.timings here after initialization
                    slots.add(this.timings.get(k));
                    viewEntries.add("Free");
                }
                daySlots.add(slots);
                dayView.add(viewEntries);
            }
            this.timeSlots.add(daySlots);
            this.view.add(dayView);
        }
    }

public void assignSlots() {
    for (Teacher teacher : this.teachers) {
        ArrayList<String> freeSlots = teacher.getFreeTime();
        int hours = teacher.getSubject().getHoursNeededPerWeek();
        boolean assigned = false;

        for (int hall = 0; hall < 3; hall++) {
            if (assigned) {
                break;
            }
            for (int day = 0; day < 5; day++) {
                if (assigned) {
                    break;
                }
                int dailyHours = 0;  // Track the number of hours assigned for the current day
                for (int slot = 0; slot < 7; slot++) {
                    if (freeSlots.contains(this.timeSlots.get(hall).get(day).get(slot))
                        && hours > 0
                        && dailyHours < 2
                        && this.view.get(hall).get(day).get(slot).equals("Free")) {
                        this.view.get(hall).get(day).set(slot, teacher.getSubject().getYear() + " Year " + this.halls.get(hall) + ": " + teacher.getSubject().getSubjectName() + " (" + teacher.getTeacherName() + ")");
                        this.timeSlots.get(hall).get(day).set(slot, "Taken");
                        hours--;
                        dailyHours++;
                        if (hours == 0) {
                            assigned = true;
                            break;
                        }
                    }
                }
            }
        }
    }
}
    public Map<Integer, ArrayList<ArrayList<String>>> getTimetable() {
        Set<Integer> years = new HashSet<>();
        for (Teacher teacher : this.teachers) {
            years.add(teacher.getSubject().getYear());
        }

        Map<Integer, ArrayList<ArrayList<String>>> yearTimetables = new HashMap<>();
        for (int year : years) {
            ArrayList<ArrayList<String>> yearTimetable = new ArrayList<>();
            for (int day = 0; day < 5; day++) {
                ArrayList<String> dayTimetable = new ArrayList<>();
                for (int slot = 0; slot < 7; slot++) {
                    dayTimetable.add("Free");
                }
                yearTimetable.add(dayTimetable);
            }
            yearTimetables.put(year, yearTimetable);
        }

        for (int hall = 0; hall < 3; hall++) {
            for (int day = 0; day < 5; day++) {
                for (int slot = 0; slot < 7; slot++) {
                    if (!this.view.get(hall).get(day).get(slot).equals("Free")) {
                        String entry = this.view.get(hall).get(day).get(slot);
                        int year = Integer.parseInt(entry.split(" Year")[0]);
                        yearTimetables.get(year).get(day).set(slot, entry.substring(7));
                    }
                }
            }
        }

        return yearTimetables;
    }
}