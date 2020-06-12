package com.greenumbrellasoftware.d3.bar.controller;

import com.greenumbrellasoftware.d3.bar.service.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
//NOTE: Because this project serves up static content from /, you have to
//provide a RequestMapping for other API calls
@RequestMapping("result")
public class Controller {

    private Data dataService;

    @Autowired
    public Controller(Data dataService) {
        this.dataService = dataService;
    }

    /**
     * API call that will return the canned results.  Could be augmented for
     * parameters to generate a query to a datastore.
     * @return
     * @throws IOException
     */
    @GetMapping
    public ResponseEntity getUnemploymentData() throws IOException {
        return ResponseEntity.ok(dataService.getData());
    }

    /**
     * API call that will return all the available fields from the
     * configured data store/set and their respective data type.
     * @return
     */
    @GetMapping("/datatype")
    public ResponseEntity getResponseDataTypes() {
        return ResponseEntity.ok(dataService.getDataTypes());
    }

}
