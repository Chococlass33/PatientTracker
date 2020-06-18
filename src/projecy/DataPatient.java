package projecy;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import java.util.ArrayList;

public class DataPatient extends BasePatient{
    private Address address;
    private Enumerations.AdministrativeGender gender;
    private DateType birthDate;
    private ArrayList<PatientData> patientDataList;
    private GetBaseData dataGetter;
    private PatientDataFactory dataFactory = new PatientDataFactory();

    public DataPatient(Patient patient, GetBaseData dataGetter) {
        super(patient.getName().get(0).getNameAsSingleString(),patient.getIdElement().getIdPart());
        address = patient.getAddress().get(0);
        gender = patient.getGender();
        birthDate = patient.getBirthDateElement();
        patientDataList = new ArrayList<>();
        this.dataGetter = dataGetter;
    }
    public void addPatientData(DataTypes dataType) {
        this.patientDataList.add(dataFactory.createPatientData(dataGetter, this.getID(), dataType));
    }
    public void updateDataValues(ArrayList<DataTypes> updateTypes) {
        updateTypesLoop:
        for (int i=0; i<updateTypes.size(); i++) {
            for (int j = 0; j < patientDataList.size(); j++) {
                if (updateTypes.get(i) == patientDataList.get(j).getDataType()) {
                    patientDataList.get(j).updateValues();
                    continue updateTypesLoop;
                }
            }
            addPatientData(updateTypes.get(i));
            }
    }
    public PatientData findData(DataTypes dataType) {
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
