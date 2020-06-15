package projecy;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;


public abstract class PatientData {

    protected GetBaseData dataGetter;
    protected String patientID;

    protected ArrayList<DoubleProperty> dataValue = new ArrayList();
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

    public Double getValue(int propertyIndex){return dataValue.get(propertyIndex).get();}
    public DoubleProperty valueProperty(int propertyIndex) {return dataValue.get(propertyIndex);}

}
