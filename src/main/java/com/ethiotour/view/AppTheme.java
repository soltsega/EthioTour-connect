package com.ethiotour.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

final class AppTheme {
    static final Color BACKGROUND = new Color(246, 248, 245);
    static final Color SURFACE = Color.WHITE;
    static final Color TEXT = new Color(21, 31, 27);
    static final Color MUTED_TEXT = new Color(65, 76, 70);
    static final Color PRIMARY = new Color(18, 86, 62);
    static final Color PRIMARY_DARK = new Color(9, 52, 38);
    static final Color PRIMARY_SOFT = new Color(218, 237, 228);
    static final Color ACCENT = new Color(166, 103, 14);
    static final Color BORDER = new Color(170, 184, 176);
    static final Color STRONG_BORDER = new Color(86, 112, 99);
    static final Color DANGER = new Color(148, 38, 34);

    static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 16);
    static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private AppTheme() {
    }

    static void applyDefaults() {
        UIManager.put("Button.select", PRIMARY_DARK);
        UIManager.put("Button.focus", ACCENT);
        UIManager.put("ComboBox.selectionBackground", PRIMARY_DARK);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("TextField.selectionBackground", PRIMARY_DARK);
        UIManager.put("TextField.selectionForeground", Color.WHITE);
        UIManager.put("TextArea.selectionBackground", PRIMARY_DARK);
        UIManager.put("TextArea.selectionForeground", Color.WHITE);
        UIManager.put("Table.selectionBackground", PRIMARY_DARK);
        UIManager.put("Table.selectionForeground", Color.WHITE);
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

    private static Border spacedBorder(Color lineColor, int vertical, int horizontal) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(lineColor),
            BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal)
        );
    }
}
