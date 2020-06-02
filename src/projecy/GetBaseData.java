package projecy;

import org.hl7.fhir.r4.model.Base;

public interface GetBaseData extends GetPatients {
    public Base getPatientResourceBase(String patientID, DataTypes dataTypes);
}
