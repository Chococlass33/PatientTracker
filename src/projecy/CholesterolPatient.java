package projecy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.Patient;

import java.math.BigDecimal;

public class CholesterolPatient {
    private String name;
    private StringProperty cholesterol = new SimpleStringProperty();
    private String id;
    public CholesterolPatient(Patient patient, BigDecimal cholesterolLevel) {
        name = patient.getName().get(0).getNameAsSingleString();
        cholesterol.set(cholesterolLevel.toString());
        id = patient.getIdElement().getIdPart();
    }

    public StringProperty cholesterolProperty() {return cholesterol;};
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCholesterol() {
        return cholesterol.get();
    }
    public void setCholesterol(BigDecimal cholesterolLevel) {
        cholesterol.set(cholesterolLevel.toString());
    }
    public String getID() {
        return id;
    }

}
