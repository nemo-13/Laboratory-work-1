package model;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.*;

public class DataModel {
    private Map<String, Map<String, List<Double>>> sheetsData;
    private Map<String, Map<String, Object>> statistics;

    public void setData(Map<String, Map<String, List<Double>>> data) {
        this.sheetsData = data;
    }

    public Map<String, Map<String, Object>> getStatistics() {
        return statistics;
    }

    public void calculateStatistics() {
        statistics = new LinkedHashMap<>();
        for (String sheetName : sheetsData.keySet()) {
            Map<String, List<Double>> data = sheetsData.get(sheetName);
            Map<String, Object> sheetStats = new LinkedHashMap<>();

            Map<String, Map<String, Double>> columnsStats = new LinkedHashMap<>();
            for (String column : data.keySet()) {
                DescriptiveStatistics stats = new DescriptiveStatistics();
                data.get(column).forEach(stats::addValue);
                Map<String, Double> columnStats = new LinkedHashMap<>();

                boolean hasNonPositive = data.get(column).stream().anyMatch(v -> v <= 0);
                columnStats.put("Среднее геом.", hasNonPositive ? Double.NaN : stats.getGeometricMean());

                columnStats.put("Среднее арифм.", stats.getMean());
                columnStats.put("Стандартное отклонение", stats.getStandardDeviation());
                columnStats.put("Размах", stats.getMax() - stats.getMin());
                columnStats.put("Количество элементов", (double) stats.getN());
                columnStats.put("Коэффициент вариации", stats.getStandardDeviation() / stats.getMean());
                columnStats.put("Дисперсия", stats.getVariance());
                columnStats.put("Максимум", stats.getMax());
                columnStats.put("Минимум", stats.getMin());

                double alpha = 0.05;
                TDistribution tDist = new TDistribution(stats.getN() - 1);
                double critVal = tDist.inverseCumulativeProbability(1 - alpha / 2);
                double margin = critVal * stats.getStandardDeviation() / Math.sqrt(stats.getN());
                columnStats.put("Доверительный интервал (нижняя)", stats.getMean() - margin);
                columnStats.put("Доверительный интервал (верхняя)", stats.getMean() + margin);

                columnsStats.put(column, columnStats);
            }
            sheetStats.put("Столбцы", columnsStats);

            Covariance covariance = new Covariance();
            List<String> columns = new ArrayList<>(data.keySet());
            double[][] matrix = new double[columns.size()][columns.size()];
            for (int i = 0; i < columns.size(); i++) {
                double[] arrI = data.get(columns.get(i)).stream().mapToDouble(Double::doubleValue).toArray();
                for (int j = 0; j < columns.size(); j++) {
                    double[] arrJ = data.get(columns.get(j)).stream().mapToDouble(Double::doubleValue).toArray();
                    matrix[i][j] = covariance.covariance(arrI, arrJ, true);
                }
            }
            sheetStats.put("Ковариация", matrix);

            statistics.put(sheetName, sheetStats);
        }
    }
}