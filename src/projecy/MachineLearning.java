package projecy;

import org.hl7.fhir.r4.model.Bundle;

import java.util.Hashtable;

public class MachineLearning
{
    Requests request;
    Hashtable<Integer, mekaPatientData> patientDictionary = new Hashtable<>();

    public MachineLearning(Requests request)
    {
        this.request = request;
    }


    public void grabData()
    {
        Bundle patientbundle = requests.getAllOfPatients();
    }
}
