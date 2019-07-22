package com.example.projectactivityld;


import java.util.List;

public class Candidates {


    private List<Location> results;


    public Candidates(List<Location> locations) {

        this.results = locations;
    }






    public List<Location> getLocations() {
        return results;
    }

    public void setLocations(List<Location> locations) {

        this.results = locations;
    }






}
