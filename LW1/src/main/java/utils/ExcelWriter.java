package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.util.Map;

public class ExcelWriter {
    public static void writeExcel(String filePath, Map<String, Map<String, Object>> statistics) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            for (String sheetName : statistics.keySet()) {
                Sheet sheet = workbook.createSheet(sheetName);
                Map<String, Object> sheetStats = statistics.get(sheetName);
                int rowIdx = 0;

                Map<String, Map<String, Double>> columnsStats = (Map<String, Map<String, Double>>) sheetStats.get("Столбцы");
                for (String column : columnsStats.keySet()) {
                    Row headerRow = sheet.createRow(rowIdx++);
                    headerRow.createCell(0).setCellValue(column + ":");

                    Map<String, Double> stats = columnsStats.get(column);
                    for (Map.Entry<String, Double> entry : stats.entrySet()) {
                        Row row = sheet.createRow(rowIdx++);
                        row.createCell(0).setCellValue(entry.getKey());
                        if (entry.getKey().equals("Доверительный интервал (нижняя)")) {
                            String ci = String.format("[%.4f, %.4f]", 
                                entry.getValue(), 
                                stats.get("Доверительный интервал (верхняя)"));
                            row.createCell(1).setCellValue(ci);
                        } else {
                            row.createCell(1).setCellValue(entry.getValue());
                        }
                    }
                    rowIdx++;
                }

                double[][] matrix = (double[][]) sheetStats.get("Ковариация");
                Row covHeader = sheet.createRow(rowIdx++);
                covHeader.createCell(0).setCellValue("Матрица ковариации (" + matrix.length + "x" + matrix[0].length + "):");
                
                for (int i = 0; i < matrix.length; i++) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue("X" + (i+1) + ":");
                    for (int j = 0; j < matrix[i].length; j++) {
                        row.createCell(j+1).setCellValue(matrix[i][j]);
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }
}