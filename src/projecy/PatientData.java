package projecy;

import javafx.beans.property.StringProperty;
import org.hl7.fhir.r4.model.Base;

import java.math.BigDecimal;

public interface PatientData {
    DataTypes getDataType();
    void updateValues();
    StringProperty StringProperty();
    StringProperty timeProperty();
    String getValueString();
    String getUpdateTime();
    BigDecimal getValue();
}
