package org.example.drsforecast;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class FileSelectionController {

    private Stage primaryStage; // Reference to the primary stage
    private File selectedFile;

    // Method to set the primary stage
    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void handleSelectFile(ActionEvent event) {
        // Open a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
        );

        // Show the file chooser dialog
        selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
            goToConfigScreen(); // Transition to Config.fxml
        } else {
            System.out.println("No file selected.");
        }
    }

    private void goToConfigScreen() {
        try {
            // Load Config.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/drsforecast/Config.fxml"));
            Scene configScene = new Scene(loader.load());

            // Pass the selected file to ConfigController
            ConfigController configController = loader.getController();
            configController.setSelectedFile(selectedFile);

            // Pass the stage to ConfigController
            configController.setStage(primaryStage);

            // Set the scene to Config.fxml
            primaryStage.setScene(configScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

