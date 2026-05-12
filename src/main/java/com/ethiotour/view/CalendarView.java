package com.ethiotour.view;

import com.ethiotour.controller.MainController;
import com.ethiotour.util.EthiopianCalendar;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarView extends JFrame {
    private MainController controller;
    
    private JLabel gregorianDateLabel;
    private JLabel ethiopianDateLabel;
    private JLabel holidayLabel;
    private JLabel seasonLabel;
    private JTextArea holidayInfoArea;
    private JButton backButton;
    private JButton convertButton;
    private JTextField gregorianInputField;
    private JTextField ethiopianInputField;
    private JLabel conversionResultLabel;
    
    public CalendarView(MainController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateCalendarInfo();
    }
    
    private void initializeComponents() {
        setTitle("Ethiopian Calendar - EthioTour Connect");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppTheme.BACKGROUND);
        
        // Current date labels
        gregorianDateLabel = new JLabel("", SwingConstants.CENTER);
        gregorianDateLabel.setFont(AppTheme.SECTION_FONT);
        gregorianDateLabel.setForeground(AppTheme.PRIMARY);
        
        ethiopianDateLabel = new JLabel("", SwingConstants.CENTER);
        ethiopianDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        ethiopianDateLabel.setForeground(AppTheme.TEXT);
        
        holidayLabel = new JLabel("", SwingConstants.CENTER);
        holidayLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        holidayLabel.setForeground(AppTheme.ACCENT);
        
        seasonLabel = new JLabel("", SwingConstants.CENTER);
        seasonLabel.setFont(AppTheme.BODY_FONT);
        seasonLabel.setForeground(AppTheme.MUTED_TEXT);
        
        // Holiday information
        holidayInfoArea = new JTextArea(8, 40);
        holidayInfoArea.setEditable(false);
        holidayInfoArea.setLineWrap(true);
        holidayInfoArea.setWrapStyleWord(true);
        holidayInfoArea.setFont(AppTheme.BODY_FONT);
        
        // Conversion tools
        convertButton = new JButton("Convert Date");
        AppTheme.stylePrimaryButton(convertButton);
        gregorianInputField = new JTextField(12);
        ethiopianInputField = new JTextField(15);
        conversionResultLabel = new JLabel("", SwingConstants.CENTER);
        conversionResultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Navigation
        backButton = new JButton("Back to Main");
        AppTheme.styleSecondaryButton(backButton);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with current dates
        JPanel topPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        topPanel.setBackground(AppTheme.SURFACE);
        topPanel.setBorder(AppTheme.panelBorder("Current Date Information"));
        topPanel.add(gregorianDateLabel);
        topPanel.add(ethiopianDateLabel);
        topPanel.add(holidayLabel);
        topPanel.add(seasonLabel);
        
        // Center panel with holiday info
        JScrollPane holidayScrollPane = new JScrollPane(holidayInfoArea);
        holidayScrollPane.setBorder(AppTheme.panelBorder("Ethiopian Holidays & Festivals"));
        
        // Bottom panel with conversion tools
        JPanel conversionPanel = new JPanel(new GridBagLayout());
        conversionPanel.setBackground(AppTheme.SURFACE);
        conversionPanel.setBorder(AppTheme.panelBorder("Date Conversion"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        conversionPanel.add(new JLabel("Gregorian (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        conversionPanel.add(gregorianInputField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        conversionPanel.add(new JLabel("Ethiopian (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        conversionPanel.add(ethiopianInputField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        conversionPanel.add(convertButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        conversionPanel.add(conversionResultLabel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(AppTheme.BACKGROUND);
        buttonPanel.add(backButton);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppTheme.BACKGROUND);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(holidayScrollPane, BorderLayout.CENTER);
        mainPanel.add(conversionPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        convertButton.addActionListener(e -> convertDate());
        backButton.addActionListener(e -> controller.returnToMain());
        
        // Set up enter key actions
        gregorianInputField.addActionListener(e -> convertFromGregorian());
        ethiopianInputField.addActionListener(e -> convertFromEthiopian());
    }
    
    private void updateCalendarInfo() {
        LocalDate today = LocalDate.now();
        
        // Update current date labels
        gregorianDateLabel.setText("Gregorian: " + today.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
        ethiopianDateLabel.setText("Ethiopian: " + EthiopianCalendar.getEthiopianDateDisplay(today));
        
        // Check for holiday today
        if (EthiopianCalendar.isEthiopianHoliday(today)) {
            String holidayName = EthiopianCalendar.getHolidayName(today);
            holidayLabel.setText("Today is " + holidayName + " Holiday.");
            holidayLabel.setForeground(AppTheme.ACCENT);
        } else {
            holidayLabel.setText("No holiday today");
            holidayLabel.setForeground(AppTheme.MUTED_TEXT);
        }
        
        // Check season
        if (EthiopianCalendar.isPeakSeason(today)) {
            seasonLabel.setText("Peak Season - Higher tourism activity");
            seasonLabel.setForeground(AppTheme.ACCENT);
        } else {
            seasonLabel.setText("Regular Season");
            seasonLabel.setForeground(AppTheme.MUTED_TEXT);
        }
        
        // Update holiday information
        updateHolidayInfo();
    }
    
    private void updateHolidayInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Major Ethiopian Holidays and Festivals:\n\n");
        
        info.append("- Enkutatash (Ethiopian New Year)\n");
        info.append("  - September 11 (or 12 in leap years)\n");
        info.append("  - Celebrates the beginning of the Ethiopian year\n\n");
        
        info.append("- Meskel (Finding of the True Cross)\n");
        info.append("  - September 27\n");
        info.append("  - Major religious festival with bonfire ceremonies\n\n");
        
        info.append("- Timkat (Epiphany)\n");
        info.append("  - January 19\n");
        info.append("  - Commemorates the baptism of Jesus\n\n");
        
        info.append("- Mawlid (Prophet Muhammad's Birthday)\n");
        info.append("  - Variable date (usually September)\n");
        info.append("  - Important Islamic celebration\n\n");
        
        info.append("- Ethiopian Christmas (Genna)\n");
        info.append("  - January 7\n");
        info.append("  - Celebrated according to the Julian calendar\n\n");
        
        info.append("- Ethiopian Easter (Fasika)\n");
        info.append("  - Variable date (usually April/May)\n");
        info.append("  - Most important religious holiday\n\n");
        
        info.append("Peak Tourist Seasons:\n");
        info.append("- September - November: Post-rainy season, pleasant weather\n");
        info.append("- January - March: Dry season, ideal for travel\n");
        info.append("- During major festivals: Increased tourism activity\n\n");
        
        info.append("Note: Ethiopian calendar is 7-8 years behind the Gregorian calendar");
        info.append(" and consists of 12 months of 30 days each, plus a 13th month");
        info.append(" of 5 or 6 days.");
        
        holidayInfoArea.setText(info.toString());
        holidayInfoArea.setCaretPosition(0);
    }
    
    private void convertDate() {
        String gregorianText = gregorianInputField.getText().trim();
        String ethiopianText = ethiopianInputField.getText().trim();
        
        if (!gregorianText.isEmpty()) {
            convertFromGregorian();
        } else if (!ethiopianText.isEmpty()) {
            convertFromEthiopian();
        } else {
            conversionResultLabel.setText("Please enter a date to convert");
            conversionResultLabel.setForeground(Color.RED);
        }
    }
    
    private void convertFromGregorian() {
        try {
            String gregorianText = gregorianInputField.getText().trim();
            if (gregorianText.isEmpty()) {
                conversionResultLabel.setText("Please enter a Gregorian date");
                conversionResultLabel.setForeground(Color.RED);
                return;
            }
            
            LocalDate gregorianDate = LocalDate.parse(gregorianText);
            String ethiopianDate = EthiopianCalendar.getEthiopianDateDisplay(gregorianDate);
            
            conversionResultLabel.setText("Gregorian " + gregorianText + " = Ethiopian " + ethiopianDate);
            conversionResultLabel.setForeground(Color.BLUE);
            
            // Check if it's a holiday
            if (EthiopianCalendar.isEthiopianHoliday(gregorianDate)) {
                String holidayName = EthiopianCalendar.getHolidayName(gregorianDate);
                conversionResultLabel.setText(conversionResultLabel.getText() + " (" + holidayName + " Holiday)");
                conversionResultLabel.setForeground(Color.RED);
            }
            
            ethiopianInputField.setText(""); // Clear the other field
            
        } catch (Exception ex) {
            conversionResultLabel.setText("Invalid Gregorian date format. Use YYYY-MM-DD");
            conversionResultLabel.setForeground(Color.RED);
        }
    }
    
    private void convertFromEthiopian() {
        try {
            String ethiopianText = ethiopianInputField.getText().trim();
            if (ethiopianText.isEmpty()) {
                conversionResultLabel.setText("Please enter an Ethiopian date");
                conversionResultLabel.setForeground(Color.RED);
                return;
            }
            
            String[] parts = ethiopianText.split("-");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid format");
            }
            
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            
            LocalDate gregorianDate = EthiopianCalendar.convertToGregorian(year, month, day);
            
            conversionResultLabel.setText("Ethiopian " + ethiopianText + " = Gregorian " + gregorianDate);
            conversionResultLabel.setForeground(Color.BLUE);
            
            // Check if it's a holiday
            if (EthiopianCalendar.isEthiopianHoliday(gregorianDate)) {
                String holidayName = EthiopianCalendar.getHolidayName(gregorianDate);
                conversionResultLabel.setText(conversionResultLabel.getText() + " (" + holidayName + " Holiday)");
                conversionResultLabel.setForeground(Color.RED);
            }
            
            gregorianInputField.setText(""); // Clear the other field
            
        } catch (Exception ex) {
            conversionResultLabel.setText("Invalid Ethiopian date format. Use YYYY-MM-DD");
            conversionResultLabel.setForeground(Color.RED);
        }
    }
    
}
