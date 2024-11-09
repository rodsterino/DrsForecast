package org.example.drsforecast;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML with absolute path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/drsforecast/FileSelection.fxml"));

            AnchorPane root = loader.load();

            // Set the primary stage in the controller
            FileSelectionController controller = loader.getController();
            controller.setStage(primaryStage);

            // Set up the scene and show the stage
            Scene scene = new Scene(root, 800, 520);
            primaryStage.setTitle("Drs Forecast Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
