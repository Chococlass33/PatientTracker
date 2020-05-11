package projecy;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
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
                try {
                    patients.addPatient(enterIDTextField.getText());
                    enterIDTextField.setText("Success!");
                } catch (ResourceNotFoundException exception) {
                    enterIDTextField.setText("Error, invalid ID");
                    System.out.println(exception);
                }
            }
        });
        HBox addPatient = new HBox(this.button, enterIDTextField);
        this.tableBox = new TableController(patients);

        final TextField setUpdateFrequencyField = new TextField("(Seconds, as int)");
        Button setUpdateFrequencyButton = new Button("Set Update Frequency");
        setUpdateFrequencyButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    patients.setUpdateFrequency(Integer.parseInt(setUpdateFrequencyField.getText()));
                    setUpdateFrequencyField.setText("Success!");
                } catch (NumberFormatException exception) {
                    setUpdateFrequencyField.setText("Error, enter valid int");
                }
            }
        });

        HBox setUpdateFrequency = new HBox(setUpdateFrequencyButton, setUpdateFrequencyField);
        this.getChildren().addAll(addPatient, this.tableBox, setUpdateFrequency);
    }

}
