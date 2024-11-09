package org.example.drsforecast;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;

public class ConfigController {

    @FXML
    private TextField openTxtField;

    @FXML
    private TextField closeTxtField;

    private Stage primaryStage;
    private File selectedFile;

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setSelectedFile(File file) {
        this.selectedFile = file;
        System.out.println("File received in ConfigController: " + file.getAbsolutePath());
    }

    @FXML
    private void handleNext(ActionEvent event) {
        String openTime = openTxtField.getText();
        String closeTime = closeTxtField.getText();

        if (validateTime(openTime) && validateTime(closeTime)) {
            System.out.println("Open Time: " + openTime + ", Close Time: " + closeTime);
            goToDetailsScreen(openTime, closeTime);
        } else {
            System.out.println("Invalid time format. Please enter times in HH:MM format.");
        }
    }

    private boolean validateTime(String time) {
        return time.matches("\\d{2}:\\d{2}");
    }

    private void goToDetailsScreen(String openTime, String closeTime) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/drsforecast/Details.fxml"));
            Scene detailsScene = new Scene(loader.load());

            DetailsController detailsController = loader.getController();
            detailsController.setFileAndTimes(selectedFile, openTime, closeTime);

            primaryStage.setScene(detailsScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

