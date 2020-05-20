package projecy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;


public class PatientList {

    public ObservableList<CholesterolPatient> patients;
    private GetPatients patientGetter;
    public PatientList(GetPatients patientGetter) {
        this.patientGetter = patientGetter;
        this.patients = FXCollections.observableArrayList(new ArrayList());

    }
    public void addPatient(CholesterolPatient patient) {
        this.patients.add(patient);
    }
    public void addPatients(String practitionerIdentifier) {
            patients.addAll(patientGetter.getPatientsForPractitioner(practitionerIdentifier));
    }
    public void removePatient(CholesterolPatient patient) {
        this.patients.remove(patient);
    }
}
