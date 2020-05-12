package projecy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;


public class PatientList {

    public ObservableList<CholesterolPatient> patients;
    private Requests requests;
    public PatientList(Requests requests) {
        this.requests = requests;
        this.patients = FXCollections.observableArrayList(new ArrayList());

    }
    public void addPatients(String practitionerIdentifier) {
            patients.addAll(requests.getPatientsForPractitioner(practitionerIdentifier));
    }
}
