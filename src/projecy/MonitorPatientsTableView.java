package projecy;


import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class MonitorPatientsTableView extends Region {
    private javafx.scene.control.TableView<CholesterolPatient> patientTable;
    private DetailsView detailsView = new DetailsView();
    private MonitoredPatientList patients;
     public MonitorPatientsTableView(MonitoredPatientList patients) {
        this.patients = patients;
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
        TableColumn<CholesterolPatient, String> cholesterolColumn = new TableColumn<CholesterolPatient, String>("Total Cholesterol (mg/dL)");
        cholesterolColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("cholesterolString"));

        //Add column for last updated time
        TableColumn<CholesterolPatient, String> timeColumn = new TableColumn<CholesterolPatient, String>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("updateTime"));

        //Add column for remove button
        TableColumn<CholesterolPatient, Button> removeColumn = new TableColumn<>("Remove Patient");
        removeColumn.setCellFactory(ActionButtonTableCell.<CholesterolPatient>forTableColumn("Remove", (patient) ->
                {
                    patients.removePatient(patient);
                    return patient;
                }
        ));
        //Add column for details button
        TableColumn<CholesterolPatient, Button> detailsColumn = new TableColumn<>("Show Patient Details");
        detailsColumn.setCellFactory(ActionButtonTableCell.<CholesterolPatient>forTableColumn("Details", (patient) ->
                {
                    detailsView.setDetails(patient);
                    return patient;
                }
                ));

        //Put together into one table
        patientTable.getColumns().addAll(nameColumn, cholesterolColumn,timeColumn,removeColumn, detailsColumn);
        //Organise view
        VBox vBox = new VBox(generateUpdatesView(), patientTable);
        HBox hBox = new HBox(vBox, detailsView);
        this.getChildren().add(hBox);
    }
    private HBox generateUpdatesView() {
        final TextField setUpdateFrequencyField = new TextField("(Seconds, as int)");
        Button setUpdateFrequencyButton = new Button("Set Update Frequency");
        setUpdateFrequencyButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    patients.setUpdateFrequency(Integer.parseInt(setUpdateFrequencyField.getText()));
                    setUpdateFrequencyField.setText("Success!");
                } catch (NumberFormatException exception) {
                    setUpdateFrequencyField.setText("Error, enter valid int");
                }
            }
        });
        return new HBox(setUpdateFrequencyButton, setUpdateFrequencyField);
    }
}
