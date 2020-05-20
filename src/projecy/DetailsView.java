package projecy;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DetailsView extends Region {
    private Text textBox = new Text();
    public DetailsView() {
        this.getChildren().add(textBox);
    }
    public void setDetails(CholesterolPatient patient) {
        String displayText = "\n";
        displayText += patient.getName() + "\n";
        displayText += patient.getAddressString();
        displayText += patient.getBirthdateString();
        displayText += patient.getGenderString();
        textBox.setText(displayText);
    }
}
