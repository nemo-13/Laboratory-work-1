package view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private JButton importButton;
    private JButton calculateButton;
    private JButton exportButton;

    public MainView() {
        setTitle("Статистический калькулятор");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 100);
        setLayout(new FlowLayout());
        
        importButton = new JButton("Импорт XLSX");
        calculateButton = new JButton("Рассчитать");
        exportButton = new JButton("Экспорт XLSX");
        
        add(importButton);
        add(calculateButton);
        add(exportButton);
    }

    public void addImportListener(java.awt.event.ActionListener listener) {
        importButton.addActionListener(listener);
    }

    public void addCalculateListener(java.awt.event.ActionListener listener) {
        calculateButton.addActionListener(listener);
    }

    public void addExportListener(java.awt.event.ActionListener listener) {
        exportButton.addActionListener(listener);
    }
}