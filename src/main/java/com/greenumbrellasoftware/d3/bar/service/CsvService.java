package com.greenumbrellasoftware.d3.bar.service;

import com.greenumbrellasoftware.d3.bar.data.DataType;
import com.greenumbrellasoftware.d3.bar.exception.DataAccessException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This service will parse CSV files and gather the fields, types of those fields, and
 * values for those fields for each row.
 */
@Component
public class CsvService implements Data {
    private final static String DELIMITER = ",";
//    private final static String CSV_FILE = "./src/main/resources/world-population-by-year-2015.csv";
//    private final static String CSV_FILE = "./src/main/resources/alabama-county-unemployment.csv";
    private final static String CSV_FILE = "./src/main/resources/stateInfo.csv";
    List<DataType> dataTypeList;
    List<Map<String, Object>> data;

    @Autowired
    public CsvService() {
        try {
            List<String> dataLines = FileUtils.readLines(new File(CSV_FILE), "UTF-8");
            dataTypeList = determineBarDataTypes(dataLines);
            data = loadData(dataLines);
        } catch (IOException e) {
            throw new DataAccessException("Unable to read the input CSV file " + CSV_FILE, e);
        }
    }

    @Override
    public List<Map<String, Object>> getData() {
        return data;
    }

    @Override
    public List<DataType> getDataTypes() {
        return dataTypeList;
    }

    /**
     * This method parses the List of lines from the CSV file and determines the fields, based on the header row,
     * and their types, based on the values in the first data row, aka the second row in the
     * file.
     * @param lines
     * @return
     */
    private List<DataType> determineBarDataTypes(List<String> lines) {
        List<DataType> types = new ArrayList<>();
        List<String> header = getHeaders(lines.get(0));
        String[] line1Tokens = lines.get(1).split(DELIMITER);
        if(line1Tokens.length != header.size())
            throw new DataAccessException("The number of header values does not match the entries in the first row.  " +
                    "Please check the data to ensure the appropriate fields are available.");

        for(int i=0; i<line1Tokens.length; i++) {
            DataType dataType = new DataType();
            dataType.setName(header.get(i));
            String fieldType = getFieldType(line1Tokens[i]);
            dataType.setType(fieldType);
            types.add(dataType);
        }
        return types;
    }

    /**
     * This method will determine if the field's value is a string, int or
     * float by looking at the characters that make it up.
     * @param fieldData
     * @return
     */
    private String getFieldType(String fieldData) {
        if(StringUtils.isBlank(fieldData))
            return DataType.STRING;

        boolean decimalFound = false;
        for(char c : fieldData.toCharArray()) {
            if(c == '.' && !decimalFound)
                decimalFound = true;
            else if(c == '.')
                return DataType.STRING;
            else if(c < '0' || c > '9')
                return DataType.STRING;
        }
        return decimalFound ? DataType.FLOAT : DataType.INT;
    }

    private List<Map<String, Object>> loadData(List<String> lines) {
        List<String> headers = getHeaders(lines.get(0));
        lines.remove(0);
        return lines.stream().map(line -> convertLine(line, headers)).collect(Collectors.toList());
    }

    private List<String> getHeaders(String headerLine) {
        return Arrays.asList(headerLine.split(DELIMITER));
    }

    /**
     * This method converts each individual line into its requisite Map<String, Object>
     * where the key is the field name and the value is the value of the field.  Keys
     * are determined from the header row, aka the first line in the CSV file.
     * @param line
     * @param headers
     * @return
     */
    private Map<String, Object> convertLine(String line, List<String> headers) {
        Map<String, Object> valueMap = new HashMap<>();
        String[] tokens = line.split(DELIMITER);
        for(int i=0; i<tokens.length; i++) {
            String columnHeader = headers.get(i);
            DataType matchingDataType = dataTypeList
                    .stream()
                    .filter(m -> StringUtils.equalsIgnoreCase(m.getName(), columnHeader))
                    .findFirst()
                    .get();

            if(StringUtils.equalsIgnoreCase(matchingDataType.getType(), DataType.FLOAT)) {
                valueMap.put(columnHeader, Float.valueOf(tokens[i]));
            } else if(StringUtils.equalsIgnoreCase(matchingDataType.getType(), DataType.INT)) {
                valueMap.put(columnHeader, Long.valueOf(tokens[i]));
            } else {
                valueMap.put(columnHeader, tokens[i]);
            }
        }
        return valueMap;
    }
}
