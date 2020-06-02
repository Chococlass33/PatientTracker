package projecy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Quantity;

import java.math.BigDecimal;

public abstract class PatientData {

    protected GetBaseData dataGetter;
    protected String patientID;
    private BigDecimal dataValue;
    private StringProperty dataString = new SimpleStringProperty();
    private StringProperty updateTime = new SimpleStringProperty();
    public abstract DataTypes getDataType();

    public PatientData(GetBaseData cholesterolGetter, String patientID) {
        this.dataGetter = cholesterolGetter;
        this.patientID = patientID;
    }

    public void updateValues() {
        /**
         * Updates the patient's cholesteral values and updated time with a new value based on cholesterolBase
         * @param CholesterolBase: the base class that contains relevant data for cholesterol
         */
        Base cholesterolBase = this.dataGetter.getPatientResourceBase(patientID, this.getDataType());
        //Unwrap and set cholesterol value and string
        Base valueQuantity = cholesterolBase.getNamedProperty("valueQuantity").getValues().get(0);
        Quantity cholesterolLevel = valueQuantity.castToQuantity(valueQuantity);
        dataValue = cholesterolLevel.getValue();
        dataString.set(dataValue.toString() + ' ' + cholesterolLevel.getUnit());
        //Unwrap, process and set date of birth
        String rawDate = cholesterolBase.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");
        this.updateTime.set(processedDate);
    }

    public StringProperty StringProperty() {return dataString;}

    public StringProperty timeProperty() {return updateTime;}

    public String getValueString() {return dataString.get();}

    public String getUpdateTime() {return updateTime.get();}

    public BigDecimal getValue(){return dataValue;}
}
