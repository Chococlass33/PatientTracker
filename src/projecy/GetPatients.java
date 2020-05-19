package projecy;

import org.hl7.fhir.r4.model.Base;

import java.util.ArrayList;

public interface GetPatients {
    public ArrayList<CholesterolPatient> getPatientsForPractitioner(String practitionerIdentifier);
    public CholesterolPatient getPatient(String patientID);
    public Base getPatientCholesterol(String patientID);
}
