package projecy;


import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;

public class MonitorPatientsTableView extends Region {
    private javafx.scene.control.TableView<DataPatient> patientTable;
    private DetailsView detailsView = new DetailsView();
    private MonitoredPatientList patients;
    private ArrayList<DataTypes> selected_types = new ArrayList();
    private int columnsBeforeData = 1;
    private int columnsAfterData = 2;
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
                    /*
                    if (patient == null) {
                        setStyle("");
                    } else if (patients.isBelowAverage(patient, CHOLESTEROL_DATA)) {
                        setStyle("-fx-background-color: tomato;");
                    } else {
                        setStyle("-fx-background-color: green;");
                    }
                    */
                }
                });
                return new ReadOnlyStringWrapper(p.getValue().getName());
            }
        });
        nameColumn.setPrefWidth(175);
        patientTable.getColumns().add(nameColumn);
        drawDataColumns();

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
        patientTable.getColumns().addAll(removeColumn, detailsColumn);
        detailsColumn.setPrefWidth(100);
        //Organise view
        VBox vBox = new VBox(generateUpdatesView(), patientTable, generateDataCheckBoxes());
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
    private void drawDataColumns() {
        this.patientTable.getColumns().remove(columnsBeforeData, patientTable.getColumns().size() - columnsAfterData);
        for(int i = 0; i < selected_types.size(); i++) {
            //Add column for Data Value
            TableColumn<DataPatient, String> DataValueColumn = new TableColumn<DataPatient, String>(selected_types.get(i).name());
            int finalI = i;
            DataValueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataPatient, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<DataPatient, String> param) {
                    param.getTableView().setRowFactory(q -> new TableRow<DataPatient>()
                    {
                        public void updateItem(DataPatient patient, boolean empty) {
                            super.updateItem(patient, empty) ;

                            if (patients.isBelowAverage(param.getValue(), selected_types.get(finalI)) == null) {
                                setStyle("");
                            } else if (patients.isBelowAverage(param.getValue(), selected_types.get(finalI))) {
                                setStyle("-fx-background-color: tomato;");
                            } else {
                                setStyle("-fx-background-color: green;");
                            }

                        }
                    });
                    return param.getValue().findData(selected_types.get(finalI)).StringProperty();
                }
            });
            DataValueColumn.setPrefWidth(100);
            //Add column for last updated time
            TableColumn<DataPatient, String> timeColumn = new TableColumn<DataPatient, String>("Time");
            timeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataPatient, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<DataPatient, String> param) {

                    return param.getValue().findData(selected_types.get(finalI)).timeProperty();
                }
            });
            timeColumn.setPrefWidth(100);
            this.patientTable.getColumns().add(columnsBeforeData, timeColumn);
            this.patientTable.getColumns().add(columnsBeforeData, DataValueColumn);
        }
    }
    private Region generateDataCheckBoxes() {
        ArrayList<CheckBox> checkboxes = new ArrayList();

        for (DataTypes type : DataTypes.values()) {
            CheckBox cbox = new CheckBox(type.name());
            cbox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    if (cbox.isSelected()) {
                        selected_types.add(type);
                    }
                    else {
                        selected_types.remove(type);
                    }
                    patients.setUpdateTypes(selected_types);
                    drawDataColumns();
                }
            });
            checkboxes.add(cbox);

        }
        HBox returnBox = new HBox();
        returnBox.getChildren().addAll(checkboxes);
        return returnBox;
    }
}
