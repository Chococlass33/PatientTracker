package projecy;

import org.hl7.fhir.r4.model.Patient;

import java.math.BigDecimal;

public class CholesterolPatient {
    private String name;
    private BigDecimal cholesterol;
    public CholesterolPatient(Patient patient, BigDecimal cholesterolLevel) {
        name = patient.getName().get(0).getNameAsSingleString();
        cholesterol = cholesterolLevel;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getCholesterol() {
        return cholesterol;
    }

}
