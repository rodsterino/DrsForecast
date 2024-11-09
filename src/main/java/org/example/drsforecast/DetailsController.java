package org.example.drsforecast;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailsController {

    @FXML
    private TableView<RowData> totalOrderTable; // Table for Total Orders

    @FXML
    private TableView<RowData> delvOrderTable; // Table for Delivered Orders

    @FXML
    private TableColumn<RowData, String> hourColumn1; // Hour column for Total Orders
    @FXML
    private TableColumn<RowData, Integer> monColumn1, tueColumn1, wedColumn1, thuColumn1, friColumn1, satColumn1, sunColumn1;

    @FXML
    private TableColumn<RowData, String> hourColumn2; // Hour column for Delivered Orders
    @FXML
    private TableColumn<RowData, Integer> monColumn2, tueColumn2, wedColumn2, thuColumn2, friColumn2, satColumn2, sunColumn2;

    private File selectedFile; // Reference to the selected file
    private String openTime; // Opening time
    private String closeTime; // Closing time

    // Set the file and time range from ConfigController
    public void setFileAndTimes(File file, String openTime, String closeTime) {
        this.selectedFile = file;
        this.openTime = openTime;
        this.closeTime = closeTime;

        // Process and display data immediately
        processAndDisplayData();
    }

    private void processAndDisplayData() {
        try {
            // Load and process the Excel data
            Map<String, Map<String, Integer>> aggregatedData = ExcelProcessor.loadAggregatedData(selectedFile);

            // Prepare Total Orders table
            List<RowData> totalOrderList = prepareData(
                    aggregatedData.get("Mon - Total Order Count"),
                    aggregatedData.get("Tue - Total Order Count"),
                    aggregatedData.get("Wed - Total Order Count"),
                    aggregatedData.get("Thu - Total Order Count"),
                    aggregatedData.get("Fri - Total Order Count"),
                    aggregatedData.get("Sat - Total Order Count"),
                    aggregatedData.get("Sun - Total Order Count")
            );

            // Prepare Delivered Orders table
            List<RowData> delvOrderList = prepareData(
                    aggregatedData.get("Mon - Delv Order Count"),
                    aggregatedData.get("Tue - Delv Order Count"),
                    aggregatedData.get("Wed - Delv Order Count"),
                    aggregatedData.get("Thu - Delv Order Count"),
                    aggregatedData.get("Fri - Delv Order Count"),
                    aggregatedData.get("Sat - Delv Order Count"),
                    aggregatedData.get("Sun - Delv Order Count")
            );

            // Bind columns for Total Orders table
            hourColumn1.setCellValueFactory(new PropertyValueFactory<>("hour"));
            monColumn1.setCellValueFactory(new PropertyValueFactory<>("monCount"));
            tueColumn1.setCellValueFactory(new PropertyValueFactory<>("tueCount"));
            wedColumn1.setCellValueFactory(new PropertyValueFactory<>("wedCount"));
            thuColumn1.setCellValueFactory(new PropertyValueFactory<>("thuCount"));
            friColumn1.setCellValueFactory(new PropertyValueFactory<>("friCount"));
            satColumn1.setCellValueFactory(new PropertyValueFactory<>("satCount"));
            sunColumn1.setCellValueFactory(new PropertyValueFactory<>("sunCount"));

            // Bind columns for Delivered Orders table
            hourColumn2.setCellValueFactory(new PropertyValueFactory<>("hour"));
            monColumn2.setCellValueFactory(new PropertyValueFactory<>("monCount"));
            tueColumn2.setCellValueFactory(new PropertyValueFactory<>("tueCount"));
            wedColumn2.setCellValueFactory(new PropertyValueFactory<>("wedCount"));
            thuColumn2.setCellValueFactory(new PropertyValueFactory<>("thuCount"));
            friColumn2.setCellValueFactory(new PropertyValueFactory<>("friCount"));
            satColumn2.setCellValueFactory(new PropertyValueFactory<>("satCount"));
            sunColumn2.setCellValueFactory(new PropertyValueFactory<>("sunCount"));

            // Populate the tables
            totalOrderTable.getItems().setAll(totalOrderList);
            delvOrderTable.getItems().setAll(delvOrderList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Prepare data for TableView from aggregated data
    private List<RowData> prepareData(Map<String, Integer>... dayData) {
        List<RowData> rowDataList = new ArrayList<>();
        if (dayData[0] == null) {
            System.out.println("No data found in provided Excel file.");
            return rowDataList; // Return empty list if no data exists
        }

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
        return rowDataList;
    }
}


