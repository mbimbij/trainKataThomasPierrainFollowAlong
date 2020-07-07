package com.example.trainkata;

public class SeatWithBookingReference extends Seat {
    public final BookingReferenceId bookingReferenceid;

    public SeatWithBookingReference(String coach, int seatNumber, BookingReferenceId bookingReferenceid) {
        super(coach, seatNumber);
        this.bookingReferenceid = bookingReferenceid;
    }

    public boolean isAvailable() {
        return bookingReferenceid.isNull();
    }

}