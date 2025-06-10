import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class TodoListGUI extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInput;
    private final String FILE_NAME = "tasks.txt";

    public TodoListGUI() {
        setTitle("To-Do List Manager");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Task list setup
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        taskInput = new JTextField();
        JButton addButton = new JButton("Add Task");
        inputPanel.add(taskInput, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Top panel with delete button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("Delete Selected");
        JButton saveExitButton = new JButton("Save & Exit");
        topPanel.add(deleteButton);
        topPanel.add(saveExitButton);
        add(topPanel, BorderLayout.NORTH);

        // Load tasks on start
        loadTasks();

        // Action listeners
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteSelectedTask());
        saveExitButton.addActionListener(e -> {
            saveTasks();
            System.exit(0);
        });

        setVisible(true);
    }

    private void loadTasks() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String task;
            while ((task = br.readLine()) != null) {
                taskListModel.addElement(task);
            }
        } catch (IOException e) {
            // Ignore if file doesn't exist
        }
    }

    private void saveTasks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskListModel.size(); i++) {
                bw.write(taskListModel.get(i));
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving tasks.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTask() {
        String task = taskInput.getText().trim();
        if (!task.isEmpty()) {
            taskListModel.addElement(task);
            taskInput.setText("");
        }
    }

    private void deleteSelectedTask() {
        int index = taskList.getSelectedIndex();
        if (index >= 0) {
            taskListModel.remove(index);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoListGUI::new);
    }
}
