package Examples;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.*;

public class requestExample {
    public static void main(String[] args) {

        String patientID = "3689";
        String practitionerID = "3";
        //Setup
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("https://fhir.monash.edu/hapi-fhir-jpaserver/fhir");

        //Get details on patient:
        Patient patient = client.read().resource(Patient.class).withId(patientID).execute();
        HumanName name = patient.getName().get(0);
        System.out.println("Patient's given names are: " + name.getGiven());
        Address address = patient.getAddress().get(0);
        System.out.println("Patient's city is: " + address.getCity());

        //Get cholesterol level on patient
        Bundle results = client
               .search()
                .byUrl("Observation?patient=" + patientID + "&code=55284-4&_sort=date&_count=13")
               .returnBundle(Bundle.class)
               .execute();
        Quantity quantity1 = new Quantity();
        Base base = results.getEntry().get(0).getResource()
                .getNamedProperty("valueQuantity").getValues().get(0);
        quantity1 = base.castToQuantity(base);
        System.out.println("Patient's cholesterol level is: " + quantity1.getValue() + quantity1.getUnit());

        /*
        //Get Patients from Practitioner
        Bundle patient = client.search().byUrl("Encounter?participant.identifier=http://hl7.org/fhir/sid/us-npi|500&_include=Encounter.participant.individual&_include=Encounter.patient")
                .returnBundle(Bundle.class)
                .execute();
        System.out.println(patient.toString());
        */


    }
}
