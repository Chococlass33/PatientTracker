package projecy;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;

public class CholesterolPatient {
    private String name;
    private String id;
    private BigDecimal cholesterolValue;
    private StringProperty cholesterolString = new SimpleStringProperty();
    private StringProperty updateTime = new SimpleStringProperty();
    private Address address;
    private Enumerations.AdministrativeGender gender;
    private DateType birthDate;
    public CholesterolPatient(String name, String id) {
        /**
         * Create simple CholesterolPatient with only name and id
         */
        this.name = name;
        this.id = id;
    }
    public CholesterolPatient(Patient patient, Base cholesterolResource) {
        /**
         * create full CholesterolPatient
         * @param patient: The Patient class to get the patient's details from
         * @param cholesterolResource: The base class to get the cholesterol details from
         */
        name = patient.getName().get(0).getNameAsSingleString();
        this.updateCholesterolAndTime(cholesterolResource);
        gender = patient.getGender();
        address = patient.getAddress().get(0);
        id = patient.getIdElement().getIdPart();
        birthDate = patient.getBirthDateElement();
    }
    public void updateCholesterolAndTime(Base cholesterolBase) {
        /**
         * Updates the patient's cholesteral values and updated time with a new value based on cholesterolBase
         * @param CholesterolBase: the base class that contains relevant data for cholesterol
         */
        //Unwrap and set cholesterol value and string
        Base valueQuantity = cholesterolBase.getNamedProperty("valueQuantity").getValues().get(0);
        Quantity cholesterolLevel = valueQuantity.castToQuantity(valueQuantity);
        cholesterolValue = cholesterolLevel.getValue();
        cholesterolString.set(cholesterolValue.toString() + ' ' + cholesterolLevel.getUnit());
        //Unwrap, process and set date of birth
        String rawDate = cholesterolBase.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");
        this.updateTime.set(processedDate);
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
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        //Override so that if ID matches, the patients are equal regardless of other attribtues
        if (o.getClass() == getClass()) {
            CholesterolPatient patient = (CholesterolPatient) o;
            if (patient.id.compareTo(this.id) == 0) {
                return true;
            }
        }
        return false;
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
    public StringProperty cholesterolStringProperty() {return cholesterolString;};
    public StringProperty timeProperty() {return updateTime;};
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getCholesterolString() {return cholesterolString.get();}
    public String getUpdateTime() {return updateTime.get();}
    public BigDecimal getCholesterolValue(){return cholesterolValue;}
    public String getID() {return id;}

}
