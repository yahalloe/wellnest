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
import java.util.List;

// 3rd-Party (Downloaded) Classes
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// Local Classes


public class WellNest extends JFrame {
    // Global Variables
    private String dbFileName = "db.json";

    public WellNest() {
        // GUI Setup
        this.setLayout(new BorderLayout());
        this.setTitle("WellNest");
        this.setVisible(true);
        this.setPreferredSize(new Dimension(500, 500));
        this.setMinimumSize(new Dimension(500, 500));


        // Load DB Data
        try {
            Gson gson = new Gson();

            // Load the JSON file
            String json = new String(Files.readAllBytes(Paths.get(dbFileName)));

            DB db = gson.fromJson(json, DB.class);

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Main Header
        JLabel header = new JLabel("Health and Fitness Trackers", SwingConstants.CENTER);

        // Main Content
        JTabbedPane mainTabPane = new JTabbedPane(JTabbedPane.LEFT);



        // -------------------------------------------- 
        // --             Today Panel                -- 
        // -------------------------------------------- 

        // Create panels for each tab
        JPanel todayPanel = new JPanel();
        todayPanel.add(new JLabel("Content for Tab 1"));



        // -------------------------------------------- 
        // --             Stats Panel                -- 
        // -------------------------------------------- 

        JPanel statsPanel = new JPanel();
        statsPanel.add(new JLabel("Content for Tab 2"));



        // -------------------------------------------- 
        // --             All Habits Panel           -- 
        // -------------------------------------------- 

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
    }
}
