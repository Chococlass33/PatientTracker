package projecy;

import org.hl7.fhir.r4.model.Base;

public interface GetPatientsCholesterol extends GetPatients {
    public Base getPatientCholesterol(String patientID);
}
