package projecy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;


public class PatientList {

    public ObservableList<DataPatient> patients;
    private GetPatients patientGetter;
    public PatientList(GetPatients patientGetter) {
        this.patientGetter = patientGetter;
        this.patients = FXCollections.observableArrayList(new ArrayList());

    }
    public void addPatient(DataPatient patient) {
        /**
         * Add Patient to self.patients
         * @param patient: The patient to add
         */
        if (!patients.contains(patient)) {
            this.patients.add(patient);
        }
    }
    public void addPatients(String practitionerIdentifier) {
        /**
         * add all patients of a practitioner
         * @param practitionerIdentifier: practitioner's identifier of who's patients to add
         */
         patients.addAll(patientGetter.getPatientsForPractitioner(practitionerIdentifier));
    }
    public void removePatient(DataPatient patient) {
        this.patients.remove(patient);
    }
    public void clearAllPatients() {
        this.patients.clear();
    }
}
