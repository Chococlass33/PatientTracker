package projecy;

import java.util.ArrayList;

public interface GetPatients {
    public ArrayList<DataPatient> getPatientsForPractitioner(String practitionerIdentifier);
    public DataPatient getPatient(String patientID);
}
