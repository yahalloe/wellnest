package wellnesters;

// Built-in Classes
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.text.NumberFormat;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.format.TextStyle;
import java.util.Locale;

// 3rd-Party (Downloaded) Classes
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    // Streak Tracker
    private DB db;
    private JLabel currentStreakLabel;
    private JLabel longestStreakLabel;
    private JLabel totalPerfectDaysLabel;
    private JLabel totalDaysLabel;
    private JLabel totalTimesCompletedLabel;
    

    //author: gartly
    //ignore this
    //personal info
    // private JFrame frame;
    // private JTextField nameField;
    // private JTextField birthDateField;
    // private JTextField genderField;
    // private JTextField heightField;
    // private JTextField weightField;
    // private JTextArea outputArea;

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


        // Load DB Data
        // try {
        // Gson gson = new Gson()
        //         .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
        //        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
        //        .create();

        //     // Load the JSON file
        //     String json = new String(Files.readAllBytes(Paths.get(dbFileName)));

        //     DB db = gson.fromJson(json, DB.class);

        // } catch (IOException e) {
        //     e.printStackTrace();
        // }


        // Main Header
        JLabel header = new JLabel("Health and Fitness Tracker", SwingConstants.CENTER);
        header.setFont(new Font("Inter", Font.BOLD, 32));

        // Main Content
        JTabbedPane mainTabPane = new JTabbedPane(JTabbedPane.NORTH);



        // -------------------------------------------- 
        // --             Today Panel                -- 
        // -------------------------------------------- 

        JPanel todayPanel = new JPanel(new GridBagLayout());
        GridBagConstraints todayPanelGbc = new GridBagConstraints();
        todayPanelGbc.fill = GridBagConstraints.VERTICAL;
        // Padding
        todayPanelGbc.insets = new Insets(5, 0, 5, 0);
        // Margins
        todayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        // -- Calendar panel --
        // JPanel todayCalendarPanel = new JPanel();
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
        DefaultListModel<String> todayTasksModel = new DefaultListModel<>();
        JList<String> todayTasksList = new JList<>(todayTasksModel);
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

        // -------------------------------------------- 
        // --             Stats Panel                -- 
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

        //removed
        // JPanel statsPanel = new JPanel(new GridLayout(5, 2));
        currentStreakLabel = new JLabel(String.valueOf(wellNestData.stats.currentStreak));
        longestStreakLabel = new JLabel(String.valueOf(wellNestData.stats.longestStreak));
        totalPerfectDaysLabel = new JLabel(String.valueOf(wellNestData.stats.totalPerfectDays));
        totalTimesCompletedLabel = new JLabel(String.valueOf(wellNestData.stats.totalTimesCompleted));
        totalDaysLabel = new JLabel(String.valueOf(wellNestData.stats.totalDays));

        // -- Calendar panel --
        // SimpleCalendar simpleCalendar = new SimpleCalendar();
        // addComponent(statsPanel, simpleCalendar, statsPanelGbc, 0, 0, 1, 1, GridBagConstraints.WEST);

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
        // --             All Habits Panel           -- 
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
 

        // ----------------------------------------------
        // --        end of All Habits Panel           -- 
        // ---------------------------------------------- 

        // Add panels to the tabbed pane with tab titles
        mainTabPane.addTab("Today", todayPanel);
        mainTabPane.addTab("Stats", statsPanel);
        mainTabPane.addTab("All Habits", allHabitsPanel);

        JScrollPane mainPane = new JScrollPane(mainTabPane);

        this.add(header, BorderLayout.NORTH);
        this.add(mainPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    // -------------------------------------------- 
    // --             Calendar                   -- 
    // -------------------------------------------- 

    // Method to add completion timestamp
    public void addCompletionTime(LocalDateTime timestamp) {
        CompletionTime completionTime = new CompletionTime();
        completionTime.timestamp = timestamp;
        wellNestData.completionTimes.add(completionTime);
        updateStatistics();
        saveData();
    }

    // Method to update statistics
    public void updateStatistics() {
        // Logic to update streaks
        int currentStreak = 0;
        int longestStreak = 0;
        int totalPerfectDays = 0;
        int totalTimesCompleted = wellNestData.completionTimes.size();
        int totalDays = 0;

        LocalDate lastCompletionDate = null;
        for (CompletionTime completionTime : wellNestData.completionTimes) {
            LocalDate completionDate = completionTime.timestamp.toLocalDate();
            if (lastCompletionDate == null || !completionDate.equals(lastCompletionDate.plusDays(1))) {
                currentStreak = 1;
            } else {
                currentStreak++;
            }
            lastCompletionDate = completionDate;
            longestStreak = Math.max(longestStreak, currentStreak);
        }

        wellNestData.stats.currentStreak = currentStreak;
        wellNestData.stats.longestStreak = longestStreak;
        wellNestData.stats.totalPerfectDays = totalPerfectDays;
        wellNestData.stats.totalTimesCompleted = totalTimesCompleted;
        wellNestData.stats.totalDays = totalDays;

        // Update labels
        currentStreakLabel.setText(String.valueOf(currentStreak));
        longestStreakLabel.setText(String.valueOf(longestStreak));
        totalPerfectDaysLabel.setText(String.valueOf(totalPerfectDays));
        totalTimesCompletedLabel.setText(String.valueOf(totalTimesCompleted));
        totalDaysLabel.setText(String.valueOf(totalDays));
    }

    // Method to save data to JSON file
    public void saveData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(wellNestData);

        try (Writer writer = new FileWriter(dbFileName)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // -------------------------------------------- 
    // --      Handles clicks to a button        -- 
    // --------------------------------------------
    public void actionPerformed(ActionEvent e) {
        // Determines which button is clicked
        String command = e.getActionCommand();

        if (command == "Add Task") {
            JDialog dialog = new JDialog(this, "New Task", true);  
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
            dialog.setPreferredSize(new Dimension(400, 400));
            dialog.setMinimumSize(new Dimension(400, 400));
            dialog.setLocationRelativeTo(null);
            JTabbedPane tabbedPane = new JTabbedPane();

            // ----------------------------------- 
            // --       Add Regular Habit       -- 
            // -----------------------------------

            JPanel addRegularHabitPanel = new JPanel(new GridBagLayout());
            GridBagConstraints addHabitGbc = new GridBagConstraints();
            addHabitGbc.fill = GridBagConstraints.VERTICAL;
            // Padding
            addHabitGbc.insets = new Insets(5, 0, 5, 0);
            // Margins
            addRegularHabitPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


            // -- Header --
            JLabel addHabitHeader = new JLabel("New Habit", SwingConstants.LEFT);
            addHabitHeader.setFont(new Font("Inter", Font.BOLD, 24));
            addComponent(addRegularHabitPanel, addHabitHeader, addHabitGbc, 0, 0, 1, 1, GridBagConstraints.WEST);


            // -- Habit Name Text Field
            regularHabitTextField = new JTextField();
            regularHabitTextField.setPreferredSize(new Dimension(300, 30));
            addComponent(addRegularHabitPanel, regularHabitTextField, addHabitGbc, 0, 1, 1, 1, GridBagConstraints.WEST);

            addComponent(addRegularHabitPanel, new JSeparator(), addHabitGbc, 0, 2, 1, 1, GridBagConstraints.WEST);


            // -- Interval --
            JPanel intervalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

            JButton daily = new JButton("Daily");
            daily.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                }
            });

            JButton weekly = new JButton("Weekly");
            weekly.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                }
            });

            JButton monthly = new JButton("Monthly");
            monthly.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                }
            });

            intervalPanel.add(daily);
            intervalPanel.add(weekly);
            intervalPanel.add(monthly);
            addComponent(addRegularHabitPanel, intervalPanel, addHabitGbc, 0, 3, 1, 1, GridBagConstraints.CENTER);


            addComponent(addRegularHabitPanel, new JSeparator(), addHabitGbc, 0, 4, 1, 1, GridBagConstraints.CENTER);



            tabbedPane.add("Regular Habit", addRegularHabitPanel);


            // ----------------------------------- 
            // --       Add One-Time Task       -- 
            // -----------------------------------

            JPanel addTaskPanel = new JPanel(new GridBagLayout());
            tabbedPane.add("One-time Task", addTaskPanel);


            dialog.add(tabbedPane);
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
        }

        if (command == "Task Finished") {

        }

        if (command == "Skip Task") {

        }

        if (command == "Edit Task") {

        }
    }

    // -------------------------------------------- 
    // --     Handle changes in the selection    --
    // --     of items in a JList                -- 
    // --------------------------------------------
    public void valueChanged(ListSelectionEvent e) {
        // Determines which JList triggered the event
        JList<?> sourceList = (JList<?>) e.getSource();

        if (!e.getValueIsAdjusting()) {
            // Today Panel Tasks List
            if (sourceList == todayTasksList) {
                // Access the selected indices or values from the JList
                // String selectedValue = ingrList.getSelectedValue().toString();

                int selectedIndex = todayTasksList.getSelectedIndex();
                // If there are items in the list
                if (selectedIndex != -1) {
                    // Enable the 'Task Finished Button'
                    taskFinishedBtn.setEnabled(true);
                    // Enable the 'Skip Task Button'
                    skipTaskBtn.setEnabled(true);
                    // Enable the 'Edit Task'
                    editTaskBtn.setEnabled(true);
                } 
                // Otherwise
                else {
                    // Enable the 'Task Finished Button'
                    taskFinishedBtn.setEnabled(false);
                    // Enable the 'Skip Task Button'
                    skipTaskBtn.setEnabled(false);
                    // Enable the 'Edit Task'
                    editTaskBtn.setEnabled(false);
                }
            }
        }
    }

    // Utility function for adding component in a GridBagLayout
    private static void addComponent(Container container, Component component, GridBagConstraints gbc,
                                     int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        container.add(component, gbc);
    }
}

    class WellNestData {
        Stats stats;
        List<CompletionTime> completionTimes;
    
        public WellNestData() {
            stats = new Stats();
            completionTimes = new ArrayList<>();
        }
    }

    class Stats {
        int currentStreak;
        int longestStreak;
        int totalPerfectDays;
        int totalTimesCompleted;
        int totalDays;
    }

    class CompletionTime {
        LocalDateTime timestamp;
    }
    

    

