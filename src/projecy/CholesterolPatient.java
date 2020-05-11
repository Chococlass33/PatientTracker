package projecy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.*;

public class CholesterolPatient {
    private String name;
    private StringProperty cholesterol = new SimpleStringProperty();
    private StringProperty updateTime = new SimpleStringProperty();
    private String id;
    public CholesterolPatient(Patient patient, Base cholesterolResource) {
        name = patient.getName().get(0).getNameAsSingleString();
        this.setCholesterol(cholesterolResource);
        String rawDate = cholesterolResource.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");

        this.updateTime.set(processedDate);
        id = patient.getIdElement().getIdPart();
    }

    public StringProperty cholesterolProperty() {return cholesterol;};
    public StringProperty timeProperty() {return updateTime;};
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCholesterol() {
        return cholesterol.get();
    }
    public String getUpdateTime() {
        return updateTime.get();
    }
    public void setCholesterol(Base cholesterolBase) {
        Base valueQuantity = cholesterolBase.getNamedProperty("valueQuantity").getValues().get(0);
        Quantity cholesterolLevel = valueQuantity.castToQuantity(valueQuantity);
        cholesterol.set(cholesterolLevel.getValue().toString() + ' ' + cholesterolLevel.getUnit());
    }
    public String getID() {
        return id;
    }

}
