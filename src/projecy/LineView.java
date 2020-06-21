package projecy;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LineView extends Region implements ListChangeListener<Double> {
    private LineChart<String, Number> lineChart;
    private DataPatient patient;
    private Text systolicText = new Text();

    public LineView(DataPatient patient) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setAnimated(false);
        NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<String, Number>(xAxis, yAxis);
        lineChart.setAnimated(false);
        this.patient = patient;
        this.getChildren().add(new VBox(systolicText, lineChart));
        BloodPressureData data = (BloodPressureData) this.patient.findData(DataTypes.Blood_Pressure);
        data.systolicHistoryValues.addListener(this);
        this.updateData(null);
    }

    /**
     * Method to update the data in the lineview with new systolicHistory values
     * @param newPatient: new patient to show the data of. Null if using the same patient as previous
     */
    public void updateData(DataPatient newPatient) {
        if(newPatient != null) {
            this.patient = newPatient;
        }
        if (patient == null) return;
        BloodPressureData data = (BloodPressureData) patient.findData(DataTypes.Blood_Pressure);
        //Set text for requirement 4
        String displayText = "\n";
        displayText += "Systolic Blood Pressure: \n";
        for (int i = 0; i < data.systolicHistoryTimes.size(); i++) {
            displayText += data.systolicHistoryValues.get(i) + " (";
            displayText += data.systolicHistoryTimes.get(i) + "), \n";
        }
        this.systolicText.setText(displayText);
        //Set chart for requirement 5
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
        Platform.runLater(() -> updateData(null));
    }
}



