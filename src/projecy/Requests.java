package projecy;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;

public class Requests {
    private IGenericClient client;
    private String cholesterolCode = "2093-3";
    public Requests(String baseURL) {
        FhirContext ctx = FhirContext.forR4();
        this.client = ctx.newRestfulGenericClient(baseURL);
    }
    public CholesterolPatient getPatient(String patientID){
        Patient patient = client.read().resource(Patient.class).withId(patientID).execute();
        CholesterolPatient cholesterolPatient = new CholesterolPatient(patient, getPatientCholesterol(patient));
        return cholesterolPatient;
    }
    public BigDecimal getPatientCholesterol(Patient patient){
        //ToDo: Getting ID is broken
        String patientID = patient.getIdElement().getIdPart();
        //Put together search string to query for the data
        String searchString =
                "Observation?patient=" + patientID + "&code=" + cholesterolCode + "&_sort=date&_count=13";
        //Call the query through the API
        Bundle results = client.search().byUrl(searchString).returnBundle(Bundle.class).execute();
        //Parse relevant data out of bundle result
        Base base = results.getEntry().get(0).getResource().getNamedProperty("valueQuantity").getValues().get(0);
        Quantity cholesterolQuantity = base.castToQuantity(base);
        return cholesterolQuantity.getValue();
    }

}
