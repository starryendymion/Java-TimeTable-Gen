package table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class TimetableGUI extends JFrame {

    private ArrayList<Teacher> teachers = new ArrayList<>();
    private DefaultListModel<String> teacherListModel;
    private JList<String> teacherList;
    private JTextField teacherNameField;
    private JTextField subjectNameField;
    private JTextField hoursField;
    private JTextField freeTimeField;
    private JTextField yearField;
    private JTextArea timetableArea;

    public TimetableGUI() {
        super("Timetable Generator");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel teacherLabel = new JLabel("Teacher Name:");
        teacherNameField = new JTextField(20);
        JLabel subjectLabel = new JLabel("Subject Name:");
        subjectNameField = new JTextField(20);
        JLabel hoursLabel = new JLabel("Hours per Week:");
        hoursField = new JTextField(5);
        JLabel freeTimeLabel = new JLabel("Free Time (e.g., 9_11):");
        freeTimeField = new JTextField(15);
        JLabel yearLabel = new JLabel("Year:");
        yearField = new JTextField(5);

        inputPanel.add(teacherLabel);
        inputPanel.add(teacherNameField);
        inputPanel.add(subjectLabel);
        inputPanel.add(subjectNameField);
        inputPanel.add(hoursLabel);
        inputPanel.add(hoursField);
        inputPanel.add(freeTimeLabel);
        inputPanel.add(freeTimeField);
        inputPanel.add(yearLabel);
        inputPanel.add(yearField);

        // List Panel
        teacherListModel = new DefaultListModel<>();
        teacherList = new JList<>(teacherListModel);
        teacherList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teacherList.setVisibleRowCount(8);
        JScrollPane listScrollPane = new JScrollPane(teacherList);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Added Entries"));
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        // Timetable Panel
        timetableArea = new JTextArea(20, 50);
        timetableArea.setEditable(false);
        JScrollPane timetableScrollPane = new JScrollPane(timetableArea);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Generated Timetable"));
        resultPanel.add(timetableScrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add Entry");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTeacherEntry();
            }
        });

        JButton clearButton = new JButton("Clear Entries");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teachers.clear();
                teacherListModel.clear();
                updateTeacherList();
            }
        });

        JButton generateButton = new JButton("Generate Timetable");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTimetable();
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(generateButton);

        add(inputPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.WEST);
        add(resultPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addTeacherEntry() {
        String teacherName = teacherNameField.getText();
        String subjectName = subjectNameField.getText();
        int hoursPerWeek;
        try {
            hoursPerWeek = Integer.parseInt(hoursField.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid input", "Please enter a valid number for hours per week.");
            return;
        }
        String freeTimeString = freeTimeField.getText();
        int year;
        try {
            year = Integer.parseInt(yearField.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid input", "Please enter a valid number for year.");
            return;
        }

        Teacher teacher = new Teacher(teacherName, new Subject(subjectName, hoursPerWeek, year), freeTimeString);
        teachers.add(teacher);
        teacherListModel.addElement(teacher.getTeacherName() + " (" + subjectName + ", Year " + year + ")");

        teacherNameField.setText("");
        subjectNameField.setText("");
        hoursField.setText("");
        freeTimeField.setText("");
        yearField.setText("");
    }

    private void updateTeacherList() {
        teacherList.setModel(teacherListModel);
    }

    private void generateTimetable() {
        Timetable timetable = new Timetable(teachers);
        timetable.assignSlots();
        Map<Integer, ArrayList<ArrayList<String>>> yearTimetables = timetable.getTimetable();

        try {
            exportTimetablesToCSV(yearTimetables);
            timetableArea.setText("Timetables generated");
        } catch (IOException e) {
            showErrorDialog("File Error", "An error occurred while writing the timetable files.");
        }
    }

    private void exportTimetablesToCSV(Map<Integer, ArrayList<ArrayList<String>>> yearTimetables) throws IOException {
        String[] timings = {"9-10", "10-11", "11-12", "12-13", "13-14", "14-15", "15-16", "16-17"};
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        for (Map.Entry<Integer, ArrayList<ArrayList<String>>> entry : yearTimetables.entrySet()) {
            int year = entry.getKey();
            ArrayList<ArrayList<String>> timetable = entry.getValue();
            try (FileWriter writer = new FileWriter("Year" + year + ".csv")) {
                writer.append("Time/Day");
                for (String timing : timings) {
                    writer.append(",").append(timing);
                }
                writer.append("\n");

                for (int i = 0; i < days.length; i++) {
                    writer.append(days[i]);
                    ArrayList<String> dayTimetable = timetable.get(i);
                    for (int j = 0; j < dayTimetable.size(); j++) {
                        if (j == 4) { // Insert "Lunch" at the 5th position (index 4)
                            writer.append(",Lunch");
                        }
                        writer.append(",").append(dayTimetable.get(j));
                    }
                    writer.append("\n");
                }
            }
        }
    }

    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TimetableGUI::new);
    }
}
