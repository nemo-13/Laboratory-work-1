package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {
    public static Map<String, Map<String, List<Double>>> readExcel(String filePath) throws Exception {
        Map<String, Map<String, List<Double>>> sheetsData = new LinkedHashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (Sheet sheet : workbook) {
                Map<String, List<Double>> data = new LinkedHashMap<>();
                DataFormatter formatter = new DataFormatter();

                Row headerRow = sheet.getRow(0);
                List<String> headers = new ArrayList<>();
                for (Cell cell : headerRow) {
                    headers.add(formatter.formatCellValue(cell));
                }

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        evaluator.evaluateFormulaCell(cell);
                        String value = formatter.formatCellValue(cell, evaluator)
                                .trim()
                                .replace(",", ".")
                                .replace(" ", "");

                        try {
                            double numValue = Double.parseDouble(value);
                            data.computeIfAbsent(headers.get(j), k -> new ArrayList<>()).add(numValue);
                        } catch (NumberFormatException ex) {
                            throw new Exception(
                                "Ошибка в листе '" + sheet.getSheetName() + 
                                "', строка " + (i+1) + 
                                ", столбец '" + headers.get(j) + "': '" + value + "'"
                            );
                        }
                    }
                }
                sheetsData.put(sheet.getSheetName(), data);
            }
        }
        return sheetsData;
    }
}