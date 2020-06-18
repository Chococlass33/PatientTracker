package projecy;


import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.XYChart;

public class DatapointChangeListner implements ChangeListener<Number> {
    private XYChart.Data dataPoint;
    private DoubleProperty doubleProperty;

    public DatapointChangeListner(XYChart.Data dataPoint, DoubleProperty doubleProperty){
        this.dataPoint = dataPoint;
        this.doubleProperty = doubleProperty;
        doubleProperty.addListener(this);

    }
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        this.dataPoint.setYValue(newValue.doubleValue());
    }
}

