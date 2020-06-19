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

        Base cholesterolBase = this.getDataGetter().getPatientResourceBase(this.getPatientID(), this.getDataType());
        //Unwrap and set cholesterol value and string
        Base valueQuantity = cholesterolBase.getNamedProperty("valueQuantity").getValues().get(0);
        Quantity cholesterolLevel = valueQuantity.castToQuantity(valueQuantity);

        if (this.getDataValue().get(0) == null) {
            this.getDataValue().set(0, new SimpleDoubleProperty(cholesterolLevel.getValue().doubleValue()));
        } else {
            this.getDataValue().get(0).set(cholesterolLevel.getValue().doubleValue());

            //Testing changing values
            /*
            Double num = dataValue.get(0).doubleValue() + 3;
            dataValue.get(0).set(num);
            */

        }
        if (getDataString().size() == 0) {
            getDataString().add(new SimpleStringProperty(getDataValue().get(0).doubleValue() + " " + cholesterolLevel.getUnit()));
        }
        getDataString().get(0).set(getDataValue().get(0).doubleValue() + " " + cholesterolLevel.getUnit());

        //Unwrap, process and set date of birth
        String rawDate = cholesterolBase.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");
        if (this.updateTimeProperty().getValue() == null) {
            this.updateTimeProperty().set(processedDate);
        } else {
            this.updateTimeProperty().set(this.updateTimeProperty().getValue() + "1");
        }



    }
}
