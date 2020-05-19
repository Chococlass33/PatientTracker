package projecy;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.*;
import java.util.ArrayList;


public class Requests implements GetPatients {
    private IGenericClient client;
    private String cholesterolCode = "2093-3";
    public Requests(String baseURL) {
        FhirContext ctx = FhirContext.forR4();
        this.client = ctx.newRestfulGenericClient(baseURL);
    }
    public CholesterolPatient getPatient(String patientID){
        Patient patient = client.read().resource(Patient.class).withId(patientID).execute();
        CholesterolPatient cholesterolPatient = new CholesterolPatient(patient, getPatientCholesterol(patient.getIdElement().getIdPart()));
        return cholesterolPatient;
    }
    public Base getPatientCholesterol(String patientID) {
        //Put together search string to query for the data
        String searchString =
                "Observation?patient=" + patientID + "&code=" + cholesterolCode + "&_sort=date&_count=13";
        //Call the query through the API
        Bundle results = client.search().byUrl(searchString).returnBundle(Bundle.class).execute();
        //Parse relevant data out of bundle result
        Base cholesterolResource = results.getEntry().get(0).getResource();
        //.getNamedProperty("valueQuantity").getValues().get(0);
        //Quantity cholesterolQuantity = base.castToQuantity(base);
        return cholesterolResource;
    }
    public ArrayList<CholesterolPatient> getPatientsForPractitioner(String practitionerIdentifier) {
        ArrayList<Bundle.BundleEntryComponent> encounters = this.getAllEncounters(practitionerIdentifier);
        ArrayList<CholesterolPatient> cholesterolPatients = new ArrayList<>();
        for (int i = 0; i < encounters.size(); i++) {
            String name = encounters.get(i).getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("display").getValues().get(0).toString();
            String id = encounters.get(i).getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            id = id.replace("Patient/", "");
            CholesterolPatient newPatient = new CholesterolPatient(name, id);
            if (!cholesterolPatients.contains(newPatient)){
                cholesterolPatients.add(new CholesterolPatient(name, id));
            }
        }
        return cholesterolPatients;
    }
    private ArrayList<Bundle.BundleEntryComponent> getAllEncounters(String practitionerIdentifier) {
        ArrayList<Bundle.BundleEntryComponent> allEncounters = new ArrayList<Bundle.BundleEntryComponent>();
        String searchUrl = "Encounter?participant.identifier=http://hl7.org/fhir/sid/us-npi|" + practitionerIdentifier
                + "&_include=Encounter.participant.individual&_include=Encounter.patient";
        int pageCount = 0;
        do {
            Bundle encounters = client.search()
                    .byUrl(searchUrl)
                    .returnBundle(Bundle.class)
                    .execute();
            allEncounters.addAll(encounters.getEntry());
            System.out.println(pageCount);
            int linkTraversal = 0;
            do {
                try {
                    if (encounters.getLink().get(linkTraversal).getRelation().toString().compareTo("next") == 0) {
                        searchUrl = encounters.getLink().get(linkTraversal).getUrl();
                        pageCount++;
                        break;
                    }
                }catch (IndexOutOfBoundsException e) {
                    return allEncounters;
                }
                linkTraversal++;
            } while (true);
        } while(pageCount < 5);
        return allEncounters;
    }

}
