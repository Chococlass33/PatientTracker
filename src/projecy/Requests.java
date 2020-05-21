package projecy;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;


public class Requests implements GetPatients, GetPatientsCholesterol,GetMeka {
    private IGenericClient client;
    private String baseURL;
    private static String CHOLESTEROL_CODE = "2093-3";
    private static String BLOODPRESSURE = "55284-4";
    private static String BMI = "39156-5";
    private static String SMOKING = "72166-2";
    public Requests(String baseURL) {
        FhirContext ctx = FhirContext.forR4();
        this.client = ctx.newRestfulGenericClient(baseURL);
        this.baseURL = baseURL;
    }
    public CholesterolPatient getPatient(String patientID){
        Patient patient = client.read().resource(Patient.class).withId(patientID).execute();
        CholesterolPatient cholesterolPatient = new CholesterolPatient(patient, getPatientCholesterol(patient.getIdElement().getIdPart()));
        return cholesterolPatient;
    }
    public Base getPatientCholesterol(String patientID) {
        //Put together search string to query for the data
        String searchString =
                "Observation?patient=" + patientID + "&code=" + CHOLESTEROL_CODE + "&_sort=date&_count=13";
        //Call the query through the API
        Bundle results = client.search().byUrl(searchString).returnBundle(Bundle.class).execute();
        //Parse relevant data out of bundle result
        Base cholesterolResource = results.getEntry().get(0).getResource();
        //.getNamedProperty("valueQuantity").getValues().get(0);
        //Quantity cholesterolQuantity = base.castToQuantity(base);
        return cholesterolResource;
    }
    public List<List<Bundle>> getAllOfObservation(int BundleCount) {
        //Put together search string to query observations
        String[] codes = {CHOLESTEROL_CODE,BLOODPRESSURE,BMI,SMOKING};
        List<List<Bundle>> bundles = new ArrayList<>();
        for (String code:codes)
        {
            List<Bundle> bundle = new ArrayList<>();
            String searchString =
                    "Observation?code=" + code + "&_sort=date&_count=200";
            Bundle results = client.search().byUrl(searchString).returnBundle(Bundle.class).execute();
            bundle.add(results);
            Base nextcallbase = results.getNamedProperty("link").getValues().get(1).getNamedProperty("url").getValues().get(0);
            for(int i = 200; i < BundleCount; i += 200)
            {
                try
                {
                    Thread.sleep(4000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                try
                {
                    String nextcall = nextcallbase.castToString(nextcallbase).toString();
                    Bundle nextresults = client.search().byUrl(nextcall).returnBundle(Bundle.class).execute();
                    bundle.add(nextresults);
                    nextcallbase = nextresults.getNamedProperty("link").getValues().get(1).getNamedProperty("url").getValues().get(0);
                } catch(FhirClientConnectionException exception)
                {
                    i = i - 200;
                }
            }
            System.out.println(bundle.get(1).getEntry());
            bundles.add(bundle);
        }
        return bundles;
    }
    public Patient getPatientMeka(String url) {
        //Put together search string to query observations
        Patient results = client.read().resource(Patient.class).withId(url.replace("Patient/","")).execute();
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return results;
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
