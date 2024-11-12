package org.example.drsforecast;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailsController {

    @FXML
    private TableView<RowData> totalOrderTable;

    @FXML
    private TableView<RowData> delvOrderTable;

    @FXML
    private TableColumn<RowData, String> hourColumn1;
    @FXML
    private TableColumn<RowData, Integer> monColumn1, tueColumn1, wedColumn1, thuColumn1, friColumn1, satColumn1, sunColumn1;

    @FXML
    private TableColumn<RowData, String> hourColumn2;
    @FXML
    private TableColumn<RowData, Integer> monColumn2, tueColumn2, wedColumn2, thuColumn2, friColumn2, satColumn2, sunColumn2;

    @FXML
    private Text storeTxt;

    @FXML
    private Text dateTxt;

    private File selectedFile;
    private String openTime;
    private String closeTime;

    public void setFileAndTimes(File file, String openTime, String closeTime) {
        this.selectedFile = file;
        this.openTime = openTime;
        this.closeTime = closeTime;
        processAndDisplayData();
    }

    private void processAndDisplayData() {
        try {
            Map<String, Map<String, Integer>> aggregatedData = ExcelProcessor.loadAggregatedData(selectedFile, openTime, closeTime);

            // Debug: Check aggregated data
            for (String column : aggregatedData.keySet()) {
                System.out.println("Column: " + column + " contains " + aggregatedData.get(column).size() + " entries.");
            }

            List<RowData> totalOrderList = prepareData(
                    aggregatedData.get("mon - total order count"),
                    aggregatedData.get("tue - total order count"),
                    aggregatedData.get("wed - total order count"),
                    aggregatedData.get("thu - total order count"),
                    aggregatedData.get("fri - total order count"),
                    aggregatedData.get("sat - total order count"),
                    aggregatedData.get("sun - total order count")
            );

            List<RowData> delvOrderList = prepareData(
                    aggregatedData.get("mon - delv order count"),
                    aggregatedData.get("tue - delv order count"),
                    aggregatedData.get("wed - delv order count"),
                    aggregatedData.get("thu - delv order count"),
                    aggregatedData.get("fri - delv order count"),
                    aggregatedData.get("sat - delv order count"),
                    aggregatedData.get("sun - delv order count")
            );

            hourColumn1.setCellValueFactory(new PropertyValueFactory<>("hour"));
            monColumn1.setCellValueFactory(new PropertyValueFactory<>("monCount"));
            tueColumn1.setCellValueFactory(new PropertyValueFactory<>("tueCount"));
            wedColumn1.setCellValueFactory(new PropertyValueFactory<>("wedCount"));
            thuColumn1.setCellValueFactory(new PropertyValueFactory<>("thuCount"));
            friColumn1.setCellValueFactory(new PropertyValueFactory<>("friCount"));
            satColumn1.setCellValueFactory(new PropertyValueFactory<>("satCount"));
            sunColumn1.setCellValueFactory(new PropertyValueFactory<>("sunCount"));

            hourColumn2.setCellValueFactory(new PropertyValueFactory<>("hour"));
            monColumn2.setCellValueFactory(new PropertyValueFactory<>("monCount"));
            tueColumn2.setCellValueFactory(new PropertyValueFactory<>("tueCount"));
            wedColumn2.setCellValueFactory(new PropertyValueFactory<>("wedCount"));
            thuColumn2.setCellValueFactory(new PropertyValueFactory<>("thuCount"));
            friColumn2.setCellValueFactory(new PropertyValueFactory<>("friCount"));
            satColumn2.setCellValueFactory(new PropertyValueFactory<>("satCount"));
            sunColumn2.setCellValueFactory(new PropertyValueFactory<>("sunCount"));

            totalOrderTable.getItems().setAll(totalOrderList);
            delvOrderTable.getItems().setAll(delvOrderList);

            // Load the Excel file to get store and week begin date
            try (FileInputStream fis = new FileInputStream(selectedFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                Row headerRow = sheet.getRow(0);

                int storeColumnIndex = -1;
                int weekBeginDateColumnIndex = -1;

                // Find the column indices for "Store" and "Week Begin Date"
                for (Cell cell : headerRow) {
                    if (cell.getStringCellValue().equalsIgnoreCase("Store")) {
                        storeColumnIndex = cell.getColumnIndex();
                    } else if (cell.getStringCellValue().equalsIgnoreCase("Week Begin Date")) {
                        weekBeginDateColumnIndex = cell.getColumnIndex();
                    }
                }

                if (storeColumnIndex == -1 || weekBeginDateColumnIndex == -1) {
                    throw new IllegalArgumentException("Excel file does not contain required columns");
                }

                // Read the first row to get the store and week begin date
                Row firstDataRow = sheet.getRow(1);
                String store = getCellValueAsString(firstDataRow.getCell(storeColumnIndex));
                LocalDate weekBeginDate = firstDataRow.getCell(weekBeginDateColumnIndex).getLocalDateTimeCellValue().toLocalDate();
                LocalDate weekEndDate = weekBeginDate.plus(6, ChronoUnit.DAYS);

                // Update the text fields
                storeTxt.setText(store);
                dateTxt.setText(weekBeginDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + " - " + weekEndDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private List<RowData> prepareData(Map<String, Integer>... dayData) {
        List<RowData> rowDataList = new ArrayList<>();
        if (dayData[0] == null) {
            System.out.println("No data for the first day (Monday).");
            return rowDataList;
        }

        // Process hourly data
        for (String hour : dayData[0].keySet()) {
            rowDataList.add(new RowData(
                    hour,
                    dayData[0].getOrDefault(hour, 0),
                    dayData[1].getOrDefault(hour, 0),
                    dayData[2].getOrDefault(hour, 0),
                    dayData[3].getOrDefault(hour, 0),
                    dayData[4].getOrDefault(hour, 0),
                    dayData[5].getOrDefault(hour, 0),
                    dayData[6].getOrDefault(hour, 0)
            ));
        }

        // Sort hours, moving times after midnight (e.g., 00:00, 01:00, 02:00) to the bottom
        rowDataList.sort((r1, r2) -> {
            String h1 = r1.getHour();
            String h2 = r2.getHour();

            boolean afterMidnight1 = isAfterMidnight(h1);
            boolean afterMidnight2 = isAfterMidnight(h2);

            if (afterMidnight1 && !afterMidnight2) return 1;
            if (!afterMidnight1 && afterMidnight2) return -1;
            return h1.compareTo(h2);
        });

        // Add totals row at the bottom
        rowDataList.add(computeTotalsRow(dayData));

        System.out.println("Prepared " + rowDataList.size() + " rows for TableView.");
        return rowDataList;
    }

    private boolean isAfterMidnight(String hour) {
        // Treat "00:00" to "03:00" as after midnight
        return hour.compareTo("00:00") >= 0 && hour.compareTo("03:59") <= 0;
    }

    private RowData computeTotalsRow(Map<String, Integer>... dayData) {
        int monTotal = 0, tueTotal = 0, wedTotal = 0, thuTotal = 0, friTotal = 0, satTotal = 0, sunTotal = 0;

        for (Map<String, Integer> dayMap : dayData) {
            if (dayMap == null) continue;

            for (int value : dayMap.values()) {
                if (dayMap == dayData[0]) monTotal += value;
                if (dayMap == dayData[1]) tueTotal += value;
                if (dayMap == dayData[2]) wedTotal += value;
                if (dayMap == dayData[3]) thuTotal += value;
                if (dayMap == dayData[4]) friTotal += value;
                if (dayMap == dayData[5]) satTotal += value;
                if (dayMap == dayData[6]) sunTotal += value;
            }
        }

        return new RowData("Totals", monTotal, tueTotal, wedTotal, thuTotal, friTotal, satTotal, sunTotal);
    }
}
