package projecy;

import java.util.ArrayList;
import java.util.Arrays;

public enum DataTypes {
    Cholesterol("2093-3", 1, new ArrayList(Arrays.asList("Cholesterol"))),
    Blood_Pressure("55284-4", 2, new ArrayList(Arrays.asList("Diastolic Blood Pressure", "Systolic Blood Pressure")));
    public final  int DATA_VALUE_COUNT;
    public final String CODE;
    public final ArrayList<String> COLUMN_LABELS;
    DataTypes(String s, int valueCount, ArrayList columnLabels) {
        this.CODE = s;
        this.DATA_VALUE_COUNT = valueCount;
        this.COLUMN_LABELS = columnLabels;
    }

    /**
     * finds and returns a datatype given the associated string of this dataType
     * @param columnLabel: The columnLabel string held in the COLUMN_LABELS arrayList property
     * @return: the DataTypes enum of the associated type. Null if string doesn't appear as a COLUMN_LABEL
     */
    public static DataTypes findFromString(String columnLabel) {
        for(DataTypes type : DataTypes.values()) {
            if (type.COLUMN_LABELS.contains(columnLabel)) {
                return type;
            }
        }
        return null;
    }
}
