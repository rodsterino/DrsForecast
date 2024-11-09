package org.example.drsforecast;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.File;

public class DetailsController {

    @FXML
    private Label fileNameLabel;
    @FXML
    private Label businessHoursLabel;

    // Set file and business hours
    public void setFileAndTimes(File file, String amTime, String pmTime) {
        fileNameLabel.setText("File: " + file.getName());
        businessHoursLabel.setText("Business Hours: " + amTime + " to " + pmTime);

        // You can add logic to process the file and display details here
        System.out.println("File: " + file.getAbsolutePath());
        System.out.println("Business Hours: " + amTime + " to " + pmTime);
    }
}

