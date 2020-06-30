package com.example.trainkata;

import org.apache.commons.lang3.StringUtils;

public class SeatWithBookingReference extends Seat {
    public final BookingReferenceId bookingReferenceid;

    public SeatWithBookingReference(String coach, int seatNumber, BookingReferenceId bookingReferenceid) {
        super(coach, seatNumber);
        this.bookingReferenceid = bookingReferenceid;
    }

    public boolean isAvailable() {
        return StringUtils.isBlank(bookingReferenceid.getValue());
    }

}