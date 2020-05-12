package projecy;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DetailsView extends VBox {
    private Text textBox = new Text();
    public DetailsView() {
        this.getChildren().add(textBox);
    }
    public void setDetails(CholesterolPatient patient) {
        String displayText = "";
        displayText += patient.getAddressString();
        displayText += patient.getBirthdateString();
        displayText += patient.getGenderString();
        textBox.setText(displayText);
    }
}
