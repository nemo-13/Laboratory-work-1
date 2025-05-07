package org.mephi.b23902.ogorodnikov.lw1.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataModel {
    private Map<String, List<List<Double>>> sheetData = new LinkedHashMap<>();

    public Map<String, List<List<Double>>> getSheetData() {
        return sheetData;
    }

    public void addSheetData(String sheetName, List<List<Double>> samples) {
        this.sheetData.put(sheetName, samples);
    }
}
