module app {
    requires Lib;

    requries javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    exports app.msv;

    opens app.msv to javafx.graphics;
    opens app.msv to javafx.base;
    opens app.msv to javafx.fxml;
}
