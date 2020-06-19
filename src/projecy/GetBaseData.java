package projecy;

import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Bundle;

import java.util.List;

public interface GetBaseData extends GetPatients {
    public List<Bundle.BundleEntryComponent> getPatientResourceBase(String patientID, DataTypes dataTypes);
}
