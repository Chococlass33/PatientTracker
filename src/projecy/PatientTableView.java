package projecy;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PatientTableView extends VBox{
    private Button button;
    private TableController tableBox;


    public PatientTableView(final MonitoredPatients patients) {
        this.button = new Button("Add new Patient");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //TODO: Placeholder for adding patient with selectable ID
                patients.addPatient("1");

            }
        });
        this.tableBox = new TableController(patients);
        this.getChildren().addAll(this.button, this.tableBox);
    }

}
