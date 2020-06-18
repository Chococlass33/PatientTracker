package projecy;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;


public class Requests implements GetPatients, GetWeka, GetBaseData
{
    private IGenericClient client;
    private String baseURL;
    private static String CHOLESTEROL_CODE = "2093-3";
    private static String BLOODPRESSURE = "55284-4";
    private static String BMI = "39156-5";
    private static String SMOKING = "72166-2";
    public Requests(String baseURL) {
        FhirContext ctx = FhirContext.forR4();
        ctx.getRestfulClientFactory().setConnectTimeout(0);
        ctx.getRestfulClientFactory().setConnectionRequestTimeout(0);
        ctx.getRestfulClientFactory().setSocketTimeout(0);
        this.client = ctx.newRestfulGenericClient(baseURL);
        this.baseURL = baseURL;
    }
    public DataPatient getPatient(String patientID){
        /**
         * generates a DataPatient object from the details of the patient given by patientID
         * @param patientID: the id of the patient to get
         * @return: a DataPatient object with the details of the patient from the server
         */
        Patient patient = client.read().resource(Patient.class).withId(patientID).execute();
        DataPatient dataPatient = new DataPatient(patient, this);
        return dataPatient;
    }
    public Base getPatientResourceBase(String patientID, DataTypes dataType) {
        Bundle results = getPatientResourceBundle(patientID, dataType.code, "13");
        //Parse relevant data out of bundle result
        return results.getEntry().get(0).getResource();
    }
    private Bundle getPatientResourceBundle(String patientID, String resourceCode, String count) {
        //Put together search string to query for the data
        String searchString;
        if (patientID == null) {
            searchString =
                    "Observation?code=" + resourceCode + "&_sort=date&_count=" + count;
        } else {
            searchString =
                    "Observation?patient=" + patientID + "&code=" + resourceCode + "&_sort=date&_count=" + count;
        }
        //Call the query through the API
        return client.search().byUrl(searchString).returnBundle(Bundle.class).execute();
    }

    /**
     * Grabs all the codes above, and grabs the latest BundleCount number of them from the server.
     * @param BundleCount Number of each observation to grab
     * @return a list of lists containing the bundles of each code type.
     */
    public List<List<Bundle>> getAllOfObservation(int BundleCount) {
        //Put together search string to query observations
        String[] codes = {CHOLESTEROL_CODE,BLOODPRESSURE,BMI,SMOKING};
        List<List<Bundle>> bundles = new ArrayList<>();

        //For each code, create a new list and start requesting bundles of that code
        for (String code:codes)
        {
            List<Bundle> bundle = new ArrayList<>();

            //grab data
            Bundle results =getPatientResourceBundle(null, code, "200");
            bundle.add(results);

            //get the next relation URL
            Base nextcallbase = results.getNamedProperty("link").getValues().get(1).getNamedProperty("url").getValues().get(0);

            //Keep calling the next relation until bundlecount
            for(int i = 200; i < BundleCount; i += 200)
            {
                //Added a sleep of 4 seconds to reduce strain to server
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
                } catch(Exception exception)
                {
                    //if failed, try again
                    exception.printStackTrace();
                    i = i - 200;
                }
            }
            bundles.add(bundle);
        }
        return bundles;
    }

    /**
     * Takes in a patient URL and returns the patient
     * @param url URL of patient
     * @return Patient object
     */
    public Patient getPatientWeka(String url) {
        //Add a pause to reduce server load
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        try
        {
            Patient results = client.read().resource(Patient.class).withId(url.replace("Patient/", "")).execute();
            return results;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return getPatientWeka(url);
        }

    }

    public ArrayList<DataPatient> getPatientsForPractitioner(String practitionerIdentifier) {
        /**
         * Gets all the patients related to a practitioner given by the practitionerIdentifier.
         * @param practitionerIdentifier: the identifier of the practitioner to find the patients of
         * @return: an array of the unique patients found that have cholesterol values associated
         */
        ArrayList<Bundle.BundleEntryComponent> encounters = this.getAllEncounters(practitionerIdentifier);
        ArrayList<DataPatient> patients = new ArrayList<>();
        //All the patients that don't have a cholesterol value are added to noDataPatients so they arn't requested for again
        ArrayList<BasePatient> noDataPatients = new ArrayList<>();
        for (int i = 0; i < encounters.size(); i++) {
            String id = encounters.get(i).getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            id = id.replace("Patient/", "");
            BasePatient dummyPatient = new BasePatient("", id);
            //Check that patient has not previously been encountered before waisting a request
            if (!patients.contains(dummyPatient)){
                if (!noDataPatients.contains(dummyPatient)){
                    try {
                        patients.add(getPatient(id));
                    } catch (IndexOutOfBoundsException e) {
                        //Patient did not have a cholesterol value
                        noDataPatients.add(dummyPatient);
                    }
                }
            }
        }
        return patients;
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
