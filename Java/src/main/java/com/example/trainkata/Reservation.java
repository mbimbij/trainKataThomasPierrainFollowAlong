package com.example.trainkata;

import java.util.Collection;
import java.util.List;

public class Reservation {
	public final TrainId trainId;
    public final BookingReferenceId bookingId;
    public final Collection<SeatWithBookingReference> seats;

    public Reservation(TrainId trainId, Collection<SeatWithBookingReference> seats, BookingReferenceId bookingId) {
		this.trainId = trainId;
        this.bookingId = bookingId;
        this.seats = seats;
    }

}
