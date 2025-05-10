package controller;

import model.DataModel;
import utils.ExcelReader;
import utils.ExcelWriter;
import view.MainView;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

public class MainController {
    private DataModel model;
    private MainView view;

    public MainController(DataModel model, MainView view) {
        this.model = model;
        this.view = view;
        view.addImportListener(new ImportListener());
        view.addCalculateListener(new CalculateListener());
        view.addExportListener(new ExportListener());
    }

    class ImportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    model.setData(ExcelReader.readExcel(file.getAbsolutePath()));
                    JOptionPane.showMessageDialog(view, "Данные успешно загружены!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Ошибка импорта: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class CalculateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                model.calculateStatistics();
                JOptionPane.showMessageDialog(view, "Расчеты выполнены!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Ошибка расчета: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class ExportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Map<String, Map<String, Object>> stats = model.getStatistics();
            if (stats == null || stats.isEmpty()) {
                JOptionPane.showMessageDialog(
                    view,
                    "Ошибка: Расчеты не выполнены. Сначала выполните расчет!",
                    "Ошибка экспорта",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    ExcelWriter.writeExcel(file.getAbsolutePath(), stats);
                    JOptionPane.showMessageDialog(view, "Данные успешно экспортированы!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        view,
                        "Ошибка экспорта: " + ex.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
}