package projecy;

import java.util.ArrayList;

public interface GetPatients {
    public ArrayList<CholesterolPatient> getPatientsForPractitioner(String practitionerIdentifier);
    public CholesterolPatient getPatient(String patientID);
}
