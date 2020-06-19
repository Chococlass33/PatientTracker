package projecy;

import javafx.beans.property.SimpleDoubleProperty;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Quantity;

import java.util.List;

public class BloodPressureData extends PatientData {

    @Override
    public DataTypes getDataType() {
        return DataTypes.Blood_Pressure;
    }

    public BloodPressureData(GetBaseData dataGetter, String patientID) {
        super(dataGetter, patientID);
    }

    @Override
    public void updateValues() {
        Base cholesterolBase = this.getDataGetter().getPatientResourceBase(this.getPatientID(), this.getDataType());
        //Unwrap and set cholesterol value and string
        List valueQuantities = cholesterolBase.getNamedProperty("component").getValues();
        assert (valueQuantities.size() == getDataType().DATA_VALUE_COUNT);
        for(int i = 0; i < getDataType().DATA_VALUE_COUNT; i++) {
            Base valueQuantity = (Base) valueQuantities.get(i);
            Base dataLevel = valueQuantity.getNamedProperty("value").getValues().get(0);
            Quantity dataQuantity = dataLevel.castToQuantity(dataLevel);
            if (this.getDataValue().get(i) == null) {
                this.getDataValue().set(i, new SimpleDoubleProperty(dataQuantity.getValue().doubleValue()));
            }
            else {
                this.getDataValue().get(i).set(dataQuantity.getValue().doubleValue());
            }

            getDataString().get(i).set(getDataValue().get(i).doubleValue() + " " + dataQuantity.getUnit());
        }
        //Unwrap, process and set date of birth
        String rawDate = cholesterolBase.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");
        this.updateTimeProperty().set(processedDate);
    }
}
