module org.example.drsforecast {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires junit;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;

    opens org.example.drsforecast to javafx.fxml;
    exports org.example.drsforecast;
}