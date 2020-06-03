package projecy;

public enum DataTypes {
    Cholesterol("2093-3", 1),
    Blood_Pressure("55284-4", 2);
    public final  int dataValueCount;
    public final String code;
    DataTypes(String s, int valueCount) {
        this.code = s;
        this.dataValueCount = valueCount;
    }
}
