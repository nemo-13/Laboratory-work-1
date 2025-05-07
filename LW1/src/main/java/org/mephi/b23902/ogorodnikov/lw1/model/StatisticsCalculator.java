package org.mephi.b23902.ogorodnikov.lw1.model;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public class StatisticsCalculator {
    public static double geometricMean(List<Double> data) {
        double product = 1.0;
        for (double num : data) product *= num;
        return Math.pow(product, 1.0 / data.size());
    }

    public static double arithmeticMean(List<Double> data) {
        return data.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public static double standardDeviation(List<Double> data) {
        double mean = arithmeticMean(data);
        double sum = data.stream().mapToDouble(x -> Math.pow(x - mean, 2)).sum();
        return Math.sqrt(sum / (data.size() - 1));
    }

    public static double range(List<Double> data) {
        double max = data.stream().max(Double::compare).orElse(0.0);
        double min = data.stream().min(Double::compare).orElse(0.0);
        return max - min;
    }

    public static double covariance(List<Double> data1, List<Double> data2) {
        double mean1 = arithmeticMean(data1);
        double mean2 = arithmeticMean(data2);
        double sum = 0.0;
        for (int i = 0; i < data1.size(); i++) {
            sum += (data1.get(i) - mean1) * (data2.get(i) - mean2);
        }
        return sum / (data1.size() - 1);
    }
    
    public static Map<String, List<Double>> calculateCovarianceMatrix(List<List<Double>> samples) {
        Map<String, List<Double>> matrix = new LinkedHashMap<>();
        int n = samples.size();
        if (n < 2) {
            throw new IllegalArgumentException("Для расчета матрицы ковариации требуется минимум 2 выборки");
        }

        String[] labels = new String[n];
        for (int i = 0; i < n; i++) {
            labels[i] = "X" + (i + 1);
        }

        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                double cov = covariance(samples.get(i), samples.get(j));
                row.add(cov);
            }
            matrix.put(labels[i], row);
        }

        return matrix;
    }

    public static double variationCoefficient(List<Double> data) {
        return standardDeviation(data) / arithmeticMean(data);
    }

    public static String confidenceInterval(List<Double> data) {
        double mean = arithmeticMean(data);
        double se = standardDeviation(data) / Math.sqrt(data.size());
        double z = 1.96;
        return String.format("[%.2f, %.2f]", mean - z * se, mean + z * se);
    }

    public static double variance(List<Double> data) {
        double mean = arithmeticMean(data);
        return data.stream().mapToDouble(x -> Math.pow(x - mean, 2)).sum() / (data.size() - 1);
    }

    public static double max(List<Double> data) {
        return data.stream().max(Double::compare).orElse(0.0);
    }

    public static double min(List<Double> data) {
        return data.stream().min(Double::compare).orElse(0.0);
    }
    
    public static int countElements(List<Double> data) {
        return data.size();
}
}