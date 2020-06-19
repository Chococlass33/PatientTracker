package projecy;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;

public class LineView extends Region
{
    private LineChart<Number, Number> lineChart;

    public LineView(DataPatient patient)
    {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setAnimated(false);
        NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setAnimated(false);
        this.updateData(patient);
        this.getChildren().add(lineChart);
    }

    public void updateData(DataPatient patient)
    {
        lineChart.getData().clear();
        if (patient == null) return;
        BloodPressureData data = (BloodPressureData) patient.findData(DataTypes.Blood_Pressure);
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < data.systolicHistoryTimes.size(); i++)
        {
            series.getData().add(new XYChart.Data(i+1, Float.parseFloat(data.systolicHistoryValues.get(i).toString())));
        }
        series.setName(patient.getName());
        lineChart.getData().add(series);
    }
}

//        for (int j=0; j < selected_types.size(); j++) {
//            for (int k = 0; k < selected_types.get(j).DATA_VALUE_COUNT; k++)  {
//                XYChart.Series dataSeries = new XYChart.Series();
//                dataSeries.setName(selected_types.get(j).COLUMN_LABELS.get(k));
//                for (int i=0; i < patientList.patients.size(); i++) {
//                    DoubleProperty propertyDataValue = patientList.patients.get(i).findData(selected_types.get(j)).getDataValue().get(k);
//                    XYChart.Data chartData = new XYChart.Data(patientList.patients.get(i).getName(), propertyDataValue.doubleValue());
//                    new DatapointChangeListner(chartData, propertyDataValue);
//                    dataSeries.getData().add(chartData);
//
//                }
//                barChart.getData().add(dataSeries);
//            }
//
//        }

//    @Override
//    public void onChanged(Change<? extends DataPatient> c) {
//        while (c.next()) {
//            if (c.wasPermutated()){
//                System.out.println("Was permutated");
//            }
//            if (c.wasUpdated()) {
//                System.out.println("Was Updated");
//            }
//            else{
//                for(DataPatient patient : c.getRemoved()){
//                    for(XYChart.Series<String, Number> dataSeries: barChart.getData()) {
//                        for(XYChart.Data chartData : dataSeries.getData()) {
//                            if (chartData.getXValue() == patient.getName()) {
//                                dataSeries.getData().remove(chartData);
//                                break;
//                            }
//                        }
//                    }
//                }
//                for (DataPatient patient : c.getAddedSubList()) {
//                    int valueCounter = 0;
//                    DataTypes currentDataType = null;
//                    for (XYChart.Series dataSeries : barChart.getData()) {
//                        DataTypes newDataType = DataTypes.findFromString(dataSeries.getName());
//                        if (currentDataType == newDataType){
//                            valueCounter += 1;
//                        } else {
//                            currentDataType  = newDataType;
//                        }
//                        DoubleProperty propertyDataValue = patient.findData(currentDataType).getDataValue().get(valueCounter);
//                        XYChart.Data chartData = new XYChart.Data(patient.getName(), propertyDataValue.doubleValue());
//                        new DatapointChangeListner(chartData, propertyDataValue);
//                        dataSeries.getData().add(chartData);
//                    }
//                }
//                System.out.println("Was added or removed");
//            }
//
//        }
//    }



