package projecy;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PatientTableView extends VBox{
    private Button button;
    private TableController tableBox;


    public PatientTableView(final MonitoredPatients patients) {
        final TextField enterIDTextField = new TextField("PatientID...");
        this.button = new Button("Add new Patient");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                patients.addPatient(enterIDTextField.getText());
            }
        });
        HBox addPatient = new HBox(this.button, enterIDTextField);
        this.tableBox = new TableController(patients);
        this.getChildren().addAll(addPatient, this.tableBox);
    }

}
