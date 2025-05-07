package org.mephi.b23902.ogorodnikov.lw1.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import org.mephi.b23902.ogorodnikov.lw1.controller.MainController;

public class MainView extends JFrame {
    private MainController controller;

    public MainView(MainController controller) {
        this.controller = controller;
        initializeUI();
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    private void initializeUI() {
    setTitle("Statistics Tool");
    setLayout(new FlowLayout());

    JButton importBtn = new JButton("Импорт");
    importBtn.addActionListener(this::importAction);
    
    JButton calculateBtn = new JButton("Расчет");
    calculateBtn.addActionListener(this::calculateAction);
    
    JButton exportBtn = new JButton("Экспорт");
    exportBtn.addActionListener(this::exportAction);
    
    JButton exitBtn = new JButton("Выход");
    exitBtn.addActionListener(e -> controller.exit());

    add(importBtn);
    add(calculateBtn);
    add(exportBtn);
    add(exitBtn);

    setSize(350, 100);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void calculateAction(ActionEvent e) {
    controller.calculateData();
    }

    private void importAction(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            controller.importData(fc.getSelectedFile());
        }
    }

    private void exportAction(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            controller.exportData(fc.getSelectedFile());
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}