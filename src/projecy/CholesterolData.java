package projecy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Quantity;

import java.math.BigDecimal;

public class CholesterolData extends PatientData{
    public CholesterolData(GetBaseData cholesterolGetter, String patientID) {
        super(cholesterolGetter, patientID);
        updateValues();
    }

    public DataTypes getDataType() {
        return DataTypes.Cholesterol;
    }


}
