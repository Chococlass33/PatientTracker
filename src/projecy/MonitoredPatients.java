package projecy;

import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;

public class MonitoredPatients {
    private ArrayList<Patient> patients;
    private Requests requests;
    private int updateFrequency;
    public MonitoredPatients(Requests requests) {
        this.requests = requests;
        this.patients = new ArrayList();
    }
    public void removePatient(Patient patient) {
        this.patients.remove(patient);
    }
    public void addPatient(String patientID) {
        Patient patient = requests.getPatient(patientID);
        this.patients.add(patient);
    }
    public void setUpdateFrequency(int timeBetweenUpdates) {
        this.updateFrequency = timeBetweenUpdates;
    }

}
