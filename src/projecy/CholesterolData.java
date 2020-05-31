package projecy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Quantity;

import java.math.BigDecimal;

public class CholesterolData implements PatientData{
    private BigDecimal cholesterolValue;
    private StringProperty cholesterolString = new SimpleStringProperty();
    private StringProperty updateTime = new SimpleStringProperty();
    private GetCholesterol cholesterolGetter;
    private String patientID;
    public CholesterolData(GetCholesterol cholesterolGetter, String patientID) {
        this.cholesterolGetter = cholesterolGetter;
        this.patientID = patientID;
        updateValues();
    }

    @Override
    public String getDataType() {
        return "Cholesterol";
    }

    public void updateValues() {
        /**
         * Updates the patient's cholesteral values and updated time with a new value based on cholesterolBase
         * @param CholesterolBase: the base class that contains relevant data for cholesterol
         */
        Base cholesterolBase = this.cholesterolGetter.getCholesterol(patientID);
        //Unwrap and set cholesterol value and string
        Base valueQuantity = cholesterolBase.getNamedProperty("valueQuantity").getValues().get(0);
        Quantity cholesterolLevel = valueQuantity.castToQuantity(valueQuantity);
        cholesterolValue = cholesterolLevel.getValue();
        cholesterolString.set(cholesterolValue.toString() + ' ' + cholesterolLevel.getUnit());
        //Unwrap, process and set date of birth
        String rawDate = cholesterolBase.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");
        this.updateTime.set(processedDate);
    }

    public StringProperty StringProperty() {return cholesterolString;};
    public StringProperty timeProperty() {return updateTime;};

    public String getValueString() {return cholesterolString.get();}
    public String getUpdateTime() {return updateTime.get();}
    public BigDecimal getValue(){return cholesterolValue;}
}
