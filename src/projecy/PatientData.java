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
    protected static int observationsNumbersRecorded = 5;
    /**
     * Each child class will override this method to return their correspondant DataType
     * @eturn: the appropriate enum value for the associated data type
     */
    public abstract DataTypes getDataType();


    /**
     * Constructor for PatientData. Sets dataGetter and patientID from input, and initializes dataString and dataValue
     * with default values
     * @param dataGetter: the source of the data to populate the child class
     * @param patientID: the ID of the patient that this data is associated with
     */
    public PatientData(GetBaseData dataGetter, String patientID) {

        this.dataGetter = dataGetter;
        this.patientID = patientID;
        for (int i = 0; i < getDataType().DATA_VALUE_COUNT; i++) {
            this.dataString.add(new SimpleStringProperty(""));
            this.dataValue.add(null);
        }
        updateValues();
    }

    /**
     * method called to update the values present in the data class with new ones retrieved from the server
     */
    public abstract void updateValues();
    public StringProperty stringProperty(int propertyIndex) {return dataString.get(propertyIndex);}
    public StringProperty timeProperty() {return updateTime;}
    public String getValueString(int propertyIndex) {return dataString.get(propertyIndex).get();}
    public String getUpdateTime() {return updateTime.get();}
    public Double getValue(int propertyIndex){return dataValue.get(propertyIndex).get();}
    public DoubleProperty valueProperty(int propertyIndex) {return dataValue.get(propertyIndex);}
    protected GetBaseData getDataGetter() {return dataGetter;}
    protected String getPatientID() {return patientID;}
    protected ArrayList<DoubleProperty> getDataValue() {return dataValue;}
    protected ArrayList<StringProperty> getDataString() {return dataString;}

    /**
     * update the current date with a new date string, in format desired
     * @param rawDate: A string produced from a .toString() method on a dateTime type
     */
    protected void updateTimeProperty(String rawDate) {
        this.updateTime.set(processDate(rawDate));
    }

    /**
     * reformats string into format desired
     * @param rawDate: A string produced from a .toString method on a dateTime type
     * @return: The same date, formatted in the correct display format
     */
    protected String processDate(String rawDate) {
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        return rawDate.replace("]", "");

    }
}
