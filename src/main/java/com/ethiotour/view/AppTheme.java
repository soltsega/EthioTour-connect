package com.ethiotour.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

final class AppTheme {
    static final Color BACKGROUND = new Color(248, 250, 252);
    static final Color SURFACE = Color.WHITE;
    static final Color TEXT = new Color(15, 23, 42);
    static final Color MUTED_TEXT = new Color(100, 116, 139);
    static final Color PRIMARY = new Color(5, 150, 105); // Emerald 600
    static final Color PRIMARY_DARK = new Color(4, 120, 87); // Emerald 700
    static final Color PRIMARY_SOFT = new Color(209, 250, 229); // Emerald 100
    static final Color ACCENT = new Color(245, 158, 11); // Amber 500
    static final Color BORDER = new Color(226, 232, 240); // Slate 200
    static final Color STRONG_BORDER = new Color(203, 213, 225); // Slate 300
    static final Color DANGER = new Color(225, 29, 72); // Rose 600
    static final Color SUCCESS = new Color(16, 185, 129); // Emerald 500
    static final Color CARD_BG = Color.WHITE;
    static final Color SHADOW = new Color(0, 0, 0, 20);

    static final Font TITLE_FONT = new Font("Inter", Font.BOLD, 32);
    static final Font SECTION_FONT = new Font("Inter", Font.BOLD, 18);
    static final Font BODY_FONT = new Font("Inter", Font.PLAIN, 14);
    static final Font SMALL_FONT = new Font("Inter", Font.PLAIN, 12);

    private AppTheme() {
    }

    static void apply() {
        try {
            FlatLightLaf.setup();
            
            // Custom UI properties for FlatLaf
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ProgressBar.arc", 8);
            
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("ScrollBar.thumbArc", 999);
            
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
            UIManager.put("Table.selectionBackground", PRIMARY_SOFT);
            UIManager.put("Table.selectionForeground", TEXT);
            
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Component.innerFocusWidth", 0);
            
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
    }

    static void stylePrimaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(spacedBorder(PRIMARY_DARK, 11, 18));
    }

    static void styleSecondaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(233, 241, 237));
        button.setForeground(PRIMARY_DARK);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(spacedBorder(STRONG_BORDER, 9, 15));
    }

    static void styleDangerButton(JButton button) {
        stylePrimaryButton(button);
        button.setBackground(DANGER);
        button.setBorder(spacedBorder(DANGER.darker(), 10, 18));
    }

    static void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setRowHeight(28);
        table.setGridColor(BORDER);
        table.setSelectionBackground(PRIMARY_DARK);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(220, 229, 224));
        table.getTableHeader().setForeground(TEXT);
    }

    static void stylePanel(JComponent component) {
        component.setBackground(BACKGROUND);
        component.setFont(BODY_FONT);
    }

    static JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SECTION_FONT);
        label.setForeground(TEXT);
        return label;
    }

    static Border panelBorder(String title) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(STRONG_BORDER), title),
            BorderFactory.createEmptyBorder(8, 10, 10, 10)
        );
    }

    static void styleWindow(JFrame frame) {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
    }

    static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
    }

    static void styleCard(JPanel panel) {
        panel.setBackground(CARD_BG);
        panel.setBorder(cardBorder());
    }

    private static Border spacedBorder(Color lineColor, int vertical, int horizontal) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(lineColor),
            BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal)
        );
    }
}
