package projecy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Quantity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public abstract class PatientData {

    protected GetBaseData dataGetter;
    protected String patientID;
    protected ArrayList<BigDecimal> dataValue = new ArrayList();
    protected ArrayList<StringProperty> dataString = new ArrayList();
    protected StringProperty updateTime = new SimpleStringProperty();
    public abstract DataTypes getDataType();

    public PatientData(GetBaseData dataGetter, String patientID) {
        this.dataGetter = dataGetter;
        this.patientID = patientID;
        for (int i=0; i < getDataType().dataValueCount; i++) {
            this.dataString.add(new SimpleStringProperty(""));
            this.dataValue.add(null);
        }
        updateValues();
    }

    public abstract void updateValues();
    public StringProperty stringProperty(int propertyIndex) {return dataString.get(propertyIndex);}

    public StringProperty timeProperty() {return updateTime;}

    public String getValueString(int propertyIndex) {return dataString.get(propertyIndex).get();}

    public String getUpdateTime() {return updateTime.get();}

    public BigDecimal getValue(int propertyIndex){return dataValue.get(propertyIndex);}

}
