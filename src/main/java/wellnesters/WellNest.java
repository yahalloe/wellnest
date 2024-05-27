package wellnesters;

// Built-in Classes
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 3rd-Party (Downloaded) Classes
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Local Classes

public class WellNest extends JFrame implements ActionListener, ListSelectionListener {
    // Global Variables
    private String dbFileName = "db.json";
    private WellNestData wellNestData;

    // Today Panel
    private JButton taskFinishedBtn;
    private JButton skipTaskBtn;
    private JButton editTaskBtn;
    private JButton addTaskBtn;
    DefaultListModel<String> todayTasksModel;
    JList<String> todayTasksList;
    private JPanel todayPanel;
    private JPanel originalTodayPanelContent;

    // Streak Tracker
    private DB db;
    private JLabel currentStreakLabel;
    private JLabel longestStreakLabel;
    private JLabel totalPerfectDaysLabel;
    private JLabel totalDaysLabel;
    private JLabel totalTimesCompletedLabel;

    // Add Habit Dialog
    private JTextField regularHabitTextField;

    public WellNest() {
        this.wellNestData = new WellNestData();
        // GUI Setup
        this.setLayout(new BorderLayout());
        this.setTitle("WellNest");
        this.setVisible(true);
        this.setPreferredSize(new Dimension(600, 500));
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(null);
        

        // [Create DB file (json) if it does not exist]
        File dbFile = new File(dbFileName);
        try {
            // Check if the json file exists
            if (!dbFile.exists()) {
                // Create the json file
                boolean created = dbFile.createNewFile();
                if (created) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(dbFile))) {
                        // Add empty list to the file
                        writer.write("[]");
                    } catch (IOException e) {
                        System.out.println("An error occurred while writing to the file: " + e.getMessage());
                        e.printStackTrace();
                    }

                    System.out.println("DB File created successfully.");
                } else {
                    System.out.println("DB File creation failed.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        // Main Header
        JLabel header = new JLabel("Health and Fitness Tracker", SwingConstants.CENTER);
        header.setFont(new Font("Inter", Font.BOLD, 32));

        // Main Content
        JTabbedPane mainTabPane = new JTabbedPane(JTabbedPane.NORTH);

        // --------------------------------------------
        // -- Today Panel --
        // --------------------------------------------

        todayPanel = new JPanel(new GridBagLayout());
        GridBagConstraints todayPanelGbc = new GridBagConstraints();
        todayPanelGbc.fill = GridBagConstraints.VERTICAL;
        // Padding
        todayPanelGbc.insets = new Insets(5, 0, 5, 0);
        // Margins
        todayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // -- Calendar panel --
        JLabel todayCalendarPlacdeholder = new JLabel("Calendar Placeholder");
        addComponent(todayPanel, todayCalendarPlacdeholder, todayPanelGbc, 0, 0, 1, 1, GridBagConstraints.WEST);

        // -- Tasks List Header --
        JLabel tasksHeader = new JLabel("Tasks List", SwingConstants.LEFT);
        tasksHeader.setFont(new Font("Inter", Font.BOLD, 24));
        addComponent(todayPanel, tasksHeader, todayPanelGbc,
                0, 1, 1, 1, GridBagConstraints.WEST);

        // -- Add Task Button --
        addTaskBtn = new JButton("+");
        addTaskBtn.setActionCommand("Add Task");
        addTaskBtn.addActionListener(this);
        addComponent(todayPanel, addTaskBtn, todayPanelGbc,
                0, 2, 1, 1, GridBagConstraints.EAST);

        // -- Tasks List --
        todayTasksModel = new DefaultListModel<>();
        todayTasksList = new JList<>(todayTasksModel);
        todayTasksList.addListSelectionListener(this);
        todayTasksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane todayTasksPane = new JScrollPane(todayTasksList);
        todayTasksPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        addComponent(todayPanel, todayTasksPane, todayPanelGbc,
                0, 3, 1, 1, GridBagConstraints.EAST);

        // -- Tasks List Buttons (Top) --
        JPanel tasksListTopBtnGrp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        // Task Finished Button
        taskFinishedBtn = new JButton("Finished");
        taskFinishedBtn.setActionCommand("Task Finished");
        taskFinishedBtn.addActionListener(this);
        taskFinishedBtn.setEnabled(false);

        // Skip Task Button
        skipTaskBtn = new JButton("Skip");
        skipTaskBtn.setActionCommand("Skip Task");
        skipTaskBtn.addActionListener(this);
        skipTaskBtn.setEnabled(false);

        // Edit Task Button
        editTaskBtn = new JButton("Edit");
        editTaskBtn.setActionCommand("Edit Task");
        editTaskBtn.addActionListener(this);
        editTaskBtn.setEnabled(false);

        tasksListTopBtnGrp.add(taskFinishedBtn);
        tasksListTopBtnGrp.add(skipTaskBtn);
        tasksListTopBtnGrp.add(editTaskBtn);

        addComponent(todayPanel, tasksListTopBtnGrp, todayPanelGbc,
                0, 4, 1, 1, GridBagConstraints.EAST);

        originalTodayPanelContent = new JPanel(new BorderLayout());
        originalTodayPanelContent.add(todayPanel);

        // --------------------------------------------
        // -- Stats Panel --
        // --------------------------------------------
        /**
         * @author: Ejanng
         */
        JPanel statsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints statsPanelGbc = new GridBagConstraints();
        statsPanelGbc.fill = GridBagConstraints.VERTICAL;
        // Padding
        statsPanelGbc.insets = new Insets(5, 0, 5, 0);
        // Margins
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        currentStreakLabel = new JLabel(String.valueOf(wellNestData.stats.currentStreak));
        longestStreakLabel = new JLabel(String.valueOf(wellNestData.stats.longestStreak));
        totalPerfectDaysLabel = new JLabel(String.valueOf(wellNestData.stats.totalPerfectDays));
        totalTimesCompletedLabel = new JLabel(String.valueOf(wellNestData.stats.totalTimesCompleted));
        totalDaysLabel = new JLabel(String.valueOf(wellNestData.stats.totalDays));

        JPanel streakListGrp = new JPanel();
        streakListGrp.setLayout(new BoxLayout(streakListGrp, BoxLayout.Y_AXIS));

        streakListGrp.add(Box.createVerticalStrut(200));
        streakListGrp.add(new JLabel("Current Streak: "));
        streakListGrp.add(currentStreakLabel);
        streakListGrp.add(Box.createVerticalStrut(20));
        streakListGrp.add(new JLabel("Longest Streak: "));
        streakListGrp.add(longestStreakLabel);
        streakListGrp.add(Box.createVerticalStrut(20));
        streakListGrp.add(new JLabel("Total Perfect Days: "));
        streakListGrp.add(totalPerfectDaysLabel);
        streakListGrp.add(Box.createVerticalStrut(20));
        streakListGrp.add(new JLabel("Total Times Completed: "));
        streakListGrp.add(totalTimesCompletedLabel);
        streakListGrp.add(Box.createVerticalStrut(20));
        streakListGrp.add(new JLabel("Total Days: "));
        streakListGrp.add(totalDaysLabel);
        streakListGrp.add(Box.createVerticalStrut(20));

        addComponent(statsPanel, streakListGrp, statsPanelGbc,
                0, 1, 1, 1, GridBagConstraints.WEST);

        // --------------------------------------------
        // -- All Habits Panel --
        // --------------------------------------------
        /**
         * @author: yahalloe
         */

        JPanel allHabitsPanel = new JPanel(new BorderLayout());

        JTabbedPane allHabitsTabPane = new JTabbedPane();

        JPanel regularHabitsPanel = new JPanel(new BorderLayout());
        JPanel oneTimeTasksPanel = new JPanel(new BorderLayout());

        DefaultListModel<String> regularHabitsModel = new DefaultListModel<>();
        JList<String> regularHabitsList = new JList<>(regularHabitsModel);

        regularHabitsModel.addElement("yahalloe");

        regularHabitsPanel.add(new JScrollPane(regularHabitsList), BorderLayout.CENTER);

        allHabitsTabPane.addTab("REGULAR HABITS", regularHabitsPanel);
        allHabitsTabPane.addTab("ONE-TIME TASKS", oneTimeTasksPanel);

        allHabitsPanel.add(allHabitsTabPane, BorderLayout.CENTER);

        // Add panels to the tabbed pane with tab titles
        mainTabPane.addTab("Today", todayPanel);
        mainTabPane.addTab("Stats", statsPanel);
        mainTabPane.addTab("All Habits", allHabitsPanel);

        this.add(header, BorderLayout.NORTH);
        this.add(mainTabPane, BorderLayout.CENTER);

        // Makes the program terminate when the frame is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Handle button events
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Add Task":
                addRegularHabitPanel();
                break;
            case "Task Finished":
                // TODO
                break;
            case "Skip Task":
                // TODO
                break;
            case "Edit Task":
                // TODO
                break;
            case "Save Task":
                String taskName = regularHabitTextField.getText();
                wellNestData.addRegularHabit(taskName);
                restoreTodayPanelContent();
                break;
            case "Cancel Task":
                restoreTodayPanelContent();
                break;
            default:
                System.out.println("Unknown action command: " + e.getActionCommand());
                break;
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        boolean isSelectionEmpty = todayTasksList.isSelectionEmpty();
        taskFinishedBtn.setEnabled(!isSelectionEmpty);
        skipTaskBtn.setEnabled(!isSelectionEmpty);
        editTaskBtn.setEnabled(!isSelectionEmpty);
    }

    private void addComponent(JPanel panel, Component component, GridBagConstraints gbc,
                              int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        panel.add(component, gbc);
    }

    private void addRegularHabitPanel() {
        // -- Reuse GridBagConstraints from todayPanel
        GridBagConstraints todayPanelGbc = new GridBagConstraints();
        todayPanelGbc.fill = GridBagConstraints.VERTICAL;
        todayPanelGbc.insets = new Insets(5, 0, 5, 0);

        // Create new panel for adding habit
        JPanel addRegularHabitPanel = new JPanel(new GridBagLayout());

        // Title
        JLabel addRegularHabitTitle = new JLabel("Add Regular Habit", SwingConstants.CENTER);
        addRegularHabitTitle.setFont(new Font("Inter", Font.BOLD, 24));
        addComponent(addRegularHabitPanel, addRegularHabitTitle, todayPanelGbc,
                0, 0, 2, 1, GridBagConstraints.CENTER);

        // Text Field
        regularHabitTextField = new JTextField(20);
        addComponent(addRegularHabitPanel, regularHabitTextField, todayPanelGbc,
                0, 1, 2, 1, GridBagConstraints.CENTER);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("Save Task");
        saveButton.addActionListener(this);
        buttonsPanel.add(saveButton);

        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel Task");
        cancelButton.addActionListener(this);
        buttonsPanel.add(cancelButton);

        addComponent(addRegularHabitPanel, buttonsPanel, todayPanelGbc,
                0, 2, 2, 1, GridBagConstraints.CENTER);

        // Replace content of todayPanel
        todayPanel.removeAll();
        todayPanel.add(addRegularHabitPanel);
        todayPanel.revalidate();
        todayPanel.repaint();
    }

    private void restoreTodayPanelContent() {
        todayPanel.removeAll();
        todayPanel.add(originalTodayPanelContent);
        todayPanel.revalidate();
        todayPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WellNest();
            }
        });
    }
}

class WellNestData {
    public Stats stats;

    public WellNestData() {
        stats = new Stats();
    }

    public void addRegularHabit(String habitName) {
        // TODO: Add logic to add the habit
    }

    class Stats {
        public int currentStreak;
        public int longestStreak;
        public int totalPerfectDays;
        public int totalTimesCompleted;
        public int totalDays;
    }
}
