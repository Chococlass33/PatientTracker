package projecy;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;

public class LineView extends Region implements ListChangeListener<Double> {
    private LineChart<String, Number> lineChart;
    private DataPatient patient;

    public LineView(DataPatient patient) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setAnimated(false);
        NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<String, Number>(xAxis, yAxis);
        lineChart.setAnimated(false);
        this.patient = patient;
        this.updateData();
        this.getChildren().add(lineChart);
        BloodPressureData data = (BloodPressureData) this.patient.findData(DataTypes.Blood_Pressure);
        data.systolicHistoryValues.addListener(this);
        this.updateData();
    }
    public void updatePatient(DataPatient patient) {
        this.patient = patient;
    }

    public void updateData() {
        if (patient == null) return;
        BloodPressureData data = (BloodPressureData) patient.findData(DataTypes.Blood_Pressure);
        XYChart.Series series;
        if(lineChart.getData().size() == 0) {
            series = new XYChart.Series();
            this.lineChart.getData().add(series);
        } else {
            series = lineChart.getData().get(0);
            series.getData().clear();
        }
        for (int i = 0; i < data.systolicHistoryTimes.size(); i++) {
            XYChart.Data chartData = new XYChart.Data(data.systolicHistoryTimes.get(i), data.systolicHistoryValues.get(i));
            series.getData().add(chartData);
        }
        series.setName(patient.getName());
    }


    @Override
    public void onChanged(Change<? extends Double> c) {
        Platform.runLater(this::updateData);
    }
}



