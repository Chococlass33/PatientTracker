package projecy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitoredPatientList extends PatientList {
    private ScheduledExecutorService updateDataService;
    private Runnable updateData;
    private ArrayList<DataTypes> updateTypes = new ArrayList<>();
    public MonitoredPatientList(GetCholesterol requests) {
        /**
         * Creates new MonitoredPatientList
         * @param requests: object of type getPatientsCholesterol to get patients from
         */
        super(requests);
        final int DEFAULT_UPDATE_PERIOD = 60;
        this.updateData = new Runnable() {
            public void run() {
                /**
                 * Function that is run asynchronously every DEFAULT_UPDATE_PERIOD seconds
                 */
                for (DataPatient patient : patients){
                    updateValues(patient);
                    System.out.println("Updating Cholesterol");
                }
            }
        };
        this.updateDataService = Executors.newScheduledThreadPool(1);
        this.updateDataService.scheduleAtFixedRate(updateData, 0, DEFAULT_UPDATE_PERIOD, TimeUnit.SECONDS);
    }
    public void setUpdateTypes(ArrayList<DataTypes> updateTypes) {
        this.updateTypes = new ArrayList<>();
        this.updateTypes.addAll(updateTypes);
    }
    private void updateValues(DataPatient patient) {
        /**
         * Method to update the cholesterol values of a particular patient
         * @Param patient: The patient to update the cholesterolValues of
         */
        patient.updateDataValues((ArrayList<DataTypes>) updateTypes.clone());
    }
    public void setUpdateFrequency(int timeBetweenUpdates) {
        /**
         * Method to set how often the scheduled updater will run it's function
         * @param timeBetweenUpdates: the time, in seconds between each update starting
         */
        this.updateDataService.scheduleWithFixedDelay(updateData, 0, timeBetweenUpdates, TimeUnit.SECONDS);
    }
    private BigDecimal averageValue(DataTypes dataType) {
        /**
         * Function to obtain the average cholesterol of all the patients being monitored
         * @return: BigDecimal value of the average cholesterol of all patients in self.patients
         */
        //Use floating point maths for this calculation as it works better for arithmatics
         float total = 0;
         int patientnum = 0;
        for (DataPatient patient : patients)
        {
            total += patient.findData(dataType).getValue().floatValue();
            total += 1;
            patientnum += 1;
        }
        //Cast result back to BigDecimal
        BigDecimal returnDecimal = new BigDecimal(total/patientnum);
    return returnDecimal;
    }

    public boolean isBelowAverage(DataPatient patient, DataTypes dataType) {
        /**
         * Function to check if a patient's cholesterol is below average
         * @param patient: The patient to check
         * @return: True for the patient is below average, false for above or equal to average.
         */
        if (patient.findData(dataType).getValue().compareTo(averageValue(dataType)) == 1) {
         return true;
        }
        return false;
    }

}
