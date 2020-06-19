package projecy;


import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
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
    private GraphView graphView;
    private int columnsBeforeData = 1;
    private int columnsAfterData = 2;
    private Double systolicbloodpressurelimit = 140.0;
    private Double diastolicbloodpressurelimit = 90.0;

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
                    /* Obsolete Method. see similar code in DrawDataColumns to re-implement this feature
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
        //Generate Graph View
        graphView = new GraphView(this.patients, this.selected_types);
        //Organise view
        VBox vBox = new VBox(generateUpdatesView(), patientTable, generateDataCheckBoxes(),generateXY());
        HBox hBox = new HBox(vBox, detailsView, graphView);
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
            int finalI = i;
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
            //Add columns for Data Values
            for (int j = 0; j < selected_types.get(i).DATA_VALUE_COUNT; j++) {
                int finalj = j;
                TableColumn<DataPatient, String> DataValueColumn = new TableColumn<DataPatient, String>(selected_types.get(i).COLUMN_LABELS.get(j));
                //Set the values of the new cells with the stringproperty of the datas
                DataValueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataPatient, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<DataPatient, String> param) {
                        return param.getValue().findData(selected_types.get(finalI)).stringProperty(finalj);
                    }
                });
                //Set colouring constraints for each datatype
                if (selected_types.get(i) == DataTypes.Cholesterol) {
                    DataValueColumn.setCellFactory(new Callback<TableColumn<DataPatient, String>, TableCell<DataPatient,String>>() {
                        @Override
                        public TableCell call(TableColumn<DataPatient, String> param) {
                            TableCell cell = new TableCell<DataPatient, String>()
                            {
                                @Override
                                public void updateItem(String item, boolean empty)
                                {
                                    super.updateItem(item, empty);
                                    if (!empty)
                                    {
                                        int currentIndex = indexProperty()
                                                .getValue() < 0 ? 0
                                                : indexProperty().getValue();
                                        String clmStatus = param.getCellData(currentIndex);

                                       if (Double.parseDouble(clmStatus.replace("mg/dL","")) >= patients.averageValue(selected_types.get(finalI), finalj))
                                        {
                                            setStyle("-fx-background-color: red");
                                            setText(clmStatus);
                                        }
                                        else
                                        {
                                            setStyle("-fx-background-color: blue");
                                            setText(clmStatus);
                                        }
                                    }
                                    else
                                    {
                                        setText("");
                                        setStyle("");
                                    }
                                }
                            };
                            return cell;

                        }
                    });
                }
                else { //Selected type is Blood Pressure



                    DataValueColumn.setCellFactory(new Callback<TableColumn<DataPatient, String>, TableCell<DataPatient,String>>() {
                        @Override
                        public TableCell call(TableColumn<DataPatient, String> param) {
                            TableCell cell = new TableCell<DataPatient, String>()
                            {
                                @Override
                                public void updateItem(String item, boolean empty)
                                {
                                    super.updateItem(item, empty);
                                    if (!empty)
                                    {
                                        int currentIndex = indexProperty()
                                                .getValue() < 0 ? 0
                                                : indexProperty().getValue();
                                        String clmStatus = param.getCellData(currentIndex);
                                        if(param.getText().contains("Systolic"))
                                        {
                                            if (Double.parseDouble(clmStatus.replace("mm[Hg]", "")) <= systolicbloodpressurelimit)
                                            {
                                                setStyle("-fx-background-color: green");
                                                setText(clmStatus);
                                            }
                                            else
                                            {
                                                setStyle("-fx-background-color: red");
                                                setText(clmStatus);
                                            }
                                        }
                                        else
                                        {
                                            if (Double.parseDouble(clmStatus.replace("mm[Hg]", "")) <= diastolicbloodpressurelimit)
                                            {
                                                setStyle("-fx-background-color: blue");
                                                setText(clmStatus);
                                            }
                                            else
                                            {
                                                setStyle("-fx-background-color: red");
                                                setText(clmStatus);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        setText("");
                                        setStyle("");
                                    }
                                }
                                };
                            return cell;

                        }
                    });
                }
                DataValueColumn.setPrefWidth(100);
                this.patientTable.getColumns().add(columnsBeforeData, DataValueColumn);
            }
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
                    graphView.updateData(patients, selected_types);
                }
            });
            checkboxes.add(cbox);

        }
        HBox returnBox = new HBox();
        returnBox.getChildren().addAll(checkboxes);
        return returnBox;
    }
    private Region generateXY() {
        GridPane grid = new GridPane();
        Label xlabel = new Label("X Value:");
        grid.add(xlabel,0,0);
        TextField xfield = new TextField();
        xfield.setText("140");
        grid.add(xfield,1,0);
        Label ylabel = new Label("Y Value:");
        grid.add(ylabel,2,0);
        TextField yfield = new TextField();
        yfield.setText("90");
        grid.add(yfield,3,0);

        xfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try
                {
                    systolicbloodpressurelimit = Double.parseDouble(xfield.getText());
                }
                catch(Exception e)
                {

                }
                drawDataColumns();
            }
        });
        xfield.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try
                {
                    systolicbloodpressurelimit = Double.parseDouble(xfield.getText());
                }
                catch(Exception e)
                {

                }
                drawDataColumns();
            }
        });
        yfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try
                {
                    diastolicbloodpressurelimit = Double.parseDouble(yfield.getText());
                }
                catch(Exception e)
                {

                }

                drawDataColumns();
            }
        });
        yfield.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try
                {
                    diastolicbloodpressurelimit = Double.parseDouble(yfield.getText());
                }
                catch(Exception e)
                {

                }

                drawDataColumns();
            }
        });


        HBox returnBox = new HBox();
        returnBox.getChildren().addAll(grid);
        return returnBox;
    }
}
