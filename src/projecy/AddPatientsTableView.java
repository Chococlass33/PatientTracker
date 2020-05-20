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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.hl7.fhir.r4.model.Patient;

public class AddPatientsTableView extends Region {
    public AddPatientsTableView(PatientList sourceList, PatientList destinationList) {
        Region practitionerIdentifierSearch = createPractitionerIdentifierSearch(sourceList);
        TableView availablePatients = new TableView<CholesterolPatient>(sourceList.patients);
        //Add Column for name
        TableColumn<CholesterolPatient, String> nameColumn = new TableColumn<CholesterolPatient, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("name"));
        //Add Column for add button
        TableColumn<CholesterolPatient, Button> addPatientColumn = new TableColumn<>("Monitor Patient");
        addPatientColumn.setCellFactory(ActionButtonTableCell.<CholesterolPatient>forTableColumn("Add", (patient) ->
                {
                    try {
                        destinationList.addPatient(patient.getID());
                    } catch (FhirClientConnectionException e) {
                        System.out.println("Error, couldn't add patient" + e);
                    }
                    return patient;
                }
        ));
        availablePatients.getColumns().addAll(nameColumn, addPatientColumn);
        VBox container = new VBox(practitionerIdentifierSearch, availablePatients);
        this.getChildren().addAll(container);

    }
    private Region createPractitionerIdentifierSearch(PatientList sourceList) {
        final TextField enterIdentiferTextField = new TextField("PractitionerIdentifier");
        Button button = new Button("Find Patients");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                try {
                    sourceList.addPatients(enterIdentiferTextField.getText());
                    enterIdentiferTextField.setText("Success!");
                } catch (ResourceNotFoundException exception) {
                    enterIdentiferTextField.setText("Error, invalid Identifier");
                    System.out.println(exception);
                } catch (FhirClientConnectionException exception) {
                    enterIdentiferTextField.setText("Error connecting to server");
                }

            }
        });
        return new HBox(button, enterIdentiferTextField);
    }

}
