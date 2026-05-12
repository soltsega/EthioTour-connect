package com.ethiotour.view;

import com.ethiotour.controller.MainController;
import com.ethiotour.model.Tour;
import com.ethiotour.service.DatabaseService;
import com.ethiotour.util.EthiopianCalendar;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;

public class MainView extends JFrame {
    private MainController controller;
    private JPanel mainPanel;
    private JLabel headingLabel;
    private JLabel summaryLabel;
    private JLabel ethiopianDateLabel;
    private JLabel gregorianDateLabel;
    private JButton destinationsButton;
    private JButton toursButton;
    private JButton bookingsButton;
    private JButton calendarButton;
    private DatabaseService dbService;
    
    public MainView() {
        this.controller = new MainController(this);
        this.dbService = DatabaseService.getInstance();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDateTimeLabels();
    }
    

    private void initializeComponents() {
        setTitle("EthioTour Connect - Tourism Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 620));
        setSize(980, 680);
        setLocationRelativeTo(null);
        
        mainPanel = new JPanel();
        mainPanel.setBackground(AppTheme.BACKGROUND);
        mainPanel.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout(16, 8));
        headerPanel.setBackground(AppTheme.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(28, 34, 26, 34));
        
        headingLabel = new JLabel("EthioTour Connect");
        headingLabel.setFont(AppTheme.TITLE_FONT);
        headingLabel.setForeground(Color.WHITE);
        
        summaryLabel = new JLabel("Operations dashboard for destinations, tours, bookings, and Ethiopian calendar planning.");
        summaryLabel.setFont(AppTheme.BODY_FONT);
        summaryLabel.setForeground(new Color(229, 238, 233));
        
        ethiopianDateLabel = new JLabel("", SwingConstants.CENTER);
        ethiopianDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ethiopianDateLabel.setForeground(Color.WHITE);
        
        gregorianDateLabel = new JLabel("", SwingConstants.CENTER);
        gregorianDateLabel.setFont(AppTheme.SMALL_FONT);
        gregorianDateLabel.setForeground(new Color(218, 230, 224));
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.setOpaque(false);
        titlePanel.add(headingLabel);
        titlePanel.add(summaryLabel);
        
        JPanel datePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        datePanel.setOpaque(false);
        datePanel.add(ethiopianDateLabel);
        datePanel.add(gregorianDateLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(datePanel, BorderLayout.EAST);
        
        JPanel contentPanel = new JPanel(new BorderLayout(22, 22));
        contentPanel.setBackground(AppTheme.BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(26, 34, 26, 34));
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 14, 14));
        statsPanel.setBackground(AppTheme.BACKGROUND);
        statsPanel.add(createMetricPanel("Destinations", String.valueOf(dbService.getAllDestinations().size()), "Regional metadata and protocols"));
        statsPanel.add(createMetricPanel("Tours", String.valueOf(dbService.getAllTours().size()), "Capacity, dates, and pricing"));
        statsPanel.add(createMetricPanel("Bookings", String.valueOf(dbService.getAllBookings().size()), "Pending confirmations and payments"));
        statsPanel.add(createMetricPanel("Open Slots", String.valueOf(countOpenSlots()), "Available tour capacity"));
        
        JPanel navigationPanel = new JPanel(new GridLayout(2, 2, 16, 16));
        navigationPanel.setBackground(AppTheme.BACKGROUND);
        
        destinationsButton = createModuleButton("Destinations", "Maintain sites, regions, altitude notes, and entrance rules.");
        toursButton = createModuleButton("Tours", "Build itineraries with availability, resident pricing, and non-resident pricing.");
        bookingsButton = createModuleButton("Bookings", "Create reservations, confirm local payments, and update booking status.");
        calendarButton = createModuleButton("Calendar", "Review Ethiopian dates, holidays, and peak travel seasons.");
        
        navigationPanel.add(destinationsButton);
        navigationPanel.add(toursButton);
        navigationPanel.add(bookingsButton);
        navigationPanel.add(calendarButton);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 18));
        centerPanel.setBackground(AppTheme.BACKGROUND);
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(navigationPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(AppTheme.BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        bottomPanel.add(createStatusPanel(), BorderLayout.CENTER);
        
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupLayout() {
        // Layout is already set up in initializeComponents
    }
    
    private void setupEventHandlers() {
        destinationsButton.addActionListener(e -> controller.showDestinationsView());
        toursButton.addActionListener(e -> controller.showToursView());
        bookingsButton.addActionListener(e -> controller.showBookingsView());
        calendarButton.addActionListener(e -> controller.showCalendarView());
    }
    
    private void updateDateTimeLabels() {
        LocalDate today = LocalDate.now();
        gregorianDateLabel.setText("Gregorian: " + today.toString());
        ethiopianDateLabel.setText("Ethiopian: " + EthiopianCalendar.getEthiopianDateDisplay(today));
        
        // Check for holidays
        if (EthiopianCalendar.isEthiopianHoliday(today)) {
            String holidayName = EthiopianCalendar.getHolidayName(today);
            ethiopianDateLabel.setText(ethiopianDateLabel.getText() + " - " + holidayName + " Holiday!");
        }
    }

    private JPanel createMetricPanel(String label, String value, String caption) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(AppTheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER),
            BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(AppTheme.PRIMARY);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(AppTheme.SECTION_FONT);
        labelComponent.setForeground(AppTheme.TEXT);
        
        JLabel captionLabel = new JLabel(caption);
        captionLabel.setFont(AppTheme.SMALL_FONT);
        captionLabel.setForeground(AppTheme.MUTED_TEXT);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(labelComponent);
        textPanel.add(captionLabel);
        
        panel.add(valueLabel, BorderLayout.NORTH);
        panel.add(textPanel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createModuleButton(String title, String description) {
        String text = "<html><div style='font-size:15px;font-weight:bold;color:#ffffff;'>" + title + "</div>"
            + "<div style='font-size:11px;font-weight:normal;color:#e5f0ea;margin-top:6px;'>" + description + "</div></html>";
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(AppTheme.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 8, 0, 0, AppTheme.ACCENT),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.PRIMARY_DARK),
                BorderFactory.createEmptyBorder(18, 22, 18, 20)
            )
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addHoverState(button);
        return button;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 0));
        panel.setBackground(new Color(237, 242, 238));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JLabel statusLabel = new JLabel("Current mode: demonstration data");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(AppTheme.PRIMARY_DARK);

        JLabel noteLabel = new JLabel("Data resets when the app restarts. SQL Server integration steps are in doc/database_setup.md.");
        noteLabel.setFont(AppTheme.SMALL_FONT);
        noteLabel.setForeground(AppTheme.MUTED_TEXT);

        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(noteLabel, BorderLayout.CENTER);
        return panel;
    }

    private void addHoverState(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(AppTheme.PRIMARY_DARK);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 8, 0, 0, new Color(214, 146, 33)),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppTheme.PRIMARY_DARK),
                        BorderFactory.createEmptyBorder(18, 22, 18, 20)
                    )
                ));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(AppTheme.PRIMARY);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 8, 0, 0, AppTheme.ACCENT),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppTheme.PRIMARY_DARK),
                        BorderFactory.createEmptyBorder(18, 22, 18, 20)
                    )
                ));
            }
        });
    }

    private int countOpenSlots() {
        int total = 0;
        List<Tour> tours = dbService.getAllTours();
        for (Tour tour : tours) {
            total += Math.max(0, tour.getAvailableSlots());
        }
        return total;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            AppTheme.applyDefaults();
            
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
