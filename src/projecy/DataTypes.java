package projecy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum DataTypes {
    Cholesterol("2093-3", 1, new ArrayList(Arrays.asList("Cholesterol"))),
    Blood_Pressure("55284-4", 2, new ArrayList(Arrays.asList("Diastolic Blood Pressure", "Systolic Blood Pressure")));
    public final  int dataValueCount;
    public final String code;
    public final ArrayList<String> columnLabels;
    DataTypes(String s, int valueCount, ArrayList columnLabels) {
        this.code = s;
        this.dataValueCount = valueCount;
        this.columnLabels = columnLabels;
    }
}
