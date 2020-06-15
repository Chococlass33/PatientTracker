package projecy;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

        if (dataValue.get(0) == null) {
            dataValue.set(0, new SimpleDoubleProperty(cholesterolLevel.getValue().doubleValue()));
        } else {
            dataValue.get(0).set(cholesterolLevel.getValue().doubleValue());

            //Testing changing values
            /*
            Double num = dataValue.get(0).doubleValue() + 3;
            dataValue.get(0).set(num);
            */

        }



        if (dataString.size() == 0) {
            dataString.add(new SimpleStringProperty(dataValue.get(0).doubleValue() + ' ' + cholesterolLevel.getUnit()));
        }
        dataString.get(0).set(dataValue.get(0).doubleValue() + ' ' + cholesterolLevel.getUnit());

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
