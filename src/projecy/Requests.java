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
        /**
         * generates a CholesterolPatient object from the details of the patient given by patientID
         * @param patientID: the id of the patient to get
         * @return: a CholesterolPatient object with the details of the patient from the server
         */
        Patient patient = client.read().resource(Patient.class).withId(patientID).execute();
        CholesterolPatient cholesterolPatient = new CholesterolPatient(patient, getPatientCholesterol(patient.getIdElement().getIdPart()));
        return cholesterolPatient;
    }
    public Base getPatientCholesterol(String patientID) {
        /**
         * gets base structure containing latest cholesterol infornmation for the patient
         * @param patientID: the id of the patient to get the cholesterol of
         * @return: the base structure containing cholesterol infornmation
         */
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
        /**
         * Gets all the patients related to a practitioner given by the practitionerIdentifier.
         * @param practitionerIdentifier: the identifier of the practitioner to find the patients of
         * @return: an array of the unique patients found that have cholesterol values associated
         */
        ArrayList<Bundle.BundleEntryComponent> encounters = this.getAllEncounters(practitionerIdentifier);
        ArrayList<CholesterolPatient> cholesterolPatients = new ArrayList<>();
        //All the patients that don't have a cholesterol value are added to noCholesterolPatients so they arn't requested for again
        ArrayList<CholesterolPatient> noCholesterolPatients = new ArrayList<>();
        for (int i = 0; i < encounters.size(); i++) {
            String id = encounters.get(i).getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            id = id.replace("Patient/", "");
            CholesterolPatient dummyPatient = new CholesterolPatient("", id);
            //Check that patient has not previously been encountered before waisting a request
            if (!cholesterolPatients.contains(dummyPatient)){
                if (!noCholesterolPatients.contains(dummyPatient)){
                    try {
                        cholesterolPatients.add(getPatient(id));
                    } catch (IndexOutOfBoundsException e) {
                        //Patient did not have a cholesterol value
                        noCholesterolPatients.add(dummyPatient);
                    }
                }
            }
        }
        return cholesterolPatients;
    }
    private ArrayList<Bundle.BundleEntryComponent> getAllEncounters(String practitionerIdentifier) {
        /**
         * Gets all the encounters related to a practitioner given by practitionerIdentifier
         * Note, this function is limited to MAX_PAGE number of pages of results
         * @param practitioenrIdentifier: the identifier of the practitioner
         * @return: an arraylist of a bundle structure containing the encounters of that practitioner
         */
        int MAX_PAGE = 5;
        ArrayList<Bundle.BundleEntryComponent> allEncounters = new ArrayList<Bundle.BundleEntryComponent>();
        String searchUrl = "Encounter?participant.identifier=http://hl7.org/fhir/sid/us-npi|" + practitionerIdentifier
                + "&_include=Encounter.participant.individual&_include=Encounter.patient";
        int pageCount = 0;
        do {
            //Loop over pages of results, adding them to allEncounters
            Bundle encounters = client.search()
                    .byUrl(searchUrl)
                    .returnBundle(Bundle.class)
                    .execute();
            allEncounters.addAll(encounters.getEntry());
            System.out.println(pageCount);
            int linkTraversal = 0;
            do {
                try {
                    //Find the link to the next page of results
                    if (encounters.getLink().get(linkTraversal).getRelation().compareTo("next") == 0) {
                        searchUrl = encounters.getLink().get(linkTraversal).getUrl();
                        pageCount++;
                        break;
                    }
                }catch (IndexOutOfBoundsException e) {
                    //No more pages of results. Return
                    return allEncounters;
                }
                linkTraversal++;
            } while (true);
        } while(pageCount < MAX_PAGE);
        return allEncounters;
    }

}
