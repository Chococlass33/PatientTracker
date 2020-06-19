package projecy;

import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class DetailsView extends Region {
    /**
     * Class to generate a region displaying all of a DataPatient's nessesary details
     */
    private Text textBox = new Text();
    private Double systolicLimit = new Double(140);
    private DataPatient patient = null;
    public DetailsView() {
        this.getChildren().add(textBox);
    }
    public void setDetails(DataPatient patient) {
        /**
         * Method to set the data to display
         * @param patient: The patient of which to display the data of
         */
        this.patient = patient;
        redrawDetails();
    }
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
                    for (int i = 0; i < data.systolicHistoryTimes.size(); i++) {
                        displayText += data.systolicHistoryValues.get(i) + " (";
                        displayText += data.systolicHistoryTimes.get(i) + "), \n";
                    }
                }
            }
            textBox.setText(displayText);
        }
    }
    public void setSystolicLimit(Double newLimit) {
        systolicLimit = newLimit;
        redrawDetails();
    }
}
