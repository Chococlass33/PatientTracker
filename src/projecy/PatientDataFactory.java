package projecy;

/**
 * Class to create the appropriate PatientData according to what dataType is given
 */
public class PatientDataFactory {
    /**
     *
     * @param dataGetter: The dataGetter that will be used to populate the PatientData with infornmation
     * @param patientID: The ID of the patient that the data is to be associated with
     * @param dataType: the dataType of the patientData that is to be created
     * @return: a PatientData of the class specified by the dataType
     */
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
