package projecy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hl7.fhir.r4.model.Bundle;
import java.util.ArrayList;


public class PatientList {

    public ObservableList<CholesterolPatient> patients;
    private Requests requests;
    public PatientList(Requests requests) {
        this.requests = requests;
        this.patients = FXCollections.observableArrayList(new ArrayList());

    }
    public void addPatients(String practitionerIdentifier) {
        ArrayList<Bundle.BundleEntryComponent> encounters = requests.getPatientsForPractitioner(practitionerIdentifier);
        for (int i = 0; i < encounters.size(); i++) {
            String name = encounters.get(i).getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("display").getValues().get(0).toString();
            String id = encounters.get(i).getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            id = id.replace("Patient/", "");
            CholesterolPatient newPatient = new CholesterolPatient(name, id);
            if (!patients.contains(newPatient)){
                patients.add(new CholesterolPatient(name, id));
            }

        }
    }

}
