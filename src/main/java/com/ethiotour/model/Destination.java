package com.ethiotour.model;

import java.time.LocalDate;

public class Destination {
    private int id;
    private String name;
    private String description;
    private String region;
    private double altitude;
    private String entranceProtocol;
    private boolean active;
    private LocalDate createdDate;
    
    public Destination() {
        this.createdDate = LocalDate.now();
        this.active = true;
    }
    
    public Destination(int id, String name, String description, String region, double altitude, String entranceProtocol) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.region = region;
        this.altitude = altitude;
        this.entranceProtocol = entranceProtocol;
        this.createdDate = LocalDate.now();
        this.active = true;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    
    public double getAltitude() { return altitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }
    
    public String getEntranceProtocol() { return entranceProtocol; }
    public void setEntranceProtocol(String entranceProtocol) { this.entranceProtocol = entranceProtocol; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
    
    @Override
    public String toString() {
        return name + " (" + region + ")";
    }
}
