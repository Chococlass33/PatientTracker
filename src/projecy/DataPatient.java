package projecy;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataPatient extends BasePatient{
    protected Address address;
    protected Enumerations.AdministrativeGender gender;
    protected DateType birthDate;
    private ArrayList<PatientData> patientDataList;

    public DataPatient(Patient patient) {
        super(patient.getName().get(0).getNameAsSingleString(),patient.getIdElement().getIdPart());
        address = patient.getAddress().get(0);
        gender = patient.getGender();
        birthDate = patient.getBirthDateElement();
        patientDataList = new ArrayList<>();
    }
    public void addPatientData(PatientData data) {
        this.patientDataList.add(data);
    }
    public void updateDataValues() {
        for (int i=0; i<patientDataList.size(); i++) {
            patientDataList.get(i).updateValues();
            }
    }
    public PatientData findData(String dataType) {
        PatientData returnData = null;
        for (int i=0; i<patientDataList.size(); i++) {
            if (patientDataList.get(i).getDataType() == dataType) {
                returnData = patientDataList.get(i);
            }
        }
        return returnData;
    }
    public String getAddressString() {
        /**
         * returns address as a string for printing/displaying
         * @return: address in printable string form
         */
        String returnString = "City: " + address.getCity() + "\n";
        returnString += "State: " + address.getState() + "\n";
        returnString += "Country: " + address.getCountry() + "\n";
        return returnString;
    }

    public String getGenderString() {
        /**
         * returns gender as a string for printing/displaying
         * @return: gender in printable string form
         */
        return "Gender: " + gender.toString() + "\n";
    }

    public String getBirthdateString() {
        /**
         * returns birth date as a string for printing/displaying
         * @return: birth date in printable string form
         */
        return "Birthdate: " + birthDate.getValueAsString() + "\n";
    }
}
