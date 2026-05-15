package com.ethiotour.service;

import com.ethiotour.model.Booking;
import com.ethiotour.model.Tour;
import com.ethiotour.util.EthiopianCalendar;
import java.time.LocalDate;

public class BookingService {
    private IDatabaseService dbService;
    
    public BookingService() {
        this.dbService = DatabaseServiceFactory.getDatabaseService();
    }
    
    public BookingValidationResult validateBooking(int tourId, int participantsCount, boolean isResident) {
        Tour tour = dbService.getTourById(tourId);
        if (tour == null) {
            return new BookingValidationResult(false, "Tour not found");
        }
        
        if (!tour.hasAvailability()) {
            return new BookingValidationResult(false, "No available slots for this tour");
        }
        
        if (participantsCount > tour.getAvailableSlots()) {
            return new BookingValidationResult(false, "Only " + tour.getAvailableSlots() + " slots available");
        }
        
        if (participantsCount <= 0) {
            return new BookingValidationResult(false, "Number of participants must be greater than 0");
        }
        
        // Check if tour date is in the past
        if (tour.getStartDate().isBefore(LocalDate.now())) {
            return new BookingValidationResult(false, "Cannot book tours that have already started");
        }
        
        // Peak season pricing warning
        if (EthiopianCalendar.isPeakSeason(tour.getStartDate())) {
            return new BookingValidationResult(true, "Peak season pricing applies. Higher rates may be charged.");
        }
        
        return new BookingValidationResult(true, "Booking can proceed");
    }
    
    public double calculatePrice(int tourId, int participantsCount, boolean isResident) {
        Tour tour = dbService.getTourById(tourId);
        if (tour == null) {
            return 0;
        }
        
        double basePrice = isResident ? tour.getResidentPrice() : tour.getNonResidentPrice();
        double totalPrice = basePrice * participantsCount;
        
        // Peak season surcharge (20%)
        if (EthiopianCalendar.isPeakSeason(tour.getStartDate())) {
            totalPrice *= 1.2;
        }
        
        return totalPrice;
    }
    
    public Booking createBooking(int tourId, String customerName, String customerEmail, 
                                String customerPhone, boolean isResident, int participantsCount) {
        // Validate booking first
        BookingValidationResult validation = validateBooking(tourId, participantsCount, isResident);
        if (!validation.isValid()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        
        // Calculate price
        double totalPrice = calculatePrice(tourId, participantsCount, isResident);
        
        // Create booking
        Booking booking = new Booking(tourId, customerName, customerEmail, customerPhone, isResident, participantsCount);
        booking.setTotalPrice(totalPrice);
        
        // Add to database
        dbService.addBooking(booking);
        
        return booking;
    }
    
    public boolean confirmBooking(int bookingId, String paymentMethod, String paymentReference) {
        Booking booking = dbService.getBookingById(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (booking.getStatus() != Booking.BookingStatus.PENDING_CONFIRMATION) {
            return false;
        }
        
        booking.setPaymentMethod(paymentMethod);
        booking.setPaymentReference(paymentReference);
        booking.updateStatus(Booking.BookingStatus.CONFIRMED);
        
        dbService.updateBooking(booking);
        return true;
    }
    
    public boolean processPayment(int bookingId) {
        Booking booking = dbService.getBookingById(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            return false;
        }
        
        // Simulate payment processing
        booking.updateStatus(Booking.BookingStatus.PAID);
        dbService.updateBooking(booking);
        
        return true;
    }
    
    public boolean cancelBooking(int bookingId) {
        Booking booking = dbService.getBookingById(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (booking.getStatus() == Booking.BookingStatus.COMPLETED || 
            booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            return false;
        }
        
        booking.updateStatus(Booking.BookingStatus.CANCELLED);
        
        // Update tour participant count
        Tour tour = dbService.getTourById(booking.getTourId());
        if (tour != null) {
            tour.setCurrentParticipants(tour.getCurrentParticipants() - booking.getParticipantsCount());
        }
        
        dbService.updateBooking(booking);
        return true;
    }
    
    public static class BookingValidationResult {
        private boolean valid;
        private String message;
        
        public BookingValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
    }
}
