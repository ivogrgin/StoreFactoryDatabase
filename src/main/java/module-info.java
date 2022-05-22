module hr.java.production.grgin9 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    opens hr.java.production.controller to javafx.fxml;
    exports hr.java.production.controller;
    exports hr.java.production.model;
    opens hr.java.production.model to javafx.fxml;
}