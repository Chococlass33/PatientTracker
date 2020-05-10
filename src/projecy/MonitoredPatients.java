package projecy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MonitoredPatients {
    public ObservableList<CholesterolPatient> patients;
    private Requests requests;
    private int updateFrequency;
    public MonitoredPatients(Requests requests) {
        this.requests = requests;
        this.patients = FXCollections.observableArrayList(new ArrayList());
    }
    public void removePatient(Patient patient) {
        this.patients.remove(patient);
    }
    public void addPatient(String patientID) {
        CholesterolPatient patient = requests.getPatient(patientID);
        this.patients.add(patient);
    }
    public void setUpdateFrequency(int timeBetweenUpdates) {
        this.updateFrequency = timeBetweenUpdates;
    }

}
