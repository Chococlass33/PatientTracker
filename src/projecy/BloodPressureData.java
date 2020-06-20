package projecy;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Quantity;

import java.util.ArrayList;
import java.util.List;

/**
 * PatientData associated with DataTypes.BloodPressure that stores and handles data relating to a patient's blood pressure
 */
public class BloodPressureData extends PatientData {
    public ObservableList<Double> systolicHistoryValues;
    public ObservableList<String> systolicHistoryTimes;
    @Override
    public DataTypes getDataType() {
        return DataTypes.Blood_Pressure;
    }

    public BloodPressureData(GetBaseData dataGetter, String patientID) {
        super(dataGetter, patientID);
    }

    @Override
    public void updateValues() {
        List<Bundle.BundleEntryComponent> resourceList = this.getDataGetter().getPatientResourceBase(this.getPatientID(), this.getDataType());
        Base resourceBase = resourceList.get(resourceList.size()-1).getResource();
        //Unwrap and set Blood pressure value and string
        List valueQuantities = resourceBase.getNamedProperty("component").getValues();
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
        if(systolicHistoryValues == null) {
            systolicHistoryValues = FXCollections.observableArrayList();
            systolicHistoryTimes = FXCollections.observableArrayList();
        }
        systolicHistoryTimes.clear();
        systolicHistoryValues.clear();
        //Fill systolicHistory
        for(int i=0; i < PatientData.observationsNumbersRecorded && i < resourceList.size() ; i++) {

            Base base = resourceList.get(resourceList.size()-(i+1)).getResource();
            List systolicValueQuantities = base.getNamedProperty("component").getValues();
            Base valueQuantity = (Base) systolicValueQuantities.get(1);
            Base dataLevel = valueQuantity.getNamedProperty("value").getValues().get(0);
            Quantity dataQuantity = dataLevel.castToQuantity(dataLevel);
            String rawDateSystolic = base.getNamedProperty("effective").getValues().get(0).toString();
            systolicHistoryValues.add(dataQuantity.getValue().doubleValue());
            systolicHistoryTimes.add(processDate(rawDateSystolic));
        }
        //Unwrap, process and set time of measurement
        String rawDate = resourceBase.getNamedProperty("effective").getValues().get(0).toString();
        this.updateTimeProperty(rawDate);
    }
}
