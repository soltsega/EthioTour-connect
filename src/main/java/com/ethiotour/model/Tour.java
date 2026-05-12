package com.ethiotour.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Tour {
    private int id;
    private String name;
    private String description;
    private Destination destination;
    private int guideId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxParticipants;
    private int currentParticipants;
    private double residentPrice;
    private double nonResidentPrice;
    private TourStatus status;
    private LocalDateTime createdDate;
    
    public enum TourStatus {
        PLANNED, ACTIVE, COMPLETED, CANCELLED
    }
    
    public Tour() {
        this.createdDate = LocalDateTime.now();
        this.status = TourStatus.PLANNED;
        this.currentParticipants = 0;
    }
    
    public Tour(int id, String name, String description, Destination destination, 
                LocalDate startDate, LocalDate endDate, int maxParticipants,
                double residentPrice, double nonResidentPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxParticipants = maxParticipants;
        this.residentPrice = residentPrice;
        this.nonResidentPrice = nonResidentPrice;
        this.createdDate = LocalDateTime.now();
        this.status = TourStatus.PLANNED;
        this.currentParticipants = 0;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Destination getDestination() { return destination; }
    public void setDestination(Destination destination) { this.destination = destination; }
    
    public int getGuideId() { return guideId; }
    public void setGuideId(int guideId) { this.guideId = guideId; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }
    
    public int getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(int currentParticipants) { this.currentParticipants = currentParticipants; }
    
    public double getResidentPrice() { return residentPrice; }
    public void setResidentPrice(double residentPrice) { this.residentPrice = residentPrice; }
    
    public double getNonResidentPrice() { return nonResidentPrice; }
    public void setNonResidentPrice(double nonResidentPrice) { this.nonResidentPrice = nonResidentPrice; }
    
    public TourStatus getStatus() { return status; }
    public void setStatus(TourStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public boolean hasAvailability() {
        return currentParticipants < maxParticipants;
    }
    
    public int getAvailableSlots() {
        return maxParticipants - currentParticipants;
    }
    
    @Override
    public String toString() {
        return name + " - " + destination.getName() + " (" + startDate + " to " + endDate + ")";
    }
}
