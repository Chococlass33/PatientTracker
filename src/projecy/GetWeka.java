package projecy;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

public interface GetWeka
{
    public List<List<Bundle>> getAllOfObservation(int BundleCount);
    public Patient getPatientWeka(String url);
}
