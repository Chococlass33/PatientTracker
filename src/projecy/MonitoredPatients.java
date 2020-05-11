package projecy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hl7.fhir.r4.model.Base;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitoredPatients {
    public ObservableList<CholesterolPatient> patients;
    private Requests requests;
    private ScheduledExecutorService updateCholesterolService;
    private Runnable updateCholesterol;
    public MonitoredPatients(Requests requests) {
        this.requests = requests;
        this.patients = FXCollections.observableArrayList(new ArrayList());
        this.updateCholesterol = new Runnable() {
            public void run() {
                for (CholesterolPatient patient : patients){
                    updateCholesterol(patient);
                    System.out.println("Updating Cholesterol");
                }
            }
        };
        this.updateCholesterolService = Executors.newScheduledThreadPool(1);
        this.updateCholesterolService.scheduleAtFixedRate(updateCholesterol, 0, 60, TimeUnit.SECONDS);
    }
    public void removePatient(CholesterolPatient patient) {
        this.patients.remove(patient);
    }
    public void addPatient(String patientID) {
        CholesterolPatient patient = requests.getPatient(patientID);
        this.patients.add(patient);
    }
    private void updateCholesterol(CholesterolPatient patient) {
        Base cholesterolLevel = requests.getPatientCholesterol(patient.getID());
        patient.updateCholesterolAndTime(cholesterolLevel);
    }
    public void setUpdateFrequency(int timeBetweenUpdates) {
        this.updateCholesterolService.scheduleWithFixedDelay(updateCholesterol, 0, timeBetweenUpdates, TimeUnit.SECONDS);
    }
    private float averageCholestorol() {
         float total = 0;
         int patientnum = 0;
        for (CholesterolPatient patient : patients)
        {
            //total += patient.getCholesterol();
            total += 1;
            patientnum += 1;
        }
    return total/patientnum;
    }

    public boolean isBelowAverage(CholesterolPatient patient)
    {
        //if (patient.getCholesterol() <= averageCholestorol())
        {
         //   return true;
        }
        return false;
    }

}
