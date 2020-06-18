package projecy;

public class PatientDataFactory {
    public PatientData createPatientData(GetBaseData dataGetter, String patientID, DataTypes dataType){
        if (dataType == DataTypes.Blood_Pressure) {
            return new BloodPressureData(dataGetter, patientID);
        }
        else if (dataType == DataTypes.Cholesterol) {
            return new CholesterolData(dataGetter, patientID);
        }
        else {
            return null;
        }
    }
}
