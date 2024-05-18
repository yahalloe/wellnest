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

        JPanel statsPanel = new JPanel();
        statsPanel.add(new JLabel("Content for Tab 2"));



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
    }
}
