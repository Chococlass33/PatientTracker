package projecy;

import javafx.beans.property.SimpleStringProperty;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Quantity;

public class CholesterolData extends PatientData {
    @Override
    public DataTypes getDataType() {
        return DataTypes.Cholesterol;
    }

    public CholesterolData(GetBaseData dataGetter, String patientID) {
        super(dataGetter, patientID);
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
        if (dataValue.size() == 0) {
            dataValue.add(cholesterolLevel.getValue());
        } else {
            dataValue.set(0, cholesterolLevel.getValue());
        }
        if (dataString.size() == 0) {
            dataString.add(new SimpleStringProperty(dataValue.toString() + ' ' + cholesterolLevel.getUnit()));
        }
        dataString.get(0).set(dataValue.toString() + ' ' + cholesterolLevel.getUnit());
        //Unwrap, process and set date of birth
        String rawDate = cholesterolBase.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");
        this.updateTime.set(processedDate);
    }
}
