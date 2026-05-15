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
    private JPanel holidayListPanel;
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
        AppTheme.styleWindow(this);
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
        
        // Holiday information list
        holidayListPanel = new JPanel();
        holidayListPanel.setLayout(new BoxLayout(holidayListPanel, BoxLayout.Y_AXIS));
        holidayListPanel.setBackground(AppTheme.CARD_BG);
        
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
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Ethiopian Calendar & Cultural Guide");
        titleLabel.setFont(AppTheme.TITLE_FONT.deriveFont(24f));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        // Dashboard
        JPanel dashboard = new JPanel(new GridLayout(1, 3, 20, 0));
        dashboard.setBackground(AppTheme.BACKGROUND);
        dashboard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Card 1: Today
        JPanel todayCard = new JPanel(new GridBagLayout());
        AppTheme.styleCard(todayCard);
        GridBagConstraints gbcToday = new GridBagConstraints();
        gbcToday.gridx = 0; gbcToday.gridy = 0; gbcToday.insets = new Insets(5, 5, 5, 5);
        
        JLabel todayTitle = AppTheme.sectionLabel("Today's Date");
        todayCard.add(todayTitle, gbcToday);
        
        gbcToday.gridy = 1;
        todayCard.add(gregorianDateLabel, gbcToday);
        
        gbcToday.gridy = 2;
        ethiopianDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        todayCard.add(ethiopianDateLabel, gbcToday);
        
        gbcToday.gridy = 3;
        todayCard.add(holidayLabel, gbcToday);
        
        gbcToday.gridy = 4;
        todayCard.add(seasonLabel, gbcToday);
        
        // Card 2: Holidays
        JPanel holidayCard = new JPanel(new BorderLayout(0, 10));
        AppTheme.styleCard(holidayCard);
        holidayCard.add(AppTheme.sectionLabel("Cultural Holidays & Festivals"), BorderLayout.NORTH);
        
        JScrollPane holidayScrollPane = new JScrollPane(holidayListPanel);
        holidayScrollPane.setBorder(null);
        holidayScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        holidayCard.add(holidayScrollPane, BorderLayout.CENTER);
        
        // Card 3: Converter
        JPanel convertCard = new JPanel(new GridBagLayout());
        AppTheme.styleCard(convertCard);
        GridBagConstraints gbcConv = new GridBagConstraints();
        gbcConv.insets = new Insets(10, 5, 10, 5);
        gbcConv.fill = GridBagConstraints.HORIZONTAL;
        
        gbcConv.gridx = 0; gbcConv.gridy = 0; gbcConv.gridwidth = 2;
        convertCard.add(AppTheme.sectionLabel("Date Converter"), gbcConv);
        
        gbcConv.gridy = 1; gbcConv.gridwidth = 1;
        convertCard.add(new JLabel("Gregorian:"), gbcConv);
        gbcConv.gridx = 1;
        convertCard.add(gregorianInputField, gbcConv);
        
        gbcConv.gridx = 0; gbcConv.gridy = 2;
        convertCard.add(new JLabel("Ethiopian:"), gbcConv);
        gbcConv.gridx = 1;
        convertCard.add(ethiopianInputField, gbcConv);
        
        gbcConv.gridx = 0; gbcConv.gridy = 3; gbcConv.gridwidth = 2;
        convertCard.add(convertButton, gbcConv);
        
        gbcConv.gridy = 4;
        convertCard.add(conversionResultLabel, gbcConv);
        
        dashboard.add(todayCard);
        dashboard.add(holidayCard);
        dashboard.add(convertCard);
        
        // Bottom Navigation
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setBackground(AppTheme.BACKGROUND);
        navPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        navPanel.add(backButton);
        
        add(header, BorderLayout.NORTH);
        add(dashboard, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);
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
        holidayListPanel.removeAll();
        
        addHolidayItem("Enkutatash", "Ethiopian New Year", "September 11/12", "Yellow flowers (Adey Abeba) and family feasts.");
        addHolidayItem("Meskel", "Finding of the True Cross", "September 27/28", "Huge bonfires (Demera) and traditional singing.");
        addHolidayItem("Timkat", "Epiphany", "January 19/20", "Model Arks (Tabots) processing to water bodies.");
        addHolidayItem("Genna", "Ethiopian Christmas", "January 7", "Traditional hockey-like game (Yegenna Chewata).");
        addHolidayItem("Fasika", "Ethiopian Easter", "Variable (April/May)", "End of 55-day fast; major celebration with Doro Wat.");
        addHolidayItem("Irreecha", "Oromo Thanksgiving", "Early October", "Celebration at Lake Hora near Bishoftu.");
        addHolidayItem("Kulubi Gabriel", "Saint Gabriel Festival", "December 28", "Massive pilgrimage to the Kulubi church.");
        
        holidayListPanel.revalidate();
        holidayListPanel.repaint();
    }
    
    private void addHolidayItem(String name, String type, String date, String description) {
        JPanel item = new JPanel(new BorderLayout(10, 5));
        item.setBackground(AppTheme.CARD_BG);
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(AppTheme.BODY_FONT.deriveFont(Font.BOLD));
        nameLabel.setForeground(AppTheme.PRIMARY);
        
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(AppTheme.SMALL_FONT);
        dateLabel.setForeground(AppTheme.ACCENT);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(nameLabel, BorderLayout.WEST);
        header.add(dateLabel, BorderLayout.EAST);
        
        JLabel typeLabel = new JLabel(type);
        typeLabel.setFont(AppTheme.SMALL_FONT.deriveFont(Font.ITALIC));
        typeLabel.setForeground(AppTheme.MUTED_TEXT);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(AppTheme.CARD_BG);
        descArea.setFont(AppTheme.SMALL_FONT);
        descArea.setForeground(AppTheme.TEXT);
        
        JPanel content = new JPanel(new BorderLayout(0, 2));
        content.setOpaque(false);
        content.add(typeLabel, BorderLayout.NORTH);
        content.add(descArea, BorderLayout.CENTER);
        
        item.add(header, BorderLayout.NORTH);
        item.add(content, BorderLayout.CENTER);
        
        holidayListPanel.add(item);
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
