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
    private javafx.scene.control.TableView<DataPatient> patientTable;
    private DetailsView detailsView = new DetailsView();
    private MonitoredPatientList patients;
    private static String CHOLESTEROL_DATA = "Cholesterol";
    public MonitorPatientsTableView(MonitoredPatientList patients) {
        /**
         * Create new MonitorPatientsTableView
         * @param patients: The DataPatients to have in the table
         */
        this.patients = patients;
        patientTable = new javafx.scene.control.TableView<DataPatient>(patients.patients);
        TableColumn<DataPatient,String> nameColumn = new TableColumn<DataPatient,String>("First Name");
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataPatient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DataPatient, String> p)
            {
                p.getTableView().setRowFactory(q -> new TableRow<DataPatient>()
                {
                    public void updateItem(DataPatient patient, boolean empty) {
                    super.updateItem(patient, empty) ;
                    if (patient == null) {
                        setStyle("");
                    } else if (patients.isBelowAverage(patient, CHOLESTEROL_DATA)) {
                        setStyle("-fx-background-color: tomato;");
                    } else {
                        setStyle("-fx-background-color: green;");
                    }
                }
                });
                return new ReadOnlyStringWrapper(p.getValue().getName());
            }
        });
        nameColumn.setPrefWidth(175);
        //Add column for cholesterol
        TableColumn<DataPatient, String> cholesterolColumn = new TableColumn<DataPatient, String>("Total Cholesterol (mg/dL)");
        cholesterolColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataPatient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DataPatient, String> param) {

                return param.getValue().findData(CHOLESTEROL_DATA).StringProperty();
            }
        });
        cholesterolColumn.setPrefWidth(100);
        //Add column for last updated time
        TableColumn<DataPatient, String> timeColumn = new TableColumn<DataPatient, String>("Time");
        timeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataPatient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DataPatient, String> param) {

                return param.getValue().findData(CHOLESTEROL_DATA).timeProperty();
            }
        });
        timeColumn.setPrefWidth(100);
        //Add column for remove button
        TableColumn<DataPatient, Button> removeColumn = new TableColumn<>("Remove Patient");
        removeColumn.setCellFactory(ActionButtonTableCell.<DataPatient>forTableColumn("Remove", (patient) ->
                {
                    patients.removePatient(patient);
                    return patient;
                }
        ));
        removeColumn.setPrefWidth(110);
        //Add column for details button
        TableColumn<DataPatient, Button> detailsColumn = new TableColumn<>("Patient Details");
        detailsColumn.setCellFactory(ActionButtonTableCell.<DataPatient>forTableColumn("Details", (patient) ->
                {
                    detailsView.setDetails(patient);
                    return patient;
                }
                ));

        //Put together into one table
        patientTable.getColumns().addAll(nameColumn, cholesterolColumn,timeColumn,removeColumn, detailsColumn);
        detailsColumn.setPrefWidth(100);
        //Organise view
        VBox vBox = new VBox(generateUpdatesView(), patientTable);
        HBox hBox = new HBox(vBox, detailsView);
        this.getChildren().add(hBox);
    }
    private Region generateUpdatesView() {
        /**
         * This function generates the updates view containing the update frequency button and the entry text field
         * @return: the Region containing the newly generated view
         */
        final TextField setUpdateFrequencyField = new TextField("(Seconds, as int)");
        Button setUpdateFrequencyButton = new Button("Set Update Frequency");
        setUpdateFrequencyButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                /**
                 * Function to read the text in the textfield and set it as the update frequency when the button is pressed
                 */
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
