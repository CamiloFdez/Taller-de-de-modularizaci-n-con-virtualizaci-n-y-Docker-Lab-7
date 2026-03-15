package edu.eci.dockerLab.controllers;

import edu.eci.dockerLab.annotations.GetMapping;
import edu.eci.dockerLab.annotations.RequestParam;
import edu.eci.dockerLab.annotations.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(value="name", defaultValue="World") String name) {

        return "Hola " + name;
    }
}