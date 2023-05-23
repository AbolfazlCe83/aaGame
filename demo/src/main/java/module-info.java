module view {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.google.gson;


    exports view;
    exports controller;
    exports model;
    opens view to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to com.google.gson;
}