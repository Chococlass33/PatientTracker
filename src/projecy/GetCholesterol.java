package projecy;

import org.hl7.fhir.r4.model.Base;

public interface GetCholesterol extends GetPatients {
    public Base getCholesterol(String patientID);
}
