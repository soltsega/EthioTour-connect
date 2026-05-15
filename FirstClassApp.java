import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FirstClassApp {
    public static void main(){
        JFrame frame = new JFrame();

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        JLabel label1 = new JLabel("Number1");
        JLabel label2 = new JLabel("Number2");

        JTextField field1  = new JTextField("Enter first number", 10);
        JTextField field2  = new JTextField("Enter second number", 10);

        JCheckBox checkBox = new JCheckBox("Show Result");

        JRadioButton radioButton = new JRadioButton("Option 1");

        JRadioButton radioButton2 = new JRadioButton("Option 2");

        JComboBox<String> comboBox = new JComboBox<String>(new String[]{"Option 1", "Option 2", "Option 3"});

        JSlider slider = new JSlider(0, 100);


        JRadioButton radiobutton = new JRadioButton("Male");
        JRadioButton radiobutton2 = new JRadioButton("Female");

        JTable table = new JTable(5, 3);
        table.setBackground(Color.WHITE);


        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(50);
        progressBar.setStringPainted(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radiobutton);
        buttonGroup.add(radiobutton2);

        field1.setForeground(Color.GRAY);
        field2.setForeground(Color.GRAY);

        JTextArea area = new JTextArea(5, 30);

        JButton button1 = new JButton("Calculate");
        JButton button2 = new JButton("Clear");

        JScrollPane scrollPane = new JScrollPane(area);

        panel1.setLayout(new GridLayout(5, 2));
        panel1.setBackground(Color.YELLOW);
        panel2.setBackground(Color.GREEN);
        panel1.add(label1);
        panel1.add(field1);
        panel1.add(label2);
        panel1.add(field2);
        panel1.add(checkBox);
        panel1.add(radioButton);
        panel1.add(radioButton2);
        panel1.add(comboBox);
        panel1.add(slider);
        panel1.add(radiobutton);
        panel1.add(radiobutton2);
        panel1.add(table);
        panel1.add(progressBar);

        panel2.add(scrollPane);
        
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double num1 = Double.parseDouble(field1.getText());
                    double num2 = Double.parseDouble(field2.getText());
                    double sum = num1 + num2;
                    if (checkBox.isSelected()) {
                        area.setText("Result: " + sum);
                    }
                } catch (NumberFormatException ex) {
                    area.setText("Please enter valid numbers");
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                field1.setText("Enter first number");
                field2.setText("Enter second number");
                field1.setForeground(Color.GRAY);
                field2.setForeground(Color.GRAY);
                area.setText("");
            }
        });

        panel2.add(button1);
        panel2.add(button2);

        frame.setLayout(new BorderLayout());
        frame.add(panel1, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);

    }
}


