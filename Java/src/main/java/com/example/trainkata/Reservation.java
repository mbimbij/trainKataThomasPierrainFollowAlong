package com.example.trainkata;

import java.util.Collection;

public class Reservation {
	public final TrainId trainId;
    public final BookingReferenceId bookingId;
    public final Collection<Seat> seats;

    public Reservation(TrainId trainId, Collection<Seat> seats, BookingReferenceId bookingId) {
		this.trainId = trainId;
        this.bookingId = bookingId;
        this.seats = seats;
    }

}
