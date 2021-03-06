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
        /**
         * Create new MonitorPatientsTableView
         * @param patients: The CholesterolPatients to have in the table
         */
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
        nameColumn.setPrefWidth(175);
        //Add column for cholesterol
        TableColumn<CholesterolPatient, String> cholesterolColumn = new TableColumn<CholesterolPatient, String>("Total Cholesterol (mg/dL)");
        cholesterolColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("cholesterolString"));
        cholesterolColumn.setPrefWidth(100);
        //Add column for last updated time
        TableColumn<CholesterolPatient, String> timeColumn = new TableColumn<CholesterolPatient, String>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<CholesterolPatient, String>("updateTime"));
        timeColumn.setPrefWidth(100);
        //Add column for remove button
        TableColumn<CholesterolPatient, Button> removeColumn = new TableColumn<>("Remove Patient");
        removeColumn.setCellFactory(ActionButtonTableCell.<CholesterolPatient>forTableColumn("Remove", (patient) ->
                {
                    patients.removePatient(patient);
                    return patient;
                }
        ));
        removeColumn.setPrefWidth(110);
        //Add column for details button
        TableColumn<CholesterolPatient, Button> detailsColumn = new TableColumn<>("Patient Details");
        detailsColumn.setCellFactory(ActionButtonTableCell.<CholesterolPatient>forTableColumn("Details", (patient) ->
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
