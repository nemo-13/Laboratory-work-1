package org.mephi.b23902.ogorodnikov.lw1.controller;

import org.mephi.b23902.ogorodnikov.lw1.view.MainView;
import org.mephi.b23902.ogorodnikov.lw1.model.DataModel;
import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import org.mephi.b23902.ogorodnikov.lw1.model.StatisticsCalculator;

public class MainController {
    private DataModel model;
    private MainView view;

    public MainController(DataModel model, MainView view) {
        this.model = model;
        this.view = view;
    }

    public void importData(File file) {
        try {
            model.getSheetData().clear();
            model.getSheetData().putAll(new ExcelDataReader().readAllSheets(file));
            JOptionPane.showMessageDialog(view, "Данные успешно загружены!");
        } catch (Exception e) {
            view.showError("Ошибка импорта: " + e.getMessage());
        }
    }

    public void exportData(File file) {
    try {
        if (model.getSheetData().isEmpty()) {
            throw new IllegalArgumentException("Нет данных для экспорта! Сначала загрузите данные и выполните расчет.");
        }

        Map<String, List<String>> stats = new LinkedHashMap<>();

        for (Map.Entry<String, List<List<Double>>> entry : model.getSheetData().entrySet()) {
            List<String> sheetStats = new ArrayList<>();
            List<List<Double>> samples = entry.getValue();

            if (samples.size() < 2) {
                throw new IllegalArgumentException("Для экспорта требуется минимум 2 выборки");
            }

            Map<String, List<Double>> covarianceMatrix = StatisticsCalculator.calculateCovarianceMatrix(samples);

            for (int i = 0; i < samples.size(); i++) {
                List<Double> sample = samples.get(i);
                sheetStats.add(String.format("Выборка %d:", i + 1));
                sheetStats.add(String.format("  Среднее геом.: %.4f", StatisticsCalculator.geometricMean(sample)));
                sheetStats.add(String.format("  Среднее арифм.: %.4f", StatisticsCalculator.arithmeticMean(sample)));
                sheetStats.add(String.format("  Стандартное отклонение: %.4f", StatisticsCalculator.standardDeviation(sample)));
                sheetStats.add(String.format("  Размах: %.4f", StatisticsCalculator.range(sample)));
                sheetStats.add(String.format("  Коэффициент вариации: %.4f", StatisticsCalculator.variationCoefficient(sample)));
                sheetStats.add(String.format("  Доверительный интервал: %s", StatisticsCalculator.confidenceInterval(sample)));
                sheetStats.add(String.format("  Дисперсия: %.4f", StatisticsCalculator.variance(sample)));
                sheetStats.add(String.format("  Максимум: %.4f", StatisticsCalculator.max(sample)));
                sheetStats.add(String.format("  Минимум: %.4f", StatisticsCalculator.min(sample)));
                sheetStats.add(String.format("  Количество элементов: %d", StatisticsCalculator.countElements(sample)));
                sheetStats.add("");
            }

            sheetStats.add("Матрица ковариации (" + samples.size() + "x" + samples.size() + "):");
            for (Map.Entry<String, List<Double>> matrixRow : covarianceMatrix.entrySet()) {
                String row = String.format(
                    "%s: [%s]",
                    matrixRow.getKey(),
                    matrixRow.getValue().stream()
                        .map(val -> String.format("%.4f", val))
                        .collect(Collectors.joining(", "))
                );
                sheetStats.add(row);
            }

            stats.put(entry.getKey(), sheetStats);
        }

        new ExcelDataWriter().writeStatistics(file, stats);
        JOptionPane.showMessageDialog(view, "Данные успешно экспортированы в файл: " + file.getName());

    } catch (Exception e) {
        view.showError("Ошибка экспорта: " + e.getMessage());
    }
}
    
    public void calculateData() {
    try {
        if (model.getSheetData().isEmpty()) {
            throw new IllegalArgumentException("Сначала загрузите данные!");
        }
        
        for (Map.Entry<String, List<List<Double>>> entry : model.getSheetData().entrySet()) {
            List<List<Double>> samples = entry.getValue();
            
            StatisticsCalculator.calculateCovarianceMatrix(samples);
        }
        
        JOptionPane.showMessageDialog(view, "Расчет завершен успешно!");
    } catch (Exception e) {
        view.showError("Ошибка расчета: " + e.getMessage());
        }
    }
    
    public void exit() {
        System.exit(0);
    }
}