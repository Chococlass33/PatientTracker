package projecy;


import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.hl7.fhir.r4.model.Patient;

import java.math.BigDecimal;

public class TableController extends VBox {
    private TableView<CholesterolPatient> patientTable;
    public TableController(MonitoredPatients patients) {
        patientTable = new TableView<CholesterolPatient>(patients.patients);
        //Add column for name
        TableColumn<CholesterolPatient, String> nameColumn = new TableColumn<CholesterolPatient, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("name"));
        //Add column for cholesterol
        TableColumn<CholesterolPatient, BigDecimal> cholesterolColumn = new TableColumn<CholesterolPatient, BigDecimal>("Total Cholesterol (mg/dL)");
        cholesterolColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, BigDecimal>("cholesterol"));

        patientTable.getColumns().addAll(nameColumn, cholesterolColumn);
        this.getChildren().add(patientTable);
    }
}
