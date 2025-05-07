package org.mephi.b23902.ogorodnikov.lw1.controller;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelDataReader {
    public Map<String, List<List<Double>>> readAllSheets(File file) throws IOException {
        Map<String, List<List<Double>>> result = new LinkedHashMap<>();
        Workbook workbook = WorkbookFactory.create(file, null, true);

        for (Sheet sheet : workbook) {
            List<List<Double>> samples = new ArrayList<>();
            int cols = sheet.getRow(0).getLastCellNum();

            for (int i = 0; i < cols; i++) samples.add(new ArrayList<>());

            for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
                Row row = sheet.getRow(rowIdx);
                if (row == null) continue;

                for (int colIdx = 0; colIdx < cols; colIdx++) {
                    Cell cell = row.getCell(colIdx, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    double value;

                    if (cell.getCellType() == CellType.FORMULA) {
                        value = cell.getNumericCellValue();
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        value = cell.getNumericCellValue();
                    } else {
                        value = 0.0;
                    }

                    samples.get(colIdx).add(value);
                }
            }
            result.put(sheet.getSheetName(), samples);
        }
        workbook.close();
        return result;
    }
}