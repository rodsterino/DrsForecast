package org.example.drsforecast;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelProcessor {

    public static Map<String, Map<String, Integer>> loadAggregatedData(File file) throws Exception {
        Map<String, Map<String, Integer>> aggregatedData = new TreeMap<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assumes data is in the first sheet

            for (Row row : sheet) {
                Cell timeCell = row.getCell(1); // Assuming column B has the time (index 1)
                if (timeCell == null || timeCell.getCellType() != CellType.STRING) continue;

                String time = timeCell.getStringCellValue();
                String hour = time.substring(0, 2) + ":00"; // Convert to hourly format

                // Aggregate data for Total Orders and Delivered Orders
                for (int col = 2; col <= 15; col++) { // Assuming Total/Delivered orders are in these columns
                    Cell cell = row.getCell(col);
                    if (cell == null || cell.getCellType() != CellType.NUMERIC) continue;

                    String columnName = sheet.getRow(0).getCell(col).getStringCellValue(); // Column header
                    int value = (int) cell.getNumericCellValue();

                    aggregatedData.putIfAbsent(columnName, new TreeMap<>());
                    aggregatedData.get(columnName).put(hour,
                            aggregatedData.get(columnName).getOrDefault(hour, 0) + value);
                }
            }
        }
        return aggregatedData;
    }
}

