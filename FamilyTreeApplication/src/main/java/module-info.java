module org.openjfx.familytreeapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens org.openjfx.familytreeapplication to javafx.fxml;
    exports org.openjfx.familytreeapplication;
}