package projecy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MonitoredPatientList extends PatientList {
    private ScheduledExecutorService updateDataService;
    private ScheduledFuture scheduledFutureUpdateService;
    private Runnable updateData;
    private ArrayList<DataTypes> updateTypes = new ArrayList<>();
    private Boolean runningUpdate = false;
    @Override
    public void addPatient(DataPatient patient) {
        /**
         * Add Patient to self.patients
         * @param patient: The patient to add
         */
        patient.updateDataValues(updateTypes);
        super.addPatient(patient);
    }
    public MonitoredPatientList(GetBaseData requests) {
        /**
         * Creates new MonitoredPatientList
         * @param requests: object of type getPatientsCholesterol to get patients from
         */
        super(requests);
        final int DEFAULT_UPDATE_PERIOD = 20;
        this.updateData = new Runnable() {
            public void run() {
                /**
                 * Function that is run asynchronously every DEFAULT_UPDATE_PERIOD seconds
                 */
                updatePatientsValues();
            }
        };
        this.updateDataService = Executors.newScheduledThreadPool(1);
        scheduledFutureUpdateService = this.updateDataService.scheduleAtFixedRate(updateData, 0, DEFAULT_UPDATE_PERIOD, TimeUnit.SECONDS);
    }
    public void setUpdateTypes(ArrayList<DataTypes> updateTypes) {
        this.updateTypes = new ArrayList<>();
        this.updateTypes.addAll(updateTypes);
        updatePatientsValues();

    }
    private void updatePatientsValues() {
        /**
         * Method to update the cholesterol values of a particular patient
         * @Param patient: The patient to update the cholesterolValues of
         */
        if (!runningUpdate) {
            runningUpdate = true;
            Iterator<DataPatient> itr = patients.iterator();
            while (itr.hasNext()) {
                DataPatient patient = itr.next();
                try {
                    patient.updateDataValues((ArrayList<DataTypes>) updateTypes.clone());
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Can't update patient as they have no data value");
                    System.out.println("Removing patient from list");
                    itr.remove();
                }
            }
            System.out.println("Updated Data Values");
            runningUpdate = false;
        }

    }
    public void setUpdateFrequency(int timeBetweenUpdates) {
        /**
         * Method to set how often the scheduled updater will run it's function
         * @param timeBetweenUpdates: the time, in seconds between each update starting
         */
        scheduledFutureUpdateService.cancel(false);
        scheduledFutureUpdateService = this.updateDataService.scheduleWithFixedDelay(updateData, 0, timeBetweenUpdates, TimeUnit.SECONDS);
    }
    public double averageValue(DataTypes dataType, int dataIndex) {
        /**
         * Function to obtain the average cholesterol of all the patients being monitored
         * @return: BigDecimal value of the average cholesterol of all patients in self.patients
         */
        //Use floating point maths for this calculation as it works better for arithmatics
         double total = 0;
         int patientnum = 0;
        for (DataPatient patient : patients)
        {
            total += patient.findData(dataType).getValue(dataIndex).floatValue();
            total += 1;
            patientnum += 1;
        }
        //Cast result back to BigDecimal
        double returnDecimal = total/patientnum;
    return returnDecimal;
    }

}
