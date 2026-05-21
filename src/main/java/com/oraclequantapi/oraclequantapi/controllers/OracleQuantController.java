package com.oraclequantapi.oraclequantapi.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/OracleQuant")
public class OracleQuantController {

    @GetMapping(path = "/Convert")
    public List<String> Convert(@PathVariable String input) {
        return null;
    }

    @GetMapping(path = "/Fetch")
    public List<String> fetch(@PathVariable String input) {
        return null;
    }

    @GetMapping(path = "/FetchAll")
    public List<String> fetchAll() {
        return null;
    }

    @PutMapping(path = "/Update")
    public List<String> Update(@PathVariable String input) {
        return null;
    }

    @DeleteMapping(path = "/Delete")
    public List<String> delete(@PathVariable String input) {
        return null;
    }
}
