package projecy;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;


public abstract class PatientData {



    private GetBaseData dataGetter;
    private String patientID;
    private ArrayList<DoubleProperty> dataValue = new ArrayList();
    private ArrayList<StringProperty> dataString = new ArrayList();
    private StringProperty updateTime = new SimpleStringProperty();
    public abstract DataTypes getDataType();

    public PatientData(GetBaseData dataGetter, String patientID) {
        this.dataGetter = dataGetter;
        this.patientID = patientID;
        for (int i = 0; i < getDataType().DATA_VALUE_COUNT; i++) {
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
    protected GetBaseData getDataGetter() {
        return dataGetter;
    }
    protected String getPatientID() {
        return patientID;
    }
    protected ArrayList<DoubleProperty> getDataValue() {
        return dataValue;
    }
    protected ArrayList<StringProperty> getDataString() {
        return dataString;
    }
    protected StringProperty updateTimeProperty() {
        return updateTime;
    }
}
