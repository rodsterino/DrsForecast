package org.example.drsforecast;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelProcessor {

    public static Map<String, Map<String, Integer>> loadAggregatedData(File file, String openTime, String closeTime) throws Exception {
        Map<String, Map<String, Integer>> aggregatedData = new TreeMap<>();
        Map<String, Integer> columnIndexMap = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assumes data is in the first sheet

            // Map column names to their indices
            Row headerRow = sheet.getRow(0);
            for (Cell cell : headerRow) {
                String columnName = normalizeColumnName(cell.getStringCellValue());
                columnIndexMap.put(columnName, cell.getColumnIndex());
            }

            // Debug: Print detected column names
            System.out.println("Detected Columns: " + columnIndexMap.keySet());

            // Define normalized expected columns
            String[] expectedColumns = {
                    "mon - total order count", "tue - total order count", "wed - total order count",
                    "thu - total order count", "fri - total order count", "sat - total order count",
                    "sun - total order count", "mon - delv order count", "tue - delv order count",
                    "wed - delv order count", "thu - delv order count", "fri - delv order count",
                    "sat - delv order count", "sun - delv order count"
            };

            // Initialize data maps for expected columns
            for (String col : expectedColumns) {
                if (!columnIndexMap.containsKey(col)) {
                    System.out.println("Missing column in Excel: " + col);
                } else {
                    aggregatedData.put(col, new TreeMap<>());
                }
            }

            // Convert open and close times into comparable values
            int openTimeInt = convertTimeToInt(openTime);
            int closeTimeInt = convertTimeToInt(closeTime);

            // Process each data row
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                Cell timeCell = row.getCell(2); // Assuming column C (index 2) contains time
                if (timeCell == null || timeCell.getCellType() != CellType.STRING) {
                    System.out.println("Skipping row " + row.getRowNum() + ": Invalid or missing time cell.");
                    continue;
                }

                String time = timeCell.getStringCellValue();
                if (!isValidTimeFormat(time)) {
                    System.out.println("Skipping row " + row.getRowNum() + ": Invalid time format: " + time);
                    continue;
                }

                int timeInt = convertTimeToInt(time);

                // Adjust comparison logic for time ranges spanning midnight
                boolean isWithinTimeRange = (openTimeInt <= closeTimeInt && timeInt >= openTimeInt && timeInt <= closeTimeInt) ||
                        (openTimeInt > closeTimeInt && (timeInt >= openTimeInt || timeInt <= closeTimeInt));

                if (!isWithinTimeRange) {
                    System.out.println("Skipping row " + row.getRowNum() + ": Time " + time + " is outside operating hours.");
                    continue;
                }

                String hour = getHourFromTime(time); // Convert to "HH:00"
                System.out.println("Processing row " + row.getRowNum() + " for hour: " + hour);

                for (String col : expectedColumns) {
                    Integer colIndex = columnIndexMap.get(col);
                    if (colIndex != null) {
                        Cell dataCell = row.getCell(colIndex);
                        if (dataCell != null) {
                            if (dataCell.getCellType() == CellType.NUMERIC) {
                                int value = (int) dataCell.getNumericCellValue();
                                aggregatedData.get(col).put(hour,
                                        aggregatedData.get(col).getOrDefault(hour, 0) + value);
                                System.out.println("Column: " + col + ", Hour: " + hour + ", Value: " + value);
                            } else {
                                System.out.println("Skipping column " + col + " for row " + row.getRowNum() +
                                        ": Cell is not numeric.");
                            }
                        } else {
                            System.out.println("Column: " + col + ", Row: " + row.getRowNum() + " is null.");
                        }
                    }
                }
            }
        }

        return aggregatedData;
    }

    // Helper method to validate time format
    private static boolean isValidTimeFormat(String time) {
        return time.matches("^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$"); // Matches "HH:mm" or "HH:mm:ss"
    }

    // Helper method to group times into 1-hour intervals (e.g., "10:15" -> "10:00")
    private static String getHourFromTime(String time) {
        // Extract hour part and format it as "HH:00"
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]); // Extract the hour
        return String.format("%02d:00", hour); // Return hour in "HH:00" format
    }

    // Helper method to convert "HH:mm:ss" to an integer (e.g., "10:00:00" -> 1000, "01:00:00" -> 2500 for times after midnight)
    private static int convertTimeToInt(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        if (hour == 0 && time.startsWith("00")) hour = 24; // Adjust for "00:00" -> 2400
        return hour * 100 + minute;
    }

    // Normalize column names for comparison
    private static String normalizeColumnName(String columnName) {
        return columnName.trim().toLowerCase().replaceAll("\\s+", " ");
    }
}


