//author: gartly
//purpose: This file creates a GUI that allows the user to input their personal information and displays it back to them.
//usage: The user inputs their name, birth
//this code might be useful in the future

package wellnesters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PersonalInfoGUI {
    private JFrame frame;
    private JTextField nameField;
    private JTextField birthDateField;
    private JTextField genderField;
    private JTextField heightField;
    private JTextField weightField;
    private JTextArea outputArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PersonalInfoGUI().createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Personal Information");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        panel.add(nameField);

        panel.add(new JLabel("Birth Date (yyyy-mm-dd):"));
        birthDateField = new JTextField(20);
        panel.add(birthDateField);

        panel.add(new JLabel("Gender (M/F):"));
        genderField = new JTextField(20);
        panel.add(genderField);

        panel.add(new JLabel("Height (in cm):"));
        heightField = new JTextField(20);
        panel.add(heightField);

        panel.add(new JLabel("Weight (in kg):"));
        weightField = new JTextField(20);
        panel.add(weightField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });
        panel.add(submitButton);

        outputArea = new JTextArea(10, 20);
        outputArea.setEditable(false);
        panel.add(new JScrollPane(outputArea));

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private void submit() {
        String name = nameField.getText();
        String birthDateInput = birthDateField.getText();
        LocalDate birthDate = LocalDate.parse(birthDateInput, DateTimeFormatter.ISO_DATE);
        String gender = genderField.getText();
        double height = Double.parseDouble(heightField.getText());
        double weight = Double.parseDouble(weightField.getText());

        outputArea.setText("Here is your personal information:\n");
        outputArea.append("Name: " + name + "\n");
        outputArea.append("Birth Date: " + birthDateInput + "\n");
        outputArea.append("Gender: " + gender + "\n");
        outputArea.append("Height: " + height + " cm\n");
        outputArea.append("Weight: " + weight + " kg\n");


    }
}