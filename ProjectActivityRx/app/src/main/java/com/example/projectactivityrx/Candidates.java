package com.example.projectactivityrx;

import java.util.List;

public class Candidates {

    List<Location> results;



    public Candidates(List<Location> locations) {

        this.results = locations;
    }


    public List<Location> getLocations() {

        return results;
    }


    public void setLocations(List<Location> locations)
    {
        this.results = locations;
    }


}
