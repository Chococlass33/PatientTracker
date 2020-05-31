package projecy;

import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class DetailsView extends Region {
    /**
     * Class to generate a region displaying all of a DataPatient's nessesary details
     */
    private Text textBox = new Text();
    public DetailsView() {
        this.getChildren().add(textBox);
    }
    public void setDetails(DataPatient patient) {
        /**
         * Method to set the data to display
         * @param patient: The patient of which to display the data of
         */
        String displayText = "\n";
        displayText += patient.getName() + "\n";
        displayText += patient.getAddressString();
        displayText += patient.getBirthdateString();
        displayText += patient.getGenderString();
        textBox.setText(displayText);
    }
}
