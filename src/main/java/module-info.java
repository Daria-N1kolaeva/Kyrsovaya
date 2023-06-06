module com.example.graphic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens com.example.kurs to javafx.fxml;
    exports com.example.kurs;
}