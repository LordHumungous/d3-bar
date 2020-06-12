package com.greenumbrellasoftware.d3.bar.service;

import com.greenumbrellasoftware.d3.bar.data.DataType;
import com.greenumbrellasoftware.d3.bar.exception.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * This interface's job is to define the result structure that goes back to the UI
 * Implementing classes will be responsible for adhering to this structure to allow
 * the UI to digest the data without any required changes.
 */
public interface Data {
    /**
     * This method will return name/type pairs that state what each field's type is of the data set.
     * It should adhere to the name: <fieldName>, type: <int|float|string|...>
     * @return
     */
    List<DataType> getDataTypes();

    /**
     * This method returns a list of data records where each individual Map has the
     * structure fieldname: value for each entry.  The fields returned MUST map to the
     * fields and types returned by getDataTypes().  That is how consumers will know what
     * data is available and what type that data is.  For example, the overall list for
     * State information could look like this JSON structure.
     * [
     *      {
     *          'name': 'Alabama',
     *          'abbr': 'AL',
     *          'becameStateIn': '12-14-1819',
     *          'becameStateCount': 22
     *      }, {
     *          'name': 'Alaska',
     *          'abbr': 'AK',
     *          'becameStateIn': '01-03-1959',
     *          'becameStateCount': 49
     *      }, ...
     * ]
     * @return A List of Maps representing field/value pairs for each field of the record.
     * @throws DataAccessException
     */
    List<Map<String, Object>> getData() throws DataAccessException;
}
