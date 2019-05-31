/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import com.microsoft.azure.sample.controller.LoadHelper;

@RestController
public class TodoListController {

    public TodoListController() {
    }

    @RequestMapping("/home")
    public Map<String, Object> home() {
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "home");
        return model;
    }

    /**
     * HTTP GET
     * Consumes memory. Should never return a successful code.
     */
    @RequestMapping(value = "/api/memoryTest/{bytes}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> performMemoryTest(
        @PathVariable("bytes") int numBytes) {
        try {
            Vector<byte[]> vector = new Vector<byte[]>();
            while (true) {
                //byte bytes[] = new byte[1048576];  // 1MB
                byte bytes[] = new byte[numBytes];  // cutting by half
                vector.add(bytes);

                Runtime rt = Runtime.getRuntime();
                System.out.println( "free memory: " + rt.freeMemory() );
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Error encountered during Memory load test.", HttpStatus.I_AM_A_TEAPOT);
        }
    }
    
    /**
     * HTTP GET
     * Consumes CPU. Should never return a successful code.
     */
    @RequestMapping(value = "/api/cpuTest/cores/{cores}/threads/{threads}/load/{load}/duration/{duration}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> performCPUTest(
        @PathVariable("cores") int cores,
        @PathVariable("threads") int threads,
        @PathVariable("load") double load,
        @PathVariable("duration") long duration) {
        try {
            LoadHelper.execute(cores, threads, load, duration);
            return new ResponseEntity<String>(String.format("CPU test complete."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error encountered during CPU load test.", HttpStatus.I_AM_A_TEAPOT);
        }
    }
    
}
