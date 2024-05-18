package wellnesters;

import javax.swing.*;
import java.awt.*;

public class WellNest extends JFrame {
    public WellNest() {
        this.setLayout(new BorderLayout());
        this.setTitle("WellNest");
        this.setVisible(true);
        this.setPreferredSize(new Dimension(500, 500));
        this.setMinimumSize(new Dimension(500, 500));

        // Top panel with GridBagLayout to respect preferred sizes
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0); // Add padding between components

        JLabel dayLabel = new JLabel("Date today");
        dayLabel.setPreferredSize(new Dimension(300, 50));
        dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dayLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(dayLabel, gbc);

        JLabel calendarLabel = new JLabel("Calendar");
        calendarLabel.setPreferredSize(new Dimension(300, 50));
        calendarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        calendarLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(calendarLabel, gbc);

        this.add(topPanel, BorderLayout.NORTH);

        // Task panel with GridBagLayout to respect preferred sizes
        JPanel taskPanel = new JPanel(new GridBagLayout());
        taskPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        GridBagConstraints gbcTask = new GridBagConstraints();
        gbcTask.fill = GridBagConstraints.HORIZONTAL;
        gbcTask.insets = new Insets(5, 5, 5, 5); // Add padding between components

        // Activity panel with GridBagLayout to respect preferred sizes
        JPanel activityPanel = new JPanel(new GridBagLayout());
        activityPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        GridBagConstraints gbcActivity = new GridBagConstraints();
        gbcActivity.fill = GridBagConstraints.HORIZONTAL;
        gbcActivity.insets = new Insets(5, 5, 5, 5); // Add padding between components

        JLabel background = new JLabel("DO Anytime");
        background.setPreferredSize(new Dimension(300, 100));
        background.setHorizontalAlignment(SwingConstants.CENTER);
        background.setBorder(BorderFactory.createLineBorder(Color.black));
        gbcActivity.gridx = 0;
        gbcActivity.gridy = 0;
        activityPanel.add(background, gbcActivity);

        JLabel progressBar = new JLabel("Progress");
        progressBar.setPreferredSize(new Dimension(200, 100));
        progressBar.setHorizontalAlignment(SwingConstants.CENTER);
        progressBar.setBorder(BorderFactory.createLineBorder(Color.black));
        gbcActivity.gridx = 0;
        gbcActivity.gridy = 1;
        activityPanel.add(progressBar, gbcActivity);

        gbcTask.gridx = 0;
        gbcTask.gridy = 0;
        taskPanel.add(activityPanel, gbcTask);

        this.add(taskPanel, BorderLayout.CENTER);
    }
}
