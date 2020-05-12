package projecy;

import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AddPatientsTableView extends VBox {
    public AddPatientsTableView(PatientList patientList, MonitoredPatientList monitoredList) {
        final TextField enterIdentiferTextField = new TextField("PractitionerIdentifier");
        Button button = new Button("Find Patients");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                
                try {
                    patientList.addPatients(enterIdentiferTextField.getText());
                    enterIdentiferTextField.setText("Success!");
                } catch (ResourceNotFoundException exception) {
                    enterIdentiferTextField.setText("Error, invalid Identifier");
                    System.out.println(exception);
                } catch (FhirClientConnectionException exception) {
                    enterIdentiferTextField.setText("Error connecting to server");
                }

            }
        });
        HBox addPatients = new HBox(button, enterIdentiferTextField);
        TableView availablePatients = new TableView<CholesterolPatient>(patientList.patients);
        //Add Column for name
        TableColumn<CholesterolPatient, String> nameColumn = new TableColumn<CholesterolPatient, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("name"));
        //Add Column for add button
        TableColumn<CholesterolPatient, Button> addPatientColumn = new TableColumn<>("Monitor Patient");
        addPatientColumn.setCellFactory(ActionButtonTableCell.<CholesterolPatient>forTableColumn("Add", (patient) ->
                {
                    try {
                        monitoredList.addPatient(patient.getID());
                    } catch (FhirClientConnectionException e) {
                        button.setText("Error adding patient");
                    }
                    return patient;
                }
        ));

        availablePatients.getColumns().addAll(nameColumn);
        this.getChildren().addAll(addPatients, availablePatients);

    }
}
