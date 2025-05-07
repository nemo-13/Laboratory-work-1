package org.mephi.b23902.ogorodnikov.lw1.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.io.File;

public class ExcelDataWriter {
    public void writeStatistics(File file, Map<String, List<String>> stats) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        for (Map.Entry<String, List<String>> entry : stats.entrySet()) {
            Sheet sheet = workbook.createSheet(entry.getKey());
            List<String> statsList = entry.getValue();

            for (int i = 0; i < statsList.size(); i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(statsList.get(i));
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}