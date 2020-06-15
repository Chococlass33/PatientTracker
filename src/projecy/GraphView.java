package projecy;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;

import javax.swing.event.ChangeEvent;
import java.util.ArrayList;

public class GraphView extends Region implements ListChangeListener<DataPatient> {
    private BarChart<String, Number> barChart;

    public GraphView(MonitoredPatientList patientList, ArrayList<DataTypes> selected_types ) {
        CategoryAxis patientAxis = new CategoryAxis();
        patientAxis.setAnimated(false);
        NumberAxis dataAxis = new NumberAxis();
        barChart = new BarChart<String, Number>(patientAxis, dataAxis);
        this.updateData(patientList, selected_types);
        this.getChildren().add(barChart);
        patientList.patients.addListener(this);
    }
    public void updateData(MonitoredPatientList patientList, ArrayList<DataTypes> selected_types) {
        barChart.getData().clear();
        for (int j=0; j < selected_types.size(); j++) {
            for ( int k = 0; k < selected_types.get(j).dataValueCount; k++)  {
                XYChart.Series dataSeries = new XYChart.Series();
                dataSeries.setName(selected_types.get(j).columnLabels.get(k));
                for (int i=0; i < patientList.patients.size(); i++) {
                    DoubleProperty propertyDataValue = patientList.patients.get(i).findData(selected_types.get(j)).dataValue.get(k);
                    XYChart.Data chartData = new XYChart.Data(patientList.patients.get(i).getName(), propertyDataValue.doubleValue());
                    new DatapointChangeListner(chartData, propertyDataValue);
                    dataSeries.getData().add(chartData);

                }
                barChart.getData().add(dataSeries);
            }

        }
    }

    @Override
    public void onChanged(Change<? extends DataPatient> c) {
        while (c.next()) {
            if (c.wasPermutated()){
                System.out.println("Was permutated");
            }
            if (c.wasUpdated()) {
                System.out.println("Was Updated");
            }
            else{
                for(DataPatient patient : c.getRemoved()){
                    for(XYChart.Series<String, Number> dataSeries: barChart.getData()) {
                        for(XYChart.Data chartData : dataSeries.getData()) {
                            if (chartData.getXValue() == patient.getName()) {
                                dataSeries.getData().remove(chartData);
                                break;
                            }
                        }
                    }
                }
                for (DataPatient patient : c.getAddedSubList()) {
                    int valueCounter = 0;
                    DataTypes currentDataType = null;
                    for (XYChart.Series dataSeries : barChart.getData()) {
                        DataTypes newDataType = DataTypes.findFromString(dataSeries.getName());
                        if (currentDataType == newDataType){
                            valueCounter += 1;
                        } else {
                            currentDataType  = newDataType;
                        }
                        DoubleProperty propertyDataValue = patient.findData(currentDataType).dataValue.get(valueCounter);
                        XYChart.Data chartData = new XYChart.Data(patient.getName(), propertyDataValue.doubleValue());
                        new DatapointChangeListner(chartData, propertyDataValue);
                        dataSeries.getData().add(chartData);
                    }
                }
                System.out.println("Was added or removed");
            }

        }
    }


}
