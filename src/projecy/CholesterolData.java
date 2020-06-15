package projecy;

import javafx.beans.property.SimpleStringProperty;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Quantity;

import java.math.BigDecimal;

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
        /* Debug changing cholesterol value
        if (dataValue.get(0) != null) {
            BigDecimal num = dataValue.get(0);
            dataValue.set(0, dataValue.get(0).add(num));
        }else {
        */

            dataValue.set(0, cholesterolLevel.getValue());
        //}


        if (dataString.size() == 0) {
            dataString.add(new SimpleStringProperty(dataValue.toString() + ' ' + cholesterolLevel.getUnit()));
        }
        dataString.get(0).set(dataValue.toString() + ' ' + cholesterolLevel.getUnit());

        //Unwrap, process and set date of birth
        String rawDate = cholesterolBase.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");
        if (this.updateTime.getValue() == null) {
            this.updateTime.set(processedDate);
        } else {
            this.updateTime.set(this.updateTime.getValue() + "1");
        }



    }
}
