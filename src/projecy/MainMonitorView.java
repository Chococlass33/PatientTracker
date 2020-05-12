package projecy;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainMonitorView extends VBox{

    private MonitorPatientsTableView tableBox;


    public MainMonitorView(final MonitoredPatients patients) {

        this.tableBox = new MonitorPatientsTableView(patients);

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
        this.getChildren().addAll(this.tableBox, setUpdateFrequency);
    }

}
