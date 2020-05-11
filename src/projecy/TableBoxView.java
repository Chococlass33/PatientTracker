package projecy;


import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;

public class TableBoxView extends VBox {
    private javafx.scene.control.TableView<CholesterolPatient> patientTable;
    public TableBoxView(MonitoredPatients patients) {
        patientTable = new javafx.scene.control.TableView<CholesterolPatient>(patients.patients);
        //Add column for name
        TableColumn<CholesterolPatient, String> nameColumn = new TableColumn<CholesterolPatient, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("name"));
        //Add column for cholesterol
        TableColumn<CholesterolPatient, BigDecimal> cholesterolColumn = new TableColumn<CholesterolPatient, BigDecimal>("Total Cholesterol (mg/dL)");
        cholesterolColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, BigDecimal>("cholesterol"));
        //Add column for remove button
        TableColumn<CholesterolPatient, Button> removecolumn = new TableColumn<>("Remove Patient");
        removecolumn.setCellFactory(ActionButtonTableCell.<CholesterolPatient>forTableColumn("Remove", (patient) ->
                {
                    patients.removePatient(patient);
                    return patient;
                }
                ));

        patientTable.getColumns().addAll(nameColumn, cholesterolColumn,removecolumn);
        this.getChildren().add(patientTable);
    }
}
