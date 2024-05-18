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
import java.util.Date;
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
    private WellNestData wellNestData;

    public WellNest() {

        this.wellNestData = new WellNestData();
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
        JTabbedPane mainTabPane = new JTabbedPane();



        // -------------------------------------------- 
        // --             Today Panel                -- 
        // -------------------------------------------- 

        // Create panels for each tab
        JPanel todayPanel = new JPanel();
        todayPanel.add(new JLabel("Content for Tab 1"));



        // -------------------------------------------- 
        // --             Stats Panel                -- 
        // -------------------------------------------- 
        JPanel statsPanel = new JPanel(new GridLayout(5, 2));
        statsPanel.add(new JLabel("Current Streak: "));
        statsPanel.add(new JLabel(String.valueOf(wellNestData.stats.currentStreak)));
        statsPanel.add(new JLabel("Longest Streak: "));
        statsPanel.add(new JLabel(String.valueOf(wellNestData.stats.longestStreak)));
        statsPanel.add(new JLabel("Total Perfect Days: "));
        statsPanel.add(new JLabel(String.valueOf(wellNestData.stats.totalPerfectDays)));
        statsPanel.add(new JLabel("Total Times Completed: "));
        statsPanel.add(new JLabel(String.valueOf(wellNestData.stats.totalTimesCompleted)));
        statsPanel.add(new JLabel("Total Days: "));
        statsPanel.add(new JLabel(String.valueOf(wellNestData.stats.totalDays)));



        // -------------------------------------------- 
        // --             All Habits Panel           -- 
        // -------------------------------------------- 

        JPanel allHabitsPanel = new JPanel();
        allHabitsPanel.add(new JLabel("Content for Tab 3"));

        // Add panels to the tabbed pane with tab titles
        mainTabPane.addTab("Today", todayPanel);
        mainTabPane.addTab("Stats", statsPanel);
        mainTabPane.addTab("All Habits", allHabitsPanel);

        JScrollPane mainPane = new JScrollPane(mainTabPane);

        this.add(header, BorderLayout.NORTH);
        this.add(mainPane, BorderLayout.CENTER);

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

        // -------------------------------------------- 
        // --             Calendar                   -- 
        // -------------------------------------------- 

    // Method to add completion timestamp
    // public void addCompletionTime(Date timestamp) {
    //     CompletionTime completionTime = new CompletionTime();
    //     completionTime.timestamp = timestamp;

    //     wellNestData.completionTimes.add(completionTime);
    //     // Update statistics here
    // }

    // Method to update statistics
    // public void updateStatistics() {
    //     // Implement statistics update logic here
    // }

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
        Date timestamp;
    }

