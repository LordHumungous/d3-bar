package com.greenumbrellasoftware.d3.bar.data;

/**
 * This class defines the types of values that can be sent back to the UI/API caller.
 * The structure is name: fieldname, such as 'state', and value: int|float\string.
 * This way the caller will know the data types and fields available for display.
 */
public class DataType {
    public static final String INT = "int";
    public static final String FLOAT = "float";
    public static final String STRING = "string";

    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
