package projecy;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Quantity;

import java.math.BigDecimal;
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
        Base cholesterolBase = this.dataGetter.getPatientResourceBase(patientID, this.getDataType());
        //Unwrap and set cholesterol value and string
        List valueQuantities = cholesterolBase.getNamedProperty("component").getValues();
        assert (valueQuantities.size() == getDataType().dataValueCount);
        for(int i=0; i < getDataType().dataValueCount; i++) {
            Base valueQuantity = (Base) valueQuantities.get(i);
            Base dataLevel = valueQuantity.getNamedProperty("value").getValues().get(0);
            Quantity dataQuantity = dataLevel.castToQuantity(dataLevel);
            if (dataValue.get(i) == null) {
                dataValue.set(i, new SimpleDoubleProperty(dataQuantity.getValue().doubleValue()));
            }
            else {
                dataValue.get(i).set(dataQuantity.getValue().doubleValue());
            }

            dataString.get(i).set(dataValue.get(i).doubleValue() + ' ' + dataQuantity.getUnit());
        }
        //Unwrap, process and set date of birth
        String rawDate = cholesterolBase.getNamedProperty("effective").getValues().get(0).toString();
        rawDate = rawDate.replace("DateTimeType[", "");
        rawDate = rawDate.replace("T", " ");
        String processedDate = rawDate.replace("]", "");
        this.updateTime.set(processedDate);
    }
}
