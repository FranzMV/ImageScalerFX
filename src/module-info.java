module ImageScalerFX {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    opens imagescalerfx;
    opens imagescalerfx.model;
    opens imagescalerfx.utils;
}