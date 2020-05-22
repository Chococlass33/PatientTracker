package org.cholapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class main extends Application{
    private TableView table = new TableView();
    @Override
    public void start(Stage stage) throws Exception {
        Button addPatientButton = new Button("Add new Patient");
        stage.setTitle("Hello");
        TableColumn nameColumn = new TableColumn("NAME");
        TableColumn cholesterolColumn = new TableColumn("TOTAL CHOLESTEROL");
        TableColumn timeColumn = new TableColumn("TIME");
        table.getColumns().addAll(nameColumn, cholesterolColumn, timeColumn);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(addPatientButton, table);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }
}
