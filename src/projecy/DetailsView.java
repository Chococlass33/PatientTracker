package projecy;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DetailsView extends Region implements ListChangeListener<DataPatient>
{
    /**
     * Class to generate a region displaying all of a DataPatient's nessesary details
     */
    private Text textBox = new Text();
    private Double systolicLimit = new Double(140);
    private DataPatient patient = null;
    private LineView lineView =null;
    private VBox vbox = new VBox(textBox, new Text());
    private MonitoredPatientList patientList;
    public DetailsView(MonitoredPatientList patientList) {
        this.getChildren().add(vbox);
        patientList.patients.addListener(this);
    }
    public void setDetails(DataPatient patient) {
        /**
         * Method to set the data to display
         * @param patient: The patient of which to display the data of
         */
        this.patient = patient;
        redrawDetails();
    }

    /**
     * Method to add/update text and lineView to detailsView
     */
    public void redrawDetails() {

        if(patient != null) {
            String displayText = "\n";
            displayText += patient.getName() + "\n";
            displayText += patient.getAddressString();
            displayText += patient.getBirthdateString();
            displayText += patient.getGenderString();

            BloodPressureData data = (BloodPressureData) patient.findData(DataTypes.Blood_Pressure);
            if (systolicLimit != null && data != null) {
                if (data.valueProperty(1).getValue() > systolicLimit) {
                    displayText += "Systolic Blood Pressure: \n";
                    for (int i = 0; i < data.systolicHistoryTimes.size(); i++) {
                        displayText += data.systolicHistoryValues.get(i) + " (";
                        displayText += data.systolicHistoryTimes.get(i) + "), \n";
                    }
                    if(this.lineView == null) {
                        lineView = new LineView(this.patient);
                    } else {
                        lineView.updateData(this.patient);
                    }
                    vbox.getChildren().remove(1);
                    vbox.getChildren().add(lineView);
                }
                else
                {
                    vbox.getChildren().remove(1);
                    vbox.getChildren().add(new Text());
                }
            }
            textBox.setText(displayText);
        }
    }

    /**
     * Sets the limit where if above, the systolic blood pressure levels are displayed
     * @param newLimit: The new limit of systolic blood pressure to set
     */
    public void setSystolicLimit(Double newLimit) {
        systolicLimit = newLimit;
        redrawDetails();
    }


    @Override
    public void onChanged(Change<? extends DataPatient> c)
    {
        redrawDetails();
    }
}
