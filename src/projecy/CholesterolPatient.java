package projecy;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.AdministrativeGender;
import org.hl7.fhir.r4.model.codesystems.GenderIdentity;

import java.math.BigDecimal;

public class CholesterolPatient {
    private String name;
    private BigDecimal cholesterolValue;
    private StringProperty cholesterolString = new SimpleStringProperty();
    private StringProperty updateTime = new SimpleStringProperty();
    private String id;
    private Address address;
    private Enumerations.AdministrativeGender gender;
    private DateType birthDate;
    public CholesterolPatient(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public CholesterolPatient(Patient patient, Base cholesterolResource) {
        name = patient.getName().get(0).getNameAsSingleString();
        this.updateCholesterolAndTime(cholesterolResource);
        Base addressBase = patient.getAddress().get(0);
        address = addressBase.castToAddress(addressBase);
        gender = patient.getGender();
        address = patient.getAddress().get(0);
        id = patient.getIdElement().getIdPart();
    }
    public void updateCholesterolAndTime(Base cholesterolBase) {
        Base valueQuantity = cholesterolBase.getNamedProperty("valueQuantity").getValues().get(0);
        Quantity cholesterolLevel = valueQuantity.castToQuantity(valueQuantity);
        cholesterolValue = cholesterolLevel.getValue();
        cholesterolString.set(cholesterolValue.toString() + ' ' + cholesterolLevel.getUnit());
        String rawDate = cholesterolBase.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");
        this.updateTime.set(processedDate);
    }
    public String getAddressString() {
        String returnString = "City: " + address.getCity() + "\n";
        returnString += "State: " + address.getState() + "\n";
        returnString += "Country: " + address.getCountry() + "\n";
        return returnString;
    }
    public String getGenderString() {
        return gender.toString();
    }
    public String getBirthdateString() {
        return birthDate.toString();
    }
    public StringProperty cholesterolStringProperty() {return cholesterolString;};
    public StringProperty timeProperty() {return updateTime;};
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCholesterolString() {
        return cholesterolString.get();
    }
    public String getUpdateTime() {
        return updateTime.get();
    }

    public BigDecimal getCholesterolValue(){
        return cholesterolValue;
    }
    public String getID() {
        return id;
    }

}
