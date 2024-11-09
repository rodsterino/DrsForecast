module org.example.drsforecast {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;


    opens org.example.drsforecast to javafx.fxml;
    exports org.example.drsforecast;
}