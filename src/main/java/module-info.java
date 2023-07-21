module com.example.puzzle_v1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.puzzle_v1 to javafx.fxml;
    exports com.example.puzzle_v1;
}