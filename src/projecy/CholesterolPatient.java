package projecy;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;

public class CholesterolPatient {
    private String name;
    private BigDecimal cholesterolValue;
    private StringProperty cholesterolString = new SimpleStringProperty();
    private StringProperty updateTime = new SimpleStringProperty();
    private String id;
    public CholesterolPatient(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public CholesterolPatient(Patient patient, Base cholesterolResource) {
        name = patient.getName().get(0).getNameAsSingleString();
        this.updateCholesterolAndTime(cholesterolResource);

        id = patient.getIdElement().getIdPart();
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
    public BigDecimal getCholesterolValue(){
        return cholesterolValue;
    }
    public String getID() {
        return id;
    }

}
