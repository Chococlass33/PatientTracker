package projecy;


import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.hl7.fhir.r4.model.Patient;

public class TableController extends VBox {
    private TableView<CholesterolPatient> patientTable;
    public TableController(MonitoredPatients patients) {
        patientTable = new TableView<CholesterolPatient>(patients.patients);
        TableColumn<CholesterolPatient, String> nameColumn = new TableColumn<CholesterolPatient, String>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("name"));

        patientTable.getColumns().add(nameColumn);
        this.getChildren().add(patientTable);
    }
}
