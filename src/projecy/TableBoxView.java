package projecy;


import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.math.BigDecimal;

public class TableBoxView extends VBox {
    private javafx.scene.control.TableView<CholesterolPatient> patientTable;
    public TableBoxView(MonitoredPatients patients) {
        patientTable = new javafx.scene.control.TableView<CholesterolPatient>(patients.patients);

        TableColumn<CholesterolPatient,String> nameColumn = new TableColumn<CholesterolPatient,String>("First Name");
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CholesterolPatient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CholesterolPatient, String> p)
            {
                p.getTableView().setRowFactory(q -> new TableRow<CholesterolPatient>()
                {
                    public void updateItem(CholesterolPatient patient, boolean empty) {
                    super.updateItem(patient, empty) ;
                    if (patient == null) {
                        setStyle("");
                    } else if (patients.isBelowAverage(patient)) {
                        setStyle("-fx-background-color: tomato;");
                    } else {
                        setStyle("-fx-background-color: green;");
                    }
                }
                });
                return new ReadOnlyStringWrapper(p.getValue().getName());
            }

        });

        //Add column for name
        TableColumn<CholesterolPatient, String> nameColumn2 = new TableColumn<CholesterolPatient, String>("Name");
        nameColumn2.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("name"));

        //Add column for cholesterol
        TableColumn<CholesterolPatient, BigDecimal> cholesterolColumn = new TableColumn<CholesterolPatient, BigDecimal>("Total Cholesterol (mg/dL)");
        cholesterolColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, BigDecimal>("cholesterol"));

        //Add column for last updated time
        TableColumn<CholesterolPatient, String> timeColumn = new TableColumn<CholesterolPatient, String>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("updateTime"));

        //Add column for remove button
        TableColumn<CholesterolPatient, Button> removecolumn = new TableColumn<>("Remove Patient");
        removecolumn.setCellFactory(ActionButtonTableCell.<CholesterolPatient>forTableColumn("Remove", (patient) ->
                {
                    patients.removePatient(patient);
                    return patient;
                }
        ));

        //Put together into one table
        patientTable.getColumns().addAll(nameColumn, cholesterolColumn,timeColumn,removecolumn);

        this.getChildren().add(patientTable);
    }
}
