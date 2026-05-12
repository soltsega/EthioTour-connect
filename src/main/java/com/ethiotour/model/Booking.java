package com.ethiotour.model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int tourId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private boolean isResident;
    private int participantsCount;
    private double totalPrice;
    private BookingStatus status;
    private String paymentMethod;
    private String paymentReference;
    private LocalDateTime bookingDate;
    private LocalDateTime lastUpdated;
    
    public enum BookingStatus {
        PENDING_CONFIRMATION, CONFIRMED, PAID, CANCELLED, COMPLETED
    }
    
    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.status = BookingStatus.PENDING_CONFIRMATION;
        this.participantsCount = 1;
    }
    
    public Booking(int tourId, String customerName, String customerEmail, 
                   String customerPhone, boolean isResident, int participantsCount) {
        this.tourId = tourId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.isResident = isResident;
        this.participantsCount = participantsCount;
        this.bookingDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.status = BookingStatus.PENDING_CONFIRMATION;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getTourId() { return tourId; }
    public void setTourId(int tourId) { this.tourId = tourId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    
    public boolean isResident() { return isResident; }
    public void setResident(boolean resident) { isResident = resident; }
    
    public int getParticipantsCount() { return participantsCount; }
    public void setParticipantsCount(int participantsCount) { this.participantsCount = participantsCount; }
    
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }
    
    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public void updateStatus(BookingStatus newStatus) {
        this.status = newStatus;
        this.lastUpdated = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Booking #" + id + " - " + customerName + " (" + status + ")";
    }
}
